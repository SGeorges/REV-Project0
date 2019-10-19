package com.revature.views;

import java.util.List;

import com.revature.dao.UserDao;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.util.ScannerUtil;

public class UserMenu implements View{
	
	private UserDao userDao = new UserDao();
	private User user = new User();
	private Account account = new Account();
	
	// Constructor to pass on the current user information to the 
	// UserMenu.
	public UserMenu(User user) {
		this.user = user;
	}

	public void printMenu() {
		System.out.println("---------- Personal Account Menu ----------");
		System.out.println("1. View Account Details");
		System.out.println("2. View Banking Accounts");
		System.out.println("3. Change Full Name");
		System.out.println("4. Change Password");
		System.out.println("0. Exit");
	}
	
	public void printAccDetails() {
		System.out.println("---------- Personal Account Details ----------");
		System.out.println("Account ID        : " + user.getId());
		System.out.println("Account Full Name : " + user.getFullName());
		System.out.println("Account Start Date: " + user.getStartDate());
		System.out.println("----------------------------------------------");
	}

	@Override
	public View run() {
		printMenu();
		
		int user_input = ScannerUtil.getInput(4);

		switch(user_input) {
		case 0: 
			return new MainMenu();
		case 1: 
			printAccDetails();
			return new UserMenu(user);
		case 2: 
			viewBankAccounts();
			System.out.println(account.toString());
			return null;
		case 3: 
			return null;
		case 4: 
			return null;
		default: 
			return null;
		}	
	}
	
	private void viewBankAccounts() {
		List<Account> accounts = userDao.getBankAccounts(user.getId());
		
		System.out.println("-----------------------------------------------");
		System.out.println("| Account ID | Account Type | Primary Account |");
		for (Account account : accounts) {
			System.out.printf("| %10d | %-12s | %-15B |%n", 
							  account.getId(), account.getAccountType(), account.isPrimaryAccount());
		}
		System.out.println("-----------------------------------------------");
		System.out.println("Select which account you'd like to access.");
		
		int accSelect = ScannerUtil.getInput(accounts.size());
		
		this.account = accounts.get(accSelect);
	}

}
