package com.revature.test.services;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.mockito.Mockito;

import com.revature.dao.UserDao;
import com.revature.models.Account;
import com.revature.services.BankService;


public class BankServicesTest {
	
	UserDao userDao = Mockito.mock(UserDao.class);
	BankService bankService = new BankService(userDao);
	
	@Test
	public void directDepositTest() {
		Account account = new Account(10, BigDecimal.valueOf(100), "Checking", true);
		BigDecimal amount = new BigDecimal(100);
		
		Mockito.when(userDao.makeUpdate(account.getId(), amount, "+"))
								.thenReturn(account);

		//Mockito.when(userDao.getAccount(10))
		//.thenReturn(account);
		
	
		/* Run Operation */
		account = bankService.makeDeposit(account, amount);
		
		Mockito.verify(userDao).makeUpdate(10, BigDecimal.valueOf(100), "+");
		
		assertEquals("100 + 100 = 200", account.getAmount(), BigDecimal.valueOf(200));	
	}
	
	@Test
	public void distributeWealthTest() {
		Account account = new Account(10, BigDecimal.valueOf(100), "Checking", true);
		BigDecimal amount = new BigDecimal(100);
		int userID = 10;
		
		Mockito.when(userDao.distributeWealth(BigDecimal.valueOf(100), 10, 10))
									.thenReturn(BigDecimal.valueOf(2));
		
		account = bankService.makeDeposit(account, amount, userID);
		
		Mockito.verify(userDao).distributeWealth(BigDecimal.valueOf(100), 10, 10);
		
		assertEquals("100+100 = 102", account.getAmount(), BigDecimal.valueOf(102));
	}
	
	@Test
	public void makeTransferTest() {
		Account account = new Account(10, BigDecimal.valueOf(100), "Checking", true);
		BigDecimal amount = new BigDecimal(100);
		int accountIDIn = 11;
		
		Mockito.when(userDao.makeTransfer(11, 10, BigDecimal.valueOf(100)))
					.thenReturn(true);
		
		account = bankService.makeTransfer(accountIDIn, account, amount);
		
		Mockito.verify(userDao).makeTransfer(11,  10,  BigDecimal.valueOf(100));
		
		assertEquals("100-100 = 0", account.getAmount(), BigDecimal.valueOf(0));
		
			
	}
}