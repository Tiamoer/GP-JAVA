package com.yangxy.gpjava.log.entity;

import com.yangxy.gpjava.system.entity.SystemEntity;
import com.yangxy.gpjava.user.entity.SlmUser;

import javax.persistence.*;

@Entity
@Table(name = "SLM_LOG")
public class LogEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "LOG_NAME", nullable = false, length = 200)
	private String logName;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CREATE_USER", nullable = false)
	private SlmUser createUser;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "LOG_SYSTEM", nullable = false)
	private SystemEntity logSystem;

	@Column(name = "LOG_PATH", nullable = false, length = 200)
	private String logPath;

	@Column(name = "LOG_RATE", nullable = false, length = 50)
	private String logRate;

	public String getLogRate() {
		return logRate;
	}

	public void setLogRate(String logRate) {
		this.logRate = logRate;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public SystemEntity getLogSystem() {
		return logSystem;
	}

	public void setLogSystem(SystemEntity logSystem) {
		this.logSystem = logSystem;
	}

	public SlmUser getCreateUser() {
		return createUser;
	}

	public void setCreateUser(SlmUser createUser) {
		this.createUser = createUser;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}