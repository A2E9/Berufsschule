package com.quantenquellcode.model;

public class Customer {
    private int id;
    private String customerId;
    private String firstname;
    private String lastname;
    private double totalSpent;

    public Customer( String customerId, String firstname, String lastname) {

        this.customerId = customerId;
        this.firstname = firstname;
        this.lastname = lastname;
    } 
    public Customer(int id, String customerId, String firstname, String lastname, double totalSpent) {
        this.id = id;
        this.customerId = customerId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalSpent = totalSpent;
    }

    public double getTotalSpent(){
        return totalSpent;
    }

    public int getID() {
        return this.id;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    @Override
    public String toString() {
        return "ID: " + this.customerId + " firstName: " + this.firstname + " secondName: " + this.lastname;
    }
}