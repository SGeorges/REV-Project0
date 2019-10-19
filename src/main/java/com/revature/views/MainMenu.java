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
			case 0: return null;
			case 1: createUser(); return new UserMenu();
			case 2: return null;
			case 3: return null;
			default: return null;
		}
	}
	
	public void createUser() {
		// Requests/gets Full name of the User 
		System.out.print("Please enter your name (Full Name): ");
		String name_input = ScannerUtil.getStringInput();
		
		System.out.println();
		
		// Requests/gets password of the User
		System.out.print("Please enter your password: ");
		String pass_input = ScannerUtil.getStringInput();
		
		System.out.println();
		
		User user = new User(name_input,pass_input);
	}
	
}
