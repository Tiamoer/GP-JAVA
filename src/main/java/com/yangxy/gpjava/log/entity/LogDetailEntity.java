package com.yangxy.gpjava.log.entity;

import com.yangxy.gpjava.system.entity.SystemEntity;
import com.yangxy.gpjava.user.entity.SlmUser;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "SLM_LOG_DETAIL")
public class LogDetailEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1486534453L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "LOG_ID", nullable = false)
	private LogEntity log;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "SYSTEM_ID", nullable = false)
	private SystemEntity system;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "USER_ID", nullable = false)
	private SlmUser user;

	@Column(name = "CREATE_TIME", nullable = false)
	private LocalDate createTime;

	@Lob
	@Column(name = "INFO")
	private String info;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LogEntity getLog() {
		return log;
	}

	public void setLog(LogEntity log) {
		this.log = log;
	}

	public SystemEntity getSystem() {
		return system;
	}

	public void setSystem(SystemEntity system) {
		this.system = system;
	}

	public SlmUser getUser() {
		return user;
	}

	public void setUser(SlmUser user) {
		this.user = user;
	}

	public LocalDate getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDate createTime) {
		this.createTime = createTime;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}