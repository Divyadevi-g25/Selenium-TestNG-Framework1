package com.orangehrm.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.orangehrm.base.BaseClass;

public class DBConnection {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/orangehrm";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "";
	private static final Logger logger = BaseClass.logger;  //Instance of logger class

	// To make the connection with database
	public static Connection getDBConnection() {
		try {
			logger.info("Starting DB connection...");
			Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			logger.info("DB connection successful");
			return conn;
		} catch (SQLException e) {
			logger.error("Error while establishing DB connection");
			e.printStackTrace();
			return null;
		}

	}

	// To get the employeedetails  from database sand store in map
	public static Map<String, String> getEmployeeDetails(String employee_id) {

		String query = "SELECT emp_firstname, emp_middle_name, emp_lastname from hs_hr_employee WHERE employee_id ="
				+ employee_id;
		// To store employee details in map

		Map<String, String> employeeDetails = new HashMap<>();

		try (Connection conn = getDBConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			logger.info("Executing query: " + query);
			if (rs.next()) {
				String firstName = rs.getString("emp_firstName");
				String middleName = rs.getString("emp_middle_Name");
				String lastName = rs.getString("emp_lastName");

				// store in map
				employeeDetails.put("firstName", firstName);
				employeeDetails.put("middleName", middleName!=null? middleName:"");
				employeeDetails.put("lastName", lastName);

				logger.info("Query executed successfully");
				logger.info("Employee Data fetched: "+employeeDetails);
			} else {
				logger.error("Employee not found");
			}
		} catch (Exception e) {
			System.out.println("Error while executing query");
			e.printStackTrace();
		}
		return employeeDetails;

	}

}
