package com.quantenquellcode.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.quantenquellcode.App;
import com.quantenquellcode.dao.UserDAO;
import com.quantenquellcode.model.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class UserController implements Initializable {

    private final UserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAO();
    }

    @FXML
    private CheckBox adminCheckBox;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button createUserButton;

    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private ListView<String> userView;
    private Map<String, User> usersByNameMap;
    private ObservableList<String> usersObserve;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<User> userList = userDAO.fetchUsers();
        usersByNameMap = new HashMap<>();
        usersObserve = FXCollections.observableArrayList();
        for (User user : userList) {
            usersObserve.add(user.getUsername());
            usersByNameMap.put(user.getUsername(), user);
        }
        displayUsers();

        createUserButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            Boolean isAdmin = adminCheckBox.isSelected();

            User user = new User(username, password, isAdmin);
            boolean isInserted = userDAO.insertUser(user);

            if (isInserted) {
                usersObserve.add(user.getUsername());
                usersByNameMap.put(user.getUsername(), user);
                try {
                    App.setRoot("user");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void deleteUser() {
        String selectedUser = userView.getSelectionModel().getSelectedItem().toString();
        User userT = usersByNameMap.get(selectedUser);

        boolean isUpdated = userDAO.deleteUser(userT.getID());

        if (isUpdated) {
            usersByNameMap.remove(selectedUser);
            try {
                App.setRoot("user");
            } catch (IOException e) {
            }
        }
    }

    @FXML
    private void updateUser() {
        String selectedUser = userView.getSelectionModel().getSelectedItem();
        User userT = usersByNameMap.get(selectedUser.toString());
        if (userT != null) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            Boolean isAdmin = adminCheckBox.isSelected();

            User user = new User(userT.getID(), username, password, isAdmin);
            boolean isUpdated = userDAO.updateUser(user);

            if (isUpdated) {
                usersByNameMap.put(username, user);
                try {
                    App.setRoot("user");
                } catch (IOException e) {
                }
            }
        }
    }

    public void displayUsers() {
        userView.setItems(usersObserve);
        userView.setOnMouseClicked(event -> {
            String selectedItem = userView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                User user = usersByNameMap.get(selectedItem);
                usernameField.setText(user.getUsername());
                passwordField.setText(user.getPassword());
                adminCheckBox.setSelected(user.getAdmin());

            }
        });
    }

    @FXML
    private void resetFields() {
        userView.getSelectionModel().clearSelection();
        usernameField.setText("");
        passwordField.setText("");
        adminCheckBox.setSelected(false);
    }

    @FXML
    private void returnBack() throws IOException {
        App.setRoot("admin");
    }
}
