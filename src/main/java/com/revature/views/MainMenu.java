package com.revature.views;

import com.revature.models.User;
import com.revature.util.ScannerUtil;

public class MainMenu implements View{

	public void printMenu() {
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
				createUser(); 
				return new UserMenu();
			case 2: 
				authenticateUser(); 
				return new UserMenu();
			case 3: 
				return null;
			default: 
				return null;
		}
	}

	public void createUser() {
		// Requests/gets Full name of the User 
		System.out.println("Please enter your name (Full Name): ");
		String nameInput = ScannerUtil.getStringInput();
		
		// Requests/gets password of the User
		System.out.println("Please enter your password: ");
		String passInput = ScannerUtil.getStringInput();
		
		User user = new User(nameInput,passInput);
		
		System.out.printf("User Name: %s, User Password: %s %n", user.getFullName(), user.getPassword());
	}
	
	private void authenticateUser() {
		// Requests/gets Personal Account ID from User
		System.out.println("Please enter your User Account ID: ");
		int pAccountID = new ScannerUtil().getIntInput();
		
		// Requests/gets Personal Account Password from User
		System.out.println("Please enter your User Account Password: ");
		String passInput = ScannerUtil.getStringInput();
		
	}
	
}
