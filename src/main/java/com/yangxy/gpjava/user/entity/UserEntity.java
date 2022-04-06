package com.yangxy.gpjava.user.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity(name = "com.yangxy.gpjava.user.entity.UserEntity")
@Table(name = "SLM_USER")
public class UserEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 14894864L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "USER_NAME", nullable = false)
	private String userName;

	@Column(name = "USER_PHONE", nullable = false)
	private String userPhone;

	@Column(name = "USER_ROLE", nullable = false)
	private String userRole;

	@Column(name = "USER_PWD", nullable = false)
	private String userPwd;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	@Override
	public String toString() {
		return "UserEntity{" +
				"id=" + id +
				", userName='" + userName + '\'' +
				", userPhone='" + userPhone + '\'' +
				", userRole='" + userRole + '\'' +
				", userPwd='" + userPwd + '\'' +
				'}';
	}
}