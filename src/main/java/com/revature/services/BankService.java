package com.revature.services;

import java.math.BigDecimal;

import com.revature.dao.UserDao;
import com.revature.models.Account;

public class BankService {
	private UserDao userDao = new UserDao();
	
	public Account makeTransfer(int idIn, Account accountOut, BigDecimal amount) {
		if (userDao.makeTransfer(idIn, accountOut.getId(), amount)) {
			System.out.printf("Your transaction of %s rubles has been successfully sent to %d.%n", amount, idIn);
			accountOut.setAmount(accountOut.getAmount().subtract(amount));
		}
		else {
			System.out.println("An error has prevented this transaction from taking place. Please review input information.");
		}
		
		return accountOut;
	}
	
	public Account makeDeposit(Account account, BigDecimal amount) {
		account.setAmount(account.getAmount().add(amount));
		
		return account;
	}
	
	public Account makeWithdrawl(Account account, BigDecimal amount) {
		account.setAmount(account.getAmount().subtract(amount));
		
		return account;
	}
}
