package com.yangxy.gpjava.quartz.entity;

import com.yangxy.gpjava.log.entity.LogEntity;
import com.yangxy.gpjava.user.entity.SlmUser;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "SLM_QUARTZ")
public class QuartzEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "QUARTZ_NAME", nullable = false, length = 200)
	private String quartzName;

	@Column(name = "QUARTZ_CRON", nullable = false, length = 50)
	private String quartzCron;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CREATE_USER", nullable = false)
	private SlmUser createUser;

	@Column(name = "QUARTZ_STATUS", nullable = false, length = 20)
	private String quartzStatus;

	@Column(name = "LAST_RUNTIME")
	private LocalDate lastRuntime;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "LOG_ID", nullable = false)
	private LogEntity log;

	public LogEntity getLog() {
		return log;
	}

	public void setLog(LogEntity log) {
		this.log = log;
	}

	public LocalDate getLastRuntime() {
		return lastRuntime;
	}

	public void setLastRuntime(LocalDate lastRuntime) {
		this.lastRuntime = lastRuntime;
	}

	public String getQuartzStatus() {
		return quartzStatus;
	}

	public void setQuartzStatus(String quartzStatus) {
		this.quartzStatus = quartzStatus;
	}

	public SlmUser getCreateUser() {
		return createUser;
	}

	public void setCreateUser(SlmUser createUser) {
		this.createUser = createUser;
	}

	public String getQuartzCron() {
		return quartzCron;
	}

	public void setQuartzCron(String quartzCron) {
		this.quartzCron = quartzCron;
	}

	public String getQuartzName() {
		return quartzName;
	}

	public void setQuartzName(String quartzName) {
		this.quartzName = quartzName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}