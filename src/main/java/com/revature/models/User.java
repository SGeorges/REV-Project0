package com.revature.models;

import java.time.LocalDate;

public class User {

	private int id;
	private String fullName;
	private String password;
	private LocalDate startDate;
	private boolean privileged;
	
	// 		----------  TO STRING  ----------		
	@Override
	public String toString() {
		return "User [id=" + id + ", fullName=" + fullName + ", password=" + password + ", privileged=" + privileged
				+ "]";
	}
	
	// 		----------  GETTERS / SETTERS  ----------		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public boolean isPrivileged() {
		return privileged;
	}

	public void setPrivileged(boolean privileged) {
		this.privileged = privileged;
	}

	// 		----------  HASHCODE / EQUALS  ----------		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + id;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + (privileged ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (id != other.id)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (privileged != other.privileged)
			return false;
		return true;
	}

	// 		----------  CONSTRUCTOR (FULL_NAME, PASSWORD)  ----------		
	public User(String fullName, String password) {
		super();
		this.fullName = fullName;
		this.password = password;
	}
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
}
