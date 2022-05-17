package com.yangxy.gpjava.system.dao;

import com.yangxy.gpjava.system.entity.SystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统模块Dao层
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/5/13
 */

@Repository
public interface SystemDao extends JpaRepository<SystemEntity, Long> {

	@Query(value = "SELECT t.* FROM SLM_SYSTEM t WHERE t.SYSTEM_HOST = :host AND t.SYSTEM_PORT = :port AND t.SYSTEM_USERNAME = :userName AND t.CREATE_USER = :userId", nativeQuery = true)
	List<SystemEntity> findByHost(@Param("host") String host, @Param("port") long port, @Param("userName") String userName, @Param("userId") long userId);

	@Query(value = "SELECT t.ID, t.SYSTEM_NAME, t.SYSTEM_HOST, t.SYSTEM_PORT, t.SYSTEM_USERNAME FROM SLM_SYSTEM t WHERE t.CREATE_USER = :userId", nativeQuery = true)
	List<Map<String, Object>> findAllByCreateUser(@Param("userId") long userId);

	@Query(value = "SELECT t.* FROM SLM_SYSTEM t WHERE t.ID = :sysId", nativeQuery = true)
	List<Map<String, Object>> findOneById(@Param("sysId") long sysId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE SLM_SYSTEM t SET SYSTEM_NAME = :sysName, SYSTEM_HOST = :sysHost, SYSTEM_PORT = :sysPort, SYSTEM_USERNAME = :sysUsername, SYSTEM_PASSWORD = :sysPassword WHERE ID = :sysId", nativeQuery = true)
	int updateSystem(@Param("sysName") String name, @Param("sysHost") String host, @Param("sysPort") long port, @Param("sysUsername") String username, @Param("sysPassword") String password, @Param("sysId") long sysId);
}
