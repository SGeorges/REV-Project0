package com.revature.views;

import com.revature.dao.UserDao;
import com.revature.models.User;
import com.revature.services.BankService;
import com.revature.util.ScannerUtil;

public class MainMenu implements View{
	
	private UserDao userDao = new UserDao();
	private BankService bs = new BankService();

	public void printMenu() {
		System.out.println("---------- WELCOME TO GOSBANK ----------");

		System.out.println("1. Create Personal Account");
		System.out.println("2. Log-in to Personal Account");
		System.out.println("3. Other");
		System.out.println("0. Exit");
	}
	
	@Override
	public View run() {
		printMenu();
		
		int user_input = ScannerUtil.getInput(3);
		
		switch(user_input) {
			case 0: 
				return null;
			case 1: 
				return new UserMenu(bs.createUser());
			case 2: 
				return new UserMenu(bs.authenticateUser());
			case 3: 
				return null;
			default: 
				return null;
		}
	}
}
