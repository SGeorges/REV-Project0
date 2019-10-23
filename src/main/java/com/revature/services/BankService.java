package com.revature.services;

import java.math.BigDecimal;

import com.revature.dao.UserDao;
import com.revature.models.Account;

public class BankService {
	private UserDao userDao = new UserDao();
	
	public Account makeTransfer(int accountIn, Account accountOut, BigDecimal amount) {
		if ( accountIn == accountOut.getId() ) {
			System.out.println("You've requested to transfer money to the same account. Please review your input.");
			return accountOut;
		} else {
			if (userDao.makeTransfer(accountIn, accountOut.getId(), amount)) {
				System.out.printf("Your transaction of %s rubles has been successfully sent to %d.%n", amount, accountIn);
				accountOut.setAmount(accountOut.getAmount().subtract(amount));
			}
			else {
				System.out.println("An error has prevented this transaction from taking place. Please review input information.");
			}	
			return accountOut;
		}
	}
	
	public Account makeDeposit(Account account, BigDecimal amount, int userID) {
		BigDecimal increase = userDao.distributeWealth(amount, account.getId(), userID);
		account.setAmount(account.getAmount().add(increase));
	
		System.out.printf("Your deposit of %s rubles has been distributed to the people.%n" , amount);
		System.out.println("Long live the motherland!");

		return account;
	}
	
	public Account makeDeposit(Account account, BigDecimal amount) {
		account.setAmount(account.getAmount().add(amount));
		userDao.makeUpdate(account.getId(), amount, "+");
	
		System.out.printf("Your deposit of %s rubles has been made to %d.%n" , amount, account.getId());

		return account;
	}
	
	public Account makeWithdrawl(Account account, BigDecimal amount) {
		account.setAmount(account.getAmount().subtract(amount));
		userDao.makeUpdate(account.getId(), amount, "-");
		
		System.out.printf("Your withdrawl of %s rubles has been made to %d.%n" , amount, account.getId());
		
		return account;
	}
}
