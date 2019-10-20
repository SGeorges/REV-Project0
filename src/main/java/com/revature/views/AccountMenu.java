package com.revature.views;

import com.revature.dao.UserDao;
import com.revature.models.Account;
import com.revature.models.User;

public class AccountMenu implements View {
	private UserDao userDao = new UserDao();
	private User user = new User();
	private Account account = new Account();
	
	
	public AccountMenu(User user, Account account) {
		super();
		this.user = user;
		this.account = account;
	}


	@Override
	public View run() {
		// TODO Auto-generated method stub
		return null;
	}

}
