package com.revature.views;

import com.revature.dao.UserDao;
import com.revature.models.User;
import com.revature.util.ScannerUtil;

public class MainMenu implements View{
	
	private UserDao userDao = new UserDao();

	public void printMenu() {
		System.out.println();
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
				return new UserMenu(createUser());
			case 2: 
				User user = authenticateUser();
				
				if (user.getId() == 0) {
					System.out.println("Your ID and password credentials do not match.");
					return new MainMenu();
				}
				else {
					return new UserMenu(user);
				}
			case 3: 
				return null;
			default: 
				return null;
		}
	}
	
	public User authenticateUser() {
		// Requests/gets Personal Account ID from User
		System.out.println("Please enter your User Account ID: ");
		int accountIdInput = ScannerUtil.getIntInput();
		
		// Requests/gets Personal Account Password from User
		System.out.println("Please enter your User Account Password: ");
		String passInput = ScannerUtil.getStringInput();
		
		return userDao.authenticateUser(accountIdInput, passInput);
	}
	
	public User createUser() {
		// Requests/gets Full name of the User 
		System.out.println("Please enter your name (Full Name): ");
		String nameInput = ScannerUtil.getStringInput();
		
		// Requests/gets password of the User
		System.out.println("Please enter your password: ");
		String passInput = ScannerUtil.getStringInput();
		
		return userDao.createUser(nameInput, passInput);
	}
}
