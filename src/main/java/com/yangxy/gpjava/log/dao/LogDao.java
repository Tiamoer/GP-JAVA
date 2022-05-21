package com.yangxy.gpjava.log.dao;

import com.yangxy.gpjava.log.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/5/18
 */

public interface LogDao extends JpaRepository<LogEntity, Long> {

	@Query(value = "SELECT t.ID, t.LOG_NAME, t.LOG_SYSTEM, t.LOG_PATH FROM SLM_LOG t WHERE t.CREATE_USER = :userId", nativeQuery = true)
	List<Map<String, Object>> findAllByCreateUser(@Param("userId") Long userId);

}
