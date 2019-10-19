package com.revature.models;

import java.math.BigDecimal;

public class Account {
	private int id;
	private BigDecimal amount;
	private String accountType;
	private boolean primaryAccount;
	
	
	// 		----------  TO STRING  ----------		
	@Override
	public String toString() {
		return "Account [id=" + id + ", amount=" + amount + ", accountType=" + accountType + ", primaryAccount="
				+ primaryAccount + "]";
	}

	// 		----------  GETTERS / SETTERS  ----------			
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public boolean isPrimaryAccount() {
		return primaryAccount;
	}

	public void setPrimaryAccount(boolean primaryAccount) {
		this.primaryAccount = primaryAccount;
	}

	// 		----------  HASHCODE / EQUALS  ----------		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + id;
		result = prime * result + (primaryAccount ? 1231 : 1237);
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
		Account other = (Account) obj;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (id != other.id)
			return false;
		if (primaryAccount != other.primaryAccount)
			return false;
		return true;
	}

	// 		----------  CONSTRUCTORS  ----------
//	public Account(int id, String accountType, boolean primaryAccount) {	public Account(int id, String accountType, boolean primaryAccount) {
//		super();
//		this.id = id;
//		this.accountType = accountType;
//		this.primaryAccount = primaryAccount;
//	}

	public Account(int id, BigDecimal amount, String accountType, boolean primaryAccount) {
		super();
		this.id = id;
		this.amount = amount;
		this.accountType = accountType;
		this.primaryAccount = primaryAccount;
	}

	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
