package org.vivi.framework.iasync.thread.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.vivi.framework.iasync.thread.entity.ExportTask;

import java.util.Date;

/**
 * 导出任务Mapper
 */
@Mapper
public interface ExportTaskMapper extends BaseMapper<ExportTask> {
    @Insert("insert into export_task(MODULE, FILE_NAME, FILE_PATH, IDENTITY, STATUS, TYPE, CREATE_TIME, USER_NAME, UPDATE_TIME ) " +
            "values(#{module}, #{fileName}, #{filePath}, #{identity}, 0, 1, #{startTime}, #{userName}, #{startTime})")
    @Options(useGeneratedKeys = true, keyColumn = "ID", keyProperty = "id")
    long insertTaskInfo(@Param("module") String module,
                        @Param("fileName") String fileName, 
                        @Param("filePath") String filePath, 
                        @Param("filePath") String identity, 
                        @Param("userName") String userName, 
                        @Param("startTime") Date startTime
    );

    @Update("update export_task set UPDATE_TIME= NOW(), STATUS = #{status}, REASON = #{failReason}  where STATUS = 0 and ID = #{identity}")
    long updateTaskInfo(@Param("status") int status,
                        @Param("identity") long identity,
                        @Param("reason") String reason
    );

    @Update("UPDATE export_task SET STATUS = 3 WHERE ID = #{id}")
    long deleteByTaskId(@Param("id") String id);
}