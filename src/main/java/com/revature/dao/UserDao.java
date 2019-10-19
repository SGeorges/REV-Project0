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
			String sql = "select account_id, accounts.amount, accounts.account_type, primary_account from account_access " + 
					     "left join accounts on account_access.account_id = accounts.id where personal_account_id = ?";
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
	
	
}
