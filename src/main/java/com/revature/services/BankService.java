package com.revature.services;

import com.revature.dao.UserDao;
import com.revature.models.User;
import com.revature.util.ScannerUtil;

public class BankService {
	private UserDao userDao = new UserDao();
	
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
