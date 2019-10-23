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
		System.out.println();
		System.out.println("------------------------------------------------");
		System.out.println("|             Personal Account Menu            |");
		System.out.println("|----------------------------------------------|");
		System.out.println("| 1. View Account Details                      |");
		System.out.println("| 2. View Banking Accounts                     |");
		System.out.println("| 3. Create Account                            |");
		System.out.println("| 0. Exit                                      |");
		System.out.println("------------------------------------------------");
	}
	
	public void printAccDetails() {
		System.out.println();
		System.out.println("------------------------------------------------");
		System.out.println("|           Personal Account Details           |");
		System.out.println("|----------------------------------------------|");
		System.out.printf("| Account ID        : %24d |%n", user.getId());
		System.out.printf("| Account Full Name : %24s |%n", user.getFullName());
		System.out.printf("| Account Start Date: %24s |%n", user.getStartDate());
		System.out.println("------------------------------------------------");
	}

	@Override
	public View run() {
		printMenu();
		
		int user_input = ScannerUtil.getInput(3);

		switch(user_input) {
		case 0: 
			return new MainMenu();
		case 1: 
			printAccDetails();
			return new UserMenu(this.user);
		case 2: 
			viewBankAccounts();
			return new AccountMenu(this.user, this.account);
		case 3: 
			createAccount();
			return new AccountMenu(this.user, this.account);
		default: 
			return null;
		}	
	}
	
	private void viewBankAccounts() {
		List<Account> accounts = userDao.getBankAccounts(user.getId());
		
		System.out.println("------------------------------------------------");
		System.out.println("| Account ID | Account Type | Primary Account  |");
		System.out.println("|----------------------------------------------|");
		for (Account account : accounts) {
			System.out.printf("| %10d | %-12s | %-16B |%n", 
							  account.getId(), account.getAccountType(), account.isPrimaryAccount());
		}
		System.out.println("------------------------------------------------");
		System.out.println("Select which account you'd like to access.");
		
		int accSelect = ScannerUtil.getInput((accounts.size() - 1));
		
		this.account = accounts.get((accSelect));
	}
	
	private void createAccount() {
		System.out.println();
		System.out.println("------------------------------------------------");
		System.out.println("Please Enter the type of account you'd like to make : ");
		System.out.println("0. Savings");
		System.out.println("1. Checking");
		System.out.println("------------------------------------------------");
		int accTypeSelect = ScannerUtil.getInput(1);
		String accType = new String();
		
		if (accTypeSelect == 0) {
			accType = "Savings";
		} else {
			accType = "Checking";
		}
		
		this.account = userDao.createAccount(user.getId(), accType);
	}

}
