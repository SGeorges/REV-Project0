package com.revature.models;

public class Holder {
	private int personalID;
	private String fullName;
	private boolean primary;
	
	// 		----------  TO STRING  ----------	
	@Override
	public String toString() {
		return "Holder [personalID=" + personalID + ", fullName=" + fullName + ", primary=" + primary + "]";
	}

	// 		----------  GETTERS / SETTERS  ----------	
	public int getPersonalID() {
		return personalID;
	}

	public void setPersonalID(int personalID) {
		this.personalID = personalID;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	// 		----------  HASHCODE / EQUALS  ----------
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + personalID;
		result = prime * result + (primary ? 1231 : 1237);
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
		Holder other = (Holder) obj;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (personalID != other.personalID)
			return false;
		if (primary != other.primary)
			return false;
		return true;
	}

	// 		----------  CONSTRUCTORS  ----------
	public Holder(int personalID, String fullName, boolean primary) {
		super();
		this.personalID = personalID;
		this.fullName = fullName;
		this.primary = primary;
	}

	public Holder() {
		super();
		// TODO Auto-generated constructor stub
	}
	


}
