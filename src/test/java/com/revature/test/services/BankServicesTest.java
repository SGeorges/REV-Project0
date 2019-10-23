package com.revature.test.services;

import org.junit.Test;
import org.mockito.Mockito;

import com.revature.dao.UserDao;
import com.revature.models.Account;
import com.revature.services.BankService;


public class BankServicesTest {
	
	UserDao userDao = Mockito.mock(UserDao.class);
	Account account = Mockito.mock(Account.class);
	BankService bankService = new BankService();
	
	@Test
	public void depositTest() {
		BankAccount retrievedAccount = new BankAccount(100);
		int accountId = 20;
		
		Mockito.when(repository.getAccount(accountId))
							   .thenReturn(retrievedAccount);
		
		
	
		/* Run Operation */
	}
}
