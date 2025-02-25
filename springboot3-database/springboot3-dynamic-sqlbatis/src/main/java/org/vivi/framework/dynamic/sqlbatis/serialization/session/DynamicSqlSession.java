package org.vivi.framework.dynamic.sqlbatis.serialization.session;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.logging.jdbc.ConnectionLogger;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class DynamicSqlSession extends DynamicSqlSessionBatch {
    private static final Log logger = LogFactory.getLog(DynamicSqlSession.class);
    SqlSession sqlSession;
    SqlSessionFactory sqlSessionFactory;
    DynamicMappedStatementHelper dynamicMappedStatementHelper;


    public DynamicSqlSession(SqlSession sqlSession, SqlSessionFactory sqlSessionFactory) {
        this.sqlSession = sqlSession;
        this.sqlSessionFactory = sqlSessionFactory;
        this.dynamicMappedStatementHelper = new DynamicMappedStatementHelper(this);
    }

    public DynamicMappedStatementHelper getDynamicMappedStatementHelper() {
        return dynamicMappedStatementHelper;
    }

    public void setDynamicMappedStatementHelper(DynamicMappedStatementHelper dynamicMappedStatementHelper) {
        this.dynamicMappedStatementHelper = dynamicMappedStatementHelper;
    }

    @Override
    protected SqlSessionFactory getSqlSqlSessionFactory() {
        return this.sqlSessionFactory;
    }

    @Override
    public SqlSession getSqlSession() {
        return sqlSession;
    }

    @Override
    protected DynamicSqlSession getDynamicSqlSession() {
        return this;
    }

    public <T> T execute(String mappedStatementId) {
        return this.execute(sqlSession, mappedStatementId);
    }


    public <T> T execute(String mappedStatementId, Object parameter) {
        return this.execute(sqlSession, mappedStatementId, parameter);
    }

    public <T> T execute(DynamicMappedStatement dms, Object parameter) {
        return this.execute(sqlSession, dms, parameter);
    }

    public <T> T execute(String mappedStatementId, Object parameter, Object dataSourceKey) {
        return this.execute(sqlSession, mappedStatementId, parameter, dataSourceKey);
    }

    public <T> T execute(DynamicMappedStatement dms, Object parameter, Object dataSourceKey) {
        return this.execute(sqlSession, dms, parameter, dataSourceKey, false);
    }

    public <T> T doExecute(DynamicMappedStatement dms, Object parameter) {
        return this.doExecute(sqlSession, dms, parameter, false);
    }


    public <T> T execute(SqlSession sqlSession, String mappedStatementId) {
        return execute(sqlSession, mappedStatementId, null, null);
    }

    public <T> T execute(SqlSession sqlSession, String mappedStatementId, Object parameter) {
        mappedStatementId = mappedStatementId.replaceAll("/", ".");
        return execute(sqlSession, mappedStatementId, parameter, null);
    }

    public <T> T execute(SqlSession sqlSession, DynamicMappedStatement dms, Object parameter) {
        if (dms.isBatch() && parameter != null && parameter instanceof Collection) {
            return (T) executeBatch(dms, parameter);
        }
        return execute(sqlSession, dms, parameter, null, false);
    }

    /**
     * 　* @Description: 执行动态SQL
     * 　* @param mappedStatementId 动态SQL全名id
     *
     * @param parameter     参数
     * @param dataSourceKey 多数据源key 优先级 dataSourceKey > DynamicMappedStatement dataSource > system default dataSource
     */
    public <T> T execute(SqlSession sqlSession, String mappedStatementId, Object parameter, Object dataSourceKey) {
        DynamicMappedStatement dms = ((DynamicConfiguration) sqlSession.getConfiguration()).getDynamicMappedStatement(mappedStatementId);
        if (dms == null) {
            throw new MyBatisMappedStatementNotPresentException(mappedStatementId, "mappedStatementId[" + mappedStatementId + "] is not exist.");
        }
        return execute(sqlSession, dms, parameter, dataSourceKey, false);
    }

    public <T> T execute(SqlSession sqlSession, DynamicMappedStatement dms, Object parameter, Object dataSourceKey, boolean isBatchIterator) {
        if (dms == null) {
            throw new MyBatisMappedStatementNotPresentException("dynamicMappedStatement is null.");
        }
        //参数校验
        if (!isBatchIterator) {
            getDynamicMappedStatementHelper().getMixedMappedStatementFieldParser().validate(dms.getInputParameter(), dms.getCommandType(), parameter, parameter);
        }
        //执行前处理
        parameter = beforeHandle(dms, parameter);

        //线程绑定数据源
        Object targetDataSourceKey = getTargetDataSourceKey(dms, dataSourceKey);
        if (!Objects.isNull(targetDataSourceKey)) {
            DynamicDataSourceHolder.set(targetDataSourceKey);
        }
        //执行
        Object result;
        try {
            result = doExecute(sqlSession, dms, parameter, isBatchIterator);
        } finally {
            //线程解绑数据源
            if (!Objects.isNull(targetDataSourceKey)) {
                DynamicDataSourceHolder.clear();
            }
        }

        //结果集格式化
        if (!isBatchIterator) {
            result = getDynamicMappedStatementHelper().getMixedMappedStatementFieldParser().format(dms.getOutputParameter(), result, result);
            getDynamicMappedStatementHelper().getMixedMappedStatementFieldParser().validate(dms.getOutputParameter(), dms.getCommandType(), result, result);
        }

        //执行后拦截
        result = afterHandle(dms, parameter, result);
        return (T) result;
    }

    @Transactional
    public <T> T doExecute(SqlSession sqlSession, DynamicMappedStatement dms, Object parameter, boolean isBatchIterator) {
        CommandType commandType = dms.getCommandType();
        ReturnType returnType = dms.getReturnType();
        if (!isBatchIterator && parameter instanceof Map && StringUtils.isNotBlank(dms.getParameterWrapper())) {
            parameter = BeanUtils.getValueByPath((Map) parameter, dms.getParameterWrapper());
        }
        //使用全名执行
        String mappedStatementId = dms.getId();
        Object result;
        if (commandType == CommandType.StandardCommand.SELECT) {
            //分页
            Map parame = parameter instanceof Map ? (Map) parameter : BeanUtils.beanToMap(parameter);
            if ((parame != null && (ReturnType.StandardReturnType.PAGE.name().equals(Objects.toString(parame.get("returnType"))))) || returnType == ReturnType.StandardReturnType.PAGE) {
                DynamicPage page;
                if (parameter instanceof Map) {
                    page = new DynamicPage((Map) parameter);
                } else {
                    parameter = BeanUtils.beanToMap(parameter);
                    page = new DynamicPage((Map) parameter);
                }
                if (page == null) {
                    page = new DynamicPage();
                    PageHelper.startPage(page.getPageNumber(), page.getPageSize());
                } else if (page.getOffset() != null) {
                    PageHelper.offsetPage(page.getOffset(), page.getLimit());
                } else {
                    PageHelper.startPage(page.getPageNumber(), page.getPageSize());
                }
                List list = sqlSession.selectList(mappedStatementId, parameter);
                PageInfo pageList = new PageInfo(list);
                result = page.result(pageList.getList(), pageList.getTotal());
                //列表
            } else if ((parame != null && (ReturnType.StandardReturnType.COLLECT.name().equals(Objects.toString(parame.get("returnType"))))) || returnType == ReturnType.StandardReturnType.COLLECT) {
                result = sqlSession.selectList(mappedStatementId, parameter);
                //单条查询
            } else {
                result = sqlSession.selectOne(mappedStatementId, parameter);
            }
        } else if (commandType == CommandType.StandardCommand.INSERT) {

            if (dms.isBatch() && parameter instanceof Collection) {
                result = executeBatch(dms, parameter, dms.getBatchSize());
            } else {
                result = sqlSession.insert(mappedStatementId, parameter);
            }

        } else if (commandType == CommandType.StandardCommand.UPDATE) {

            if (dms.isBatch() && parameter instanceof Collection) {
                result = executeBatch(dms, parameter, dms.getBatchSize());
            } else {
                result = sqlSession.update(mappedStatementId, parameter);
            }

        } else if (commandType == CommandType.StandardCommand.DELETE) {
            result = sqlSession.delete(mappedStatementId, parameter);
        } else if (commandType == CommandType.StandardCommand.FLUSH) {
            result = sqlSession.flushStatements();
        } else if (commandType == CommandType.StandardCommand.SAVE) {

            if (dms.isBatch() && parameter instanceof Collection) {
                result = executeBatch(dms, parameter, dms.getBatchSize());
            } else {
                String primaryKeyPath = getDynamicMappedStatementHelper().getMixedMappedStatementFieldParser().getFirstPrimaryKeyPath(dms.getInputParameter().get(0));
                primaryKeyPath = primaryKeyPath.replace(dms.getParameterWrapper(), "");
                primaryKeyPath = primaryKeyPath.startsWith(".") ? primaryKeyPath.substring(1) : primaryKeyPath;
                Object keyValue = BeanUtils.getValueByPath((Map) parameter, primaryKeyPath);
                if (StringUtils.isBlank(keyValue)) {
                    result = sqlSession.insert(mappedStatementId, parameter);
                } else {
                    result = sqlSession.update(mappedStatementId, parameter);
                }
            }
        } else if (CollectionUtils.isNotEmpty(dms.getChildren())) {
            result = new HashMap<>();
            for (DynamicMappedStatement child : dms.getChildren()) {
                Object childResult = this.execute(child, parameter);
                BeanUtils.copy(childResult, result);
            }
        } else {
            result = this.executeAmbiguity(dms, parameter);
            //throw new MyBatisExecutorException("sqlCommandType or returnType is unkonw.");
        }
        return (T) resultHandle(dms, result);
    }


    /**
     * 判断数据库操作是否成功
     *
     * @param result 数据库操作返回影响条数
     */
    private static Object resultHandle(DynamicMappedStatement dms, Object result) {
        //新增、修改、删除 结果集转换为boolean
        if (result instanceof Number && dms.getReturnType() == ReturnType.StandardReturnType.BOOLEAN) {
            result = ((Number) result).intValue() >= 1;
        }
        if (StringUtils.isBlank(dms.getResultWrapper())) {
            // return result;
        } else {
            Map wrapperResult = new HashMap();
            Map next = wrapperResult;
            Map pre = null;
            String lastKey = null;
            String[] keys = dms.getResultWrapper().split("\\.");
            for (int i = 0; i < keys.length; i++) {
                String key = lastKey = keys[i];
                Map temp = new HashMap<>();
                next.put(key, temp);
                pre = next;
                next = temp;
            }
            if (dms.getReturnType() == ReturnType.StandardReturnType.PAGE || dms.getReturnType() == ReturnType.StandardReturnType.MAP) {
                next.putAll(BeanUtils.beanToMap(result));
            } else {
                pre.put(lastKey, result);
            }
            result = wrapperResult;
        }
        return result;
    }


    public List<Object> executeAmbiguity(DynamicMappedStatement dms, Object parameter) {
        Transaction tx = null;
        try {
            Configuration configuration = sqlSession.getConfiguration();
            Environment environment = configuration.getEnvironment();
            TransactionFactory transactionFactory;
            if (environment == null || environment.getTransactionFactory() == null) {
                transactionFactory = new ManagedTransactionFactory();
            } else {
                transactionFactory = environment.getTransactionFactory();
            }
            tx = transactionFactory.newTransaction(environment.getDataSource(), null, true);
            final Executor executor = configuration.newExecutor(tx, configuration.getDefaultExecutorType());
            MappedStatement ms = configuration.getMappedStatement(dms.getId());
            BoundSql boundSql = ms.getBoundSql(parameter);
            StatementHandler handler = configuration.newStatementHandler(executor, ms, parameter, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER, boundSql);

            Connection connection = tx.getConnection();
            if (ms.getStatementLog().isDebugEnabled()) {
                connection = ConnectionLogger.newInstance(connection, ms.getStatementLog(), 0);
            }
            Statement stmt = handler.prepare(connection, tx.getTimeout());
            handler.parameterize(stmt);
            return handler.query(stmt, Executor.NO_RESULT_HANDLER);
        } catch (Exception e) {
            if (tx != null) {
                try {
                    tx.close();
                } catch (SQLException ignore) {
                    // Intentionally ignore. Prefer previous error.
                }
            }
            logger.error("Error opening session.  Cause: " + e.getMessage(), e);
            throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
        } finally {
            ErrorContext.instance().reset();
        }
    }

}