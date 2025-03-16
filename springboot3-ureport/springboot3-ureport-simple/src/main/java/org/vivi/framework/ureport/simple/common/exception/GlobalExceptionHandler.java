package org.vivi.framework.ureport.simple.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.vivi.framework.ureport.simple.common.bean.Result;
import org.vivi.framework.ureport.simple.ureport.exception.CellComputeException;
import org.vivi.framework.ureport.simple.ureport.exception.CellDependencyException;
import org.vivi.framework.ureport.simple.ureport.exception.CellNotExistException;
import org.vivi.framework.ureport.simple.ureport.exception.ConvertException;
import org.vivi.framework.ureport.simple.ureport.exception.DatasetUndefinitionException;
import org.vivi.framework.ureport.simple.ureport.exception.ExpressionParserException;
import org.vivi.framework.ureport.simple.ureport.exception.IllegalCellExpandException;
import org.vivi.framework.ureport.simple.ureport.exception.IndependenceException;
import org.vivi.framework.ureport.simple.ureport.exception.ReportComputeException;
import org.vivi.framework.ureport.simple.ureport.exception.ReportException;
import org.vivi.framework.ureport.simple.ureport.exception.ReportPagingException;
import org.vivi.framework.ureport.simple.ureport.exception.ReportParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(CellComputeException.class)
    public Result handleCellComputeException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage(e.getMessage());
		logger.error(e.getMessage(), e);
        return reuslt;
    }
	
	@ExceptionHandler(CellDependencyException.class)
    public Result handleCellDependencyException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage(e.getMessage());
		logger.error(e.getMessage(), e);
        return reuslt;
    }
	
	@ExceptionHandler(CellNotExistException.class)
    public Result handleCellNotExistException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage(e.getMessage());
		logger.error(e.getMessage(), e);
        return reuslt;
    }
	
	@ExceptionHandler(ConvertException.class)
    public Result handleConvertException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage(e.getMessage());
		logger.error(e.getMessage(), e);
        return reuslt;
    }
	
	@ExceptionHandler(DatasetUndefinitionException.class)
    public Result handleDatasetUndefinitionException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage(e.getMessage());
		logger.error(e.getMessage(), e);
        return reuslt;
    }
	
	@ExceptionHandler(ExpressionParserException.class)
    public Result handleExpressionParserException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage(e.getMessage());
		logger.error(e.getMessage(), e);
        return reuslt;
    }
	
	@ExceptionHandler(IllegalCellExpandException.class)
    public Result handleIllegalCellExpandException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage(e.getMessage());
		logger.error(e.getMessage(), e);
        return reuslt;
    }
	
	@ExceptionHandler(IndependenceException.class)
    public Result handleIndependenceException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage(e.getMessage());
		logger.error(e.getMessage(), e);
        return reuslt;
    }
	
	@ExceptionHandler(ReportComputeException.class)
    public Result handleReportComputeException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage(e.getMessage());
		logger.error(e.getMessage(), e);
        return reuslt;
    }
	
	@ExceptionHandler(ReportException.class)
    public Result handleReportException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage(e.getMessage());
		logger.error(e.getMessage(), e);
        return reuslt;
    }
	
	@ExceptionHandler(ReportPagingException.class)
    public Result handleReportPagingException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage(e.getMessage());
		logger.error(e.getMessage(), e);
        return reuslt;
    }
	
	@ExceptionHandler(ReportParseException.class)
    public Result handleReportParseException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage(e.getMessage());
		logger.error(e.getMessage(), e);
        return reuslt;
    }
	
	@ExceptionHandler(ParamErrorException.class)
    public Result handleParamErrorException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage(e.getMessage());
		//logger.error(e.getMessage(), e);
        return reuslt;
    }
	
	@ExceptionHandler(SQLSyntaxErrorException.class)
    public Result handleSQLSyntaxErrorException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage(e.getMessage());
		logger.error(e.getMessage(), e);
        return reuslt;
    }
	
	@ExceptionHandler(Exception.class)
    public Result customException(Exception e) {
		Result reuslt = new Result();
		reuslt.setCode(500);
		reuslt.setMessage("系统内部出错");
		logger.error(e.getMessage(), e);
        return reuslt;
    }
}
