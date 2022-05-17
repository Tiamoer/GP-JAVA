package com.yangxy.gpjava.user.dao;

import com.yangxy.gpjava.user.entity.SlmUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<SlmUser, Long> {

	@Query(value = "SELECT t FROM com.yangxy.gpjava.user.entity.SlmUser t WHERE t.userName = :username")
	SlmUser getUserByName(@Param("username") String username);

	@Query(value = "SELECT t.userRole FROM com.yangxy.gpjava.user.entity.SlmUser t WHERE t.userName = :username")
	String getUserRoleByUsername(@Param("username") String username);

	@Query(value = "SELECT t FROM com.yangxy.gpjava.user.entity.SlmUser t WHERE t.userPhone = :phone")
	SlmUser getUserByPhone(@Param("phone") String phone);
}