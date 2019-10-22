package com.revature.views;

import java.math.BigDecimal;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.services.BankService;
import com.revature.util.ScannerUtil;


public class AccountMenu implements View {
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
		System.out.println("------------------------------------------------");
		System.out.println("|                 Account Menu                 |");
		System.out.println("|----------------------------------------------|");
		System.out.println("| 1. View Account Balance                      |");
		System.out.println("| 2. Deposit to Account                        |");
		System.out.println("| 3. Withdraw from Account                     |");
		System.out.println("| 4. Transfer to different Account             |");
		if (this.user.isPrivileged()) {
			System.out.println("| 5. Direct Deposit to Account                 |");
		}
		System.out.println("| 0. Exit                                      |");
		System.out.println("------------------------------------------------");

	}
	
	public void transferMenu() {
		System.out.println();
		System.out.println("------------------------------------------------");
		System.out.println("Please insert the Account ID you'd like to transfer to: ");
		int accountIdInput = ScannerUtil.getIntInput();
		
		System.out.println("Please insert the amount you would like to trasnfer: ");
		double amountInput = ScannerUtil.getInput(account.getAmount().doubleValue());

		System.out.println("------------------------------------------------");
		this.account = bs.makeTransfer(accountIdInput, this.account, BigDecimal.valueOf(amountInput));
		System.out.println("------------------------------------------------");
	}
	
	public void depositMenu() {
		System.out.println();
		System.out.println("------------------------------------------------");
		System.out.println("Please insert the amount you would like to deposit: ");
		double amountInput = ScannerUtil.getIntInput();
		System.out.println("------------------------------------------------");
		this.account = bs.makeDeposit(this.account, BigDecimal.valueOf(amountInput), this.user.getId());
		System.out.println("------------------------------------------------");
	}
	
	public void directDepositMenu() {
		System.out.println();
		System.out.println("------------------------------------------------");
		System.out.println("Please insert the amount you would like to deposit: ");
		double amountInput = ScannerUtil.getIntInput();
		System.out.println("------------------------------------------------");
		this.account = bs.makeDeposit(this.account, BigDecimal.valueOf(amountInput));
		System.out.println("------------------------------------------------");
	}
	
	public void withdrawMenu() {
		System.out.println();
		System.out.println("------------------------------------------------");
		System.out.println("Please insert the amount you would like to withdraw: ");
		double amountInput = ScannerUtil.getIntInput();
		System.out.println("------------------------------------------------");
		this.account = bs.makeWithdrawl(this.account, BigDecimal.valueOf(amountInput));
		System.out.println("------------------------------------------------");
	}
	
	@Override
	public View run() {
		printMenu();
		int user_input;
		
		if (this.user.isPrivileged()) {
			user_input = ScannerUtil.getInput(5);
		} else {
			user_input = ScannerUtil.getInput(4);
		}

		
		switch(user_input) {
		case 0:
			return new UserMenu(this.user);
		case 1:
			viewBalance();
			return new AccountMenu(this.user, this.account);
		case 2: 
			depositMenu();
			return new AccountMenu(this.user, this.account);	
		case 3: 
			withdrawMenu();
			return new AccountMenu(this.user, this.account);
		case 4: 
			transferMenu();	// allows user to insert account that doesn't exist and takes money away from own account.
			return new AccountMenu(this.user, this.account);
		case 5:
			directDepositMenu();
			return new AccountMenu(this.user, this.account);
		default: 
			return null;
		}
	}
	
	private void viewBalance() {
		System.out.println();
		System.out.println("------------------------------------------------");
		System.out.printf("| Account ID :  %28d   |%n", account.getId());
		System.out.printf("| Account Type: %28s   |%n", account.getAccountType());
		System.out.println("|----------------------------------------------|");
		System.out.printf("| Account Balance :  %14s (Rubles)   |%n", account.getAmount());
		System.out.println("------------------------------------------------");
	}

}
