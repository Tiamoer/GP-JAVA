package com.yangxy.gpjava.system.entity;

import com.yangxy.gpjava.user.entity.SlmUser;

import javax.persistence.*;

@Entity
@Table(name = "SLM_SYSTEM")
public class SystemEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "SYSTEM_NAME", nullable = false, length = 200)
	private String systemName;

	@Column(name = "SYSTEM_HOST", nullable = false, length = 50)
	private String systemHost;

	@Column(name = "SYSTEM_PORT", nullable = false)
	private Long systemPort;

	@Column(name = "SYSTEM_USERNAME", nullable = false, length = 200)
	private String systemUsername;

	@Column(name = "SYSTEM_PASSWORD", nullable = false, length = 200)
	private String systemPassword;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CREATE_USER", nullable = false)
	private SlmUser createUser;

	public SlmUser getCreateUser() {
		return createUser;
	}

	public void setCreateUser(SlmUser createUser) {
		this.createUser = createUser;
	}

	public String getSystemPassword() {
		return systemPassword;
	}

	public void setSystemPassword(String systemPassword) {
		this.systemPassword = systemPassword;
	}

	public String getSystemUsername() {
		return systemUsername;
	}

	public void setSystemUsername(String systemUsername) {
		this.systemUsername = systemUsername;
	}

	public Long getSystemPort() {
		return systemPort;
	}

	public void setSystemPort(Long systemPort) {
		this.systemPort = systemPort;
	}

	public String getSystemHost() {
		return systemHost;
	}

	public void setSystemHost(String systemHost) {
		this.systemHost = systemHost;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}