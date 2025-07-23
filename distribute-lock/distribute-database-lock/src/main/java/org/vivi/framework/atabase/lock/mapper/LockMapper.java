package org.vivi.framework.atabase.lock.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.vivi.framework.atabase.lock.model.TaskLock;

@Mapper
public interface LockMapper {

    /**
     * 根据任务ID获取锁记录（带悲观锁）
     */
    @Select("SELECT * FROM task_lock WHERE lock_key = #{lockKey} FOR UPDATE")
    TaskLock selectForUpdate(long lockKey);

    /**
     * 插入锁记录
     */
    @Insert("INSERT INTO task_lock(lock_key, owner_node, expire_time, version,update_time) " +
            "VALUES(#{lockKey}, #{ownerNode}, #{expireTime}, 1,NOW())")
    int insert(TaskLock lock);

    /**
     * 乐观锁更新（根据版本号）
     */
    @Update("UPDATE task_lock SET " +
            "owner_node = #{ownerNode}, " +
            "expire_time = #{expireTime}, " +
            "version = version + 1, " +
            "update_time = NOW() " +
            "WHERE lock_key = #{lockKey} AND version = #{version}")
    int updateWithVersion(TaskLock lock);

    /**
     * 释放锁（将owner和expire_time置空）
     */
    @Update("UPDATE task_lock SET " +
            "owner_node = NULL, " +
            "expire_time = NULL, " +
            "version = version + 1, " +
            "update_time = NOW() " +
            "WHERE lock_key = #{lockKey} AND owner_node = #{ownerNode}")
    int releaseLock(String lockKey, String ownerNode);
}
