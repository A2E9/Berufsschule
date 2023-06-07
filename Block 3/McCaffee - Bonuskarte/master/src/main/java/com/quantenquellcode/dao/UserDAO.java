package com.quantenquellcode.dao;

import com.quantenquellcode.model.Customer;
import com.quantenquellcode.model.User;
import com.quantenquellcode.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private DatabaseConnection dbConnection;

    public UserDAO(){}

    public List<User> fetchUsers(){
        dbConnection = new DatabaseConnection("caffeshop.db");
        String getUserSql = "SELECT id, isadmin, username, password FROM users";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(getUserSql)) {

            ResultSet rs = pstmt.executeQuery();
            List<User> userList = new ArrayList<>();
            while (rs.next()) {
                Boolean admin = rs.getString("isadmin").equals("1") ? true : false;

                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        admin);

                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;
    }

    public boolean insertUser(User user){
        dbConnection = new DatabaseConnection("caffeshop.db");
        String insertUserSql = "INSERT INTO users (username, password, isadmin) VALUES (?, ?, ?)";
        
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(insertUserSql)) {
                
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getAdmin() ? "1" : "0");

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }

    public boolean updateUser(User user){
        dbConnection = new DatabaseConnection("caffeshop.db");
        String updateUserSql = "UPDATE users SET password = ?, isadmin = ?, username = ? WHERE id = ?";
        
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateUserSql)) {
                
            pstmt.setString(1, user.getPassword());
            pstmt.setInt(2, user.getAdmin() ? 1 : 0);
            pstmt.setString(3, user.getUsername());
            pstmt.setInt(4, user.getID());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }

    public boolean deleteUser(int id) {
        dbConnection = new DatabaseConnection("caffeshop.db");
        String deleteUserSql = "DELETE FROM users WHERE id = ?";
        
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(deleteUserSql)) {
                
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }

}
