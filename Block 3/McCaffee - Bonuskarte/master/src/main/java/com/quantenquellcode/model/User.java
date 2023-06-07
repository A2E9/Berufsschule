package com.quantenquellcode.model;

public class User {
    private int id;
    private String username;
    private String password;
    private Boolean admin;

    public User (String username,String password, Boolean admin){
        this.admin = admin;
        this.password = password;
        this.username = username;
    }
    public User (int id, String username,String password, Boolean admin){
        this.id = id;
        this.admin = admin;
        this.password = password;
        this.username = username;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    } 
    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
