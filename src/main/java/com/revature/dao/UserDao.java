package com.revature.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Account;
import com.revature.models.Holder;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

// USER DATA ACCESS OBJECT
public class UserDao {

	Connection conn;
	
	public void setConnection(Connection conn) {
		try {
			if (this.conn != null && !this.conn.isClosed()) {
				System.out.println("Closing Connection");
				this.conn.close();
			}
			
			this.conn = conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// When constructor implemented creates connection with database.
	public UserDao() {
		this.conn = ConnectionUtil.getConnection();
	}
	
	// createUser, getNewUser, extractUser used in sequence to generate
	// a new user row on the 'personal_accounts' table, let the database
	// generate the default/serial values (id, start_date, privileged)
	// and then return that information as the current user. 
	public User createUser(String fullName, String password) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			User user = new User();
			String sql = "INSERT INTO personal_accounts(full_name, password) VALUES (?, ?) ";
			PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1,  fullName);
				statement.setString(2, password);
				
			int updateResult = statement.executeUpdate();
			
			if (updateResult == 1) {
				user = getUser(connection, fullName, password);
			}
			
			return user;
	
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private User getUser(Connection connection, String fullName, String password) throws SQLException {
		String sql = "SELECT * FROM personal_accounts WHERE (LOWER(full_name) = LOWER(?)) AND password = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, fullName);
			statement.setString(2, password);
		
		ResultSet resultSet = statement.executeQuery();
		User user = new User();
		
		while (resultSet.next()) {
			user = extractUser(resultSet);
		}
		
		return user;
	}
	
	private User extractUser(ResultSet resultSet) throws SQLException {
		int id = resultSet.getInt("id");
		String fullName = resultSet.getString("full_name");
		String password = resultSet.getString("password");
		LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
		boolean privileged = resultSet.getBoolean("privileged");
		
		User user = new User(id, fullName, password, startDate, privileged);
		return user;
	}
	
	private Account extractAccount(ResultSet resultSet) throws SQLException {
		int accountId = resultSet.getInt("account_id");
		
		String amountString = resultSet.getString("amount");
			amountString = amountString.substring(1).replace(",", "");
		BigDecimal amount = new BigDecimal(amountString);
		
		String accountType = resultSet.getString("account_type");
		boolean primaryAccount = resultSet.getBoolean("primary_account");
		
		Account account = new Account(accountId, amount, accountType, primaryAccount);
		return account;
	}
	
	private Holder extractHolder(ResultSet resultSet) throws SQLException {
		int personID = resultSet.getInt("id");
		String fullName = resultSet.getString("full_name");
		boolean primaryAccount = resultSet.getBoolean("primary_account");
		
		Holder holder = new Holder(personID, fullName, primaryAccount);
		return holder;
	}

	// Takes user input for id and password then pulls information from Personal_account into the resultSet
	// ResultSet then pushed to extractUser to populate a user model to be returned. 
	public User authenticateUser( int id, String password) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM personal_accounts WHERE id = ? AND password = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, id);
				statement.setString(2, password);
				
			ResultSet resultSet = statement.executeQuery();
			User user = new User();
			
			if (resultSet.next()) {
				user = extractUser(resultSet);
			}
			
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	
	public List<Account> getBankAccounts(int id) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT account_id, accounts.amount, accounts.account_type, primary_account FROM account_access " + 
					     "LEFT JOIN accounts on account_access.account_id = accounts.id WHERE personal_account_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, id);
				
			ResultSet resultSet = statement.executeQuery();
			List<Account> accounts = new ArrayList<>();
			
			while (resultSet.next()) {
				Account account = extractAccount(resultSet);
				accounts.add(account);
			}
			
			return accounts;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Holder> viewAccountHolders(int accountID) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT id, full_name, account_access.primary_account FROM personal_accounts " +
						 "LEFT JOIN account_access ON account_access.personal_account_id = personal_accounts.id " +
						 "WHERE (account_access.account_id = ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, accountID);
				
//			System.out.println(sql);
			ResultSet resultSet = statement.executeQuery();
			List<Holder> holders = new ArrayList<>();
			
			while(resultSet.next()) {
				Holder holder = extractHolder(resultSet);
				holders.add(holder);
			}
			
			return holders;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean makeTransfer(int idIn, int idOut, BigDecimal amount) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT transfer_wealth( ?::Money, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
				statement.setBigDecimal(1, amount);
				statement.setInt(2, idIn);
				statement.setInt(3, idOut);
				
//			System.out.println(statement.toString());
			
			ResultSet resultSet = statement.executeQuery();
			return resultSet.next();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void makeUpdate(int accountId, BigDecimal amount, String operator) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "";
			if (operator.equals("+")) {
				sql = "UPDATE accounts SET (amount) = (amount + ?::money) WHERE id = ?";
			} else if (operator.equals("-")){
				sql = "UPDATE accounts SET (amount) = (amount - ?::money) WHERE id = ?";

			}
			PreparedStatement statement = connection.prepareStatement(sql);
				statement.setBigDecimal(1, amount);
				statement.setInt(2, accountId);
			
			int change = statement.executeUpdate();
			
			if (change > 1) {
				System.out.println("something bad happened.... " + change + "changes");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public BigDecimal distributeWealth(BigDecimal amount, int accountID, int userID) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			connection.setAutoCommit(false);
			
			String sql = "SELECT distribute_wealth(?::money, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
				statement.setBigDecimal(1, amount);
				statement.setInt(2, accountID);
				statement.setInt(3, userID);
			
			ResultSet resultSet = statement.executeQuery();
			BigDecimal increase = new BigDecimal(0);
			
			while(resultSet.next()) {
				String increaseString = resultSet.getString(1);
				increaseString = increaseString.substring(1).replace(",", "");
				BigDecimal a = new BigDecimal(increaseString);
				
				increase = a;
			}
			
			connection.commit();
			return increase;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Account createAccount(int perID, String accType) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT create_account(?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, perID);
				statement.setString(2, accType);
				
			ResultSet resultSet = statement.executeQuery();
			Account account = new Account();
			
			while(resultSet.next()) {
				account = getAccount(connection, resultSet.getInt(1));
			}
			
			return account;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Account getAccount(Connection connection, int accountID) throws SQLException {
		String sql = "SELECT account_id, accounts.amount, accounts.account_type, primary_account FROM account_access " + 
			     	 "LEFT JOIN accounts on account_access.account_id = accounts.id WHERE (account_id = ?)";
		PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, accountID);
			
		ResultSet resultSet = statement.executeQuery();
		Account newAccount = new Account();
		
		while(resultSet.next()) {
			newAccount = extractAccount(resultSet);
		}
		return newAccount;
	}

	public void revokePrimaryHolder(int accountID, int holderID) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "UPDATE account_access SET primary_account = 'false' WHERE (account_id = ?) AND (personal_account_id = ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, accountID);
				statement.setInt(2, holderID);
				
			int update = statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void addAccountHolder(int accountId, int holderID) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT add_holder(?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, holderID);
				statement.setInt(2, accountId);
				
			ResultSet resultSet = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}	
	
}
