package com.yangxy.gpjava.log.dao;

import com.yangxy.gpjava.log.entity.LogDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 *
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/5/20
 */

public interface LogDetailDao extends JpaRepository<LogDetailEntity, Long> {

	@Query(value = "SELECT t.* FROM SLM_LOG_DETAIL t WHERE t.LOG_ID = :logId", nativeQuery = true)
	List<LogDetailEntity> findByLogId(@Param("logId") Long logId);

}
