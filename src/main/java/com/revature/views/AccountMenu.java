package com.revature.views;

import java.math.BigDecimal;

import com.revature.dao.UserDao;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.services.BankService;
import com.revature.util.ScannerUtil;

public class AccountMenu implements View {
	private UserDao userDao = new UserDao();
	private BankService bs = new BankService();
	
	private User user = new User();
	private Account account = new Account();
	
	
	public AccountMenu(User user, Account account) {
		super();
		this.user = user;
		this.account = account;
	}


	public void printMenu() {
		System.out.println();
		System.out.println("---------- Account Menu ----------");
		System.out.println("1. View Account Balance");
		System.out.println("2. Deposit to Account");
		System.out.println("3. Withdraw from Account");
		System.out.println("4. Transfer to different Account");
		System.out.println("0. Exit");
	}
	
	public void transferMenu() {
		System.out.println("----------------------------------");
		System.out.println("Please insert the Account ID you'd like to transfer to: ");
		int accountIdInput = ScannerUtil.getIntInput();
		
		System.out.println("Please insert the amount you would like to trasnfer: ");
		double amountInput = ScannerUtil.getInput(account.getAmount().doubleValue());

		this.account = bs.makeTransfer(accountIdInput, this.account, BigDecimal.valueOf(amountInput));
	}
	
	@Override
	public View run() {
		printMenu();
		
		int user_input = ScannerUtil.getInput(4);
		
		switch(user_input) {
		case 0:
			return new UserMenu(this.user);
		case 1:
			return null;
		case 2: 
			return null;
		case 3: 
			return null;
		case 4: 
			transferMenu();
			return new AccountMenu(this.user, this.account);
		default: 
			return null;
		}
	}

}
