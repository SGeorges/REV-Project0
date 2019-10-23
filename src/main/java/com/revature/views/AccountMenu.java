package com.revature.views;

import java.math.BigDecimal;
import java.util.List;

import com.revature.dao.UserDao;
import com.revature.models.Account;
import com.revature.models.Holder;
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


	private void printMenu() {
		int index = 1;
		
		System.out.println();
		System.out.println("------------------------------------------------");
		System.out.println("|                 Account Menu                 |");
		System.out.println("|----------------------------------------------|");
		System.out.printf("| %d. View Account Balance                      |%n", index++);
		System.out.printf("| %d. Deposit to Account                        |%n", index++);
		if (this.account.isPrimaryAccount()) {
			System.out.printf("| %d. Withdraw from Account                     |%n", index++);
			System.out.printf("| %d. Transfer to different Account             |%n", index++);
			System.out.printf("| %d. View Account Holders                      |%n", index++);
			System.out.printf("| %d. Add Account Holder                        |%n", index++);
			System.out.printf("| %d. Revoke Account Holder                     |%n", index++);
			System.out.printf("| %d. Close Account                             |%n", index++);
		}
		if (this.user.isPrivileged()) {
			System.out.printf("| %d. Direct Deposit to Account                 |%n", index++);
		}
		System.out.println("| 0. Exit                                      |");
		System.out.println("------------------------------------------------");

	}
	
	private void transferMenu() {
		System.out.println();
		System.out.println("------------------------------------------------");
		System.out.println("Please insert the Account ID you'd like to transfer to: ");
		int accountIdInput = ScannerUtil.getIntInput();
		
		System.out.println("Please insert the amount you would like to trasnfer: ");
		double amountInput = ScannerUtil.getInput(this.account.getAmount().doubleValue());

		System.out.println("------------------------------------------------");
		this.account = bs.makeTransfer(accountIdInput, this.account, BigDecimal.valueOf(amountInput));
		System.out.println("------------------------------------------------");
	}
	
	private void depositMenu() {
		System.out.println();
		System.out.println("------------------------------------------------");
		System.out.println("Please insert the amount you would like to deposit: ");
		double amountInput = ScannerUtil.getIntInput();
		System.out.println("------------------------------------------------");
		this.account = bs.makeDeposit(this.account, BigDecimal.valueOf(amountInput), this.user.getId());
		System.out.println("------------------------------------------------");
	}
	
	private void directDepositMenu() {
		System.out.println();
		System.out.println("------------------------------------------------");
		System.out.println("Please insert the amount you would like to deposit: ");
		double amountInput = ScannerUtil.getIntInput();
		System.out.println("------------------------------------------------");
		this.account = bs.makeDeposit(this.account, BigDecimal.valueOf(amountInput));
		System.out.println("------------------------------------------------");
	}
	
	private void withdrawMenu() {
		System.out.println();
		System.out.println("------------------------------------------------");
		System.out.println("Please insert the amount you would like to withdraw: ");
		double amountInput = ScannerUtil.getInput(this.account.getAmount().doubleValue());
		System.out.println("------------------------------------------------");
		this.account = bs.makeWithdrawl(this.account, BigDecimal.valueOf(amountInput));
		System.out.println("------------------------------------------------");
	}
	
	private void viewAccountHolders() {
		List<Holder> holders = userDao.viewAccountHolders(this.account.getId());
		
		System.out.println();
		System.out.println("------------------------------------------------");
		System.out.println("|  ID  |      Full Name      | Primary Account |");
		System.out.println("|----------------------------------------------|");
		for (Holder holder : holders) {
			System.out.printf("| %4d | %-19s | %-15B |%n", 
							  holder.getPersonalID(), holder.getFullName(), holder.isPrimary());
		}
		System.out.println("------------------------------------------------");
	}
	
	private void addAccountHolder() {
		System.out.println();
		System.out.println("Please enter the Personal ID you'd like to add: ");
		int holderID = ScannerUtil.getIntInput();
		
		List<Holder> holders = userDao.viewAccountHolders(this.account.getId());
		boolean exists = false;
		
		for (Holder holder : holders) {
			if (holderID == holder.getPersonalID()) {
				exists = true;
			}
		}

		if (exists) {
			System.out.println("This account is already an Account Holder.");
		} else {
			userDao.addAccountHolder(this.account.getId(), holderID);
		}
		
	}
	
	private void revokePrimaryHolder() {
		viewAccountHolders();
		
		System.out.println("Insert ID of which holder to remove:");
		int holderID = ScannerUtil.getIntInput();
		
		if (holderID == this.user.getId()) {
			System.out.println("A user cannot remove themself as a holder, must close account instead.");
		} else {
			userDao.revokePrimaryHolder(this.account.getId(), holderID);
		}
	}
	
	private boolean closeAccount() {
		System.out.println("Would you like to close this account?");
		System.out.println("0. YES");
		System.out.println("1. NO");
		
		int userChoice = ScannerUtil.getInput(1);
		
		if ( userChoice == 0 ) {
			userDao.closeAccount(this.account.getId());
			System.out.printf("All access to the account %d has been removed", this.account.getId());
			return true;
		}else {
			return true;
		}
	}
	
	@Override
	public View run() {
		printMenu();
		int userInput;
		
		if ((this.user.isPrivileged()) && (this.account.isPrimaryAccount())) {
			userInput = ScannerUtil.getInput(9);
		}
		else if ((!this.user.isPrivileged()) && (this.account.isPrimaryAccount())) {
			userInput = ScannerUtil.getInput(8);
		}
		else if (this.user.isPrivileged()) {
			userInput = ScannerUtil.getInput(3);
		} else {
			userInput = ScannerUtil.getInput(2);
		}

		
		switch(userInput) {
		case 0:
			return new UserMenu(this.user);
		case 1:
			viewBalance();
			return new AccountMenu(this.user, this.account);
		case 2: 
			depositMenu();
			return new AccountMenu(this.user, this.account);	
		case 3: 
			if (this.account.isPrimaryAccount()) {
				withdrawMenu();
				return new AccountMenu(this.user, this.account);
			} else if (this.user.isPrivileged()) {
				directDepositMenu();
				return new AccountMenu(this.user, this.account);
			} else return null;
		case 4: 
			transferMenu();	// allows user to insert account that doesn't exist and takes money away from own account.
			return new AccountMenu(this.user, this.account);
		case 5:
			viewAccountHolders();
			return new AccountMenu(this.user,this.account);
		case 6: 
			addAccountHolder();
			return new AccountMenu(this.user,this.account);
		case 7: 
			revokePrimaryHolder();
			return new AccountMenu(this.user,this.account);
		case 8: 
			if (closeAccount()) {
				return new UserMenu(this.user);
			} else {
				return new AccountMenu(this.user,this.account);
			}
		case 9:
			if (this.user.isPrivileged()) {
				directDepositMenu();
				return new AccountMenu(this.user,this.account);
			}
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
