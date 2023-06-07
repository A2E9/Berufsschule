package com.quantenquellcode.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.quantenquellcode.model.Customer;
import com.quantenquellcode.utils.DatabaseConnection;

public class CustomerDAO {
    private DatabaseConnection dbConnection;

    public CustomerDAO() {
        dbConnection = new DatabaseConnection("caffeshop.db");
    }

    
    
    public Customer fetchCustomer(String customerId) {
        DatabaseConnection dbConnection = new DatabaseConnection("caffeshop.db");
        String getUserSql = "SELECT id, customerId, firstname, lastname FROM customers WHERE customerid = ?";

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(getUserSql)) {

            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                
                Customer customer = new Customer(
                    rs.getInt("id"),
                    rs.getString("customerId"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),0);
                    dbConnection.getConnection().close();
                    return customer;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;
    }

    
    public int fetchCustomerBonus(String customerDB_ID) {
        int currentBonus = 0;
        dbConnection = new DatabaseConnection("caffeshop.db");
        String selectAmountSql = "SELECT count FROM bonus WHERE customer_id = (SELECT id FROM customers WHERE id = ?)";

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectAmountSql)) {

            selectStmt.setString(1, customerDB_ID);
            ResultSet resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                currentBonus = resultSet.getInt("count");
            }

            dbConnection.getConnection().close();
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }
        return currentBonus;
    }

    public void updateCustomerBonus(int bonus, String customerDB_ID) {

        int bonusAmount = bonus;
        dbConnection = new DatabaseConnection("caffeshop.db");
        String selectAmountSql = "SELECT count FROM bonus WHERE customer_id = (SELECT id FROM customers WHERE id = ?)";
        String updateAmountSql = "UPDATE bonus SET count = ? WHERE customer_id = (SELECT id FROM customers WHERE id = ?)";

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectAmountSql);
                PreparedStatement updateStmt = connection.prepareStatement(updateAmountSql);) {

            selectStmt.setString(1, customerDB_ID);
            ResultSet resultSet = selectStmt.executeQuery();

            int currentBonus = 0;
            if (resultSet.next()) {
                currentBonus = resultSet.getInt("count");
            }

            int newBonus = (currentBonus + bonusAmount) % 14;

            

            updateStmt.setInt(1, newBonus);
            updateStmt.setString(2, customerDB_ID);
            updateStmt.executeUpdate();

            dbConnection.getConnection().close();
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }
    }

    public void updateCustomerAmount(String customerDB_ID, float totalPrice) {

        String selectAmountSql = "SELECT amount FROM payment WHERE customer_id = (SELECT id FROM customers WHERE id = ?)";
        String updateAmountSql = "UPDATE payment SET amount = ? WHERE customer_id = (SELECT id FROM customers WHERE id = ?)";
        dbConnection = new DatabaseConnection("caffeshop.db");
        try (Connection connection = dbConnection.getConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectAmountSql);
                PreparedStatement updateStmt = connection.prepareStatement(updateAmountSql);) {

            selectStmt.setString(1, customerDB_ID);
            ResultSet resultSet = selectStmt.executeQuery();

            BigDecimal currentAmount = BigDecimal.ZERO;
            if (resultSet.next()) {
                currentAmount = resultSet.getBigDecimal("amount");
            }

            BigDecimal totalPriceDecimal = BigDecimal.valueOf(totalPrice);
            BigDecimal newAmount = currentAmount.add(totalPriceDecimal);

            updateStmt.setBigDecimal(1, newAmount);
            updateStmt.setString(2, customerDB_ID);
            updateStmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }
    }


    public List<Customer> fetchCustomers() {
        dbConnection = new DatabaseConnection("caffeshop.db");
        String getCustomerSql = 
            "SELECT customers.id, customers.customerId, customers.firstname, customers.lastname, SUM(payment.amount) AS totalSpent " +
            "FROM customers " +
            "LEFT JOIN payment ON customers.id = payment.customer_id " +
            "GROUP BY customers.id";
        
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(getCustomerSql)) {
    
            ResultSet rs = pstmt.executeQuery();
            List<Customer> customerList = new ArrayList<>();
            while (rs.next()) {
                // Assuming you added a 'totalSpent' field to the Customer class
                Customer customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("customerId"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getDouble("totalSpent"));  // Update this to match your Customer class constructor
    
                customerList.add(customer);
            }
            return customerList;
        } catch (SQLException e) {
            System.out.println(e);
        }
    
        return null;
    }
    
    public boolean insertCustomer(Customer customer){
        dbConnection = new DatabaseConnection("caffeshop.db");
        String insertCustomerSql = "INSERT INTO customers (id, customerId, firstname, lastname) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(insertCustomerSql)) {
                
            pstmt.setInt(1, customer.getID());
            pstmt.setString(2, customer.getCustomerId());
            pstmt.setString(3, customer.getFirstname());
            pstmt.setString(4, customer.getLastname());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }

    public boolean updateCustomer(Customer customer){
        dbConnection = new DatabaseConnection("caffeshop.db");
        String updateCustomerSql = "UPDATE customers SET customerId = ?, firstname = ?, lastname = ? WHERE id = ?";
        
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateCustomerSql)) {
                
            pstmt.setString(1, customer.getCustomerId());
            pstmt.setString(2, customer.getFirstname());
            pstmt.setString(3, customer.getLastname());
            pstmt.setInt(4, customer.getID());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }

    public boolean deleteCustomer(String id) {
        dbConnection = new DatabaseConnection("caffeshop.db");
        String deleteCustomerSql = "DELETE FROM customers WHERE id = ?";
        
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(deleteCustomerSql)) {
                
            pstmt.setString(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }
}
