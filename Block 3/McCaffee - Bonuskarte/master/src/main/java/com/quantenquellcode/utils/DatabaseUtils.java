package com.quantenquellcode.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtils {

        public static void createNewTable() {
                String url = "jdbc:sqlite:caffeshop.db";

                String sqlCustomers = "CREATE TABLE IF NOT EXISTS customers (\n"
                                + "    id integer PRIMARY KEY,\n"
                                + "    customerId text NOT NULL,\n"
                                + "    firstName text NOT NULL,\n"
                                + "    lastName text NOT NULL\n"
                                + ");";

                String sqlProducts = "CREATE TABLE IF NOT EXISTS products (\n"
                                + "    id INTEGER PRIMARY KEY,\n"
                                + "    name TEXT NOT NULL,\n"
                                + "    category TEXT NOT NULL\n"
                                + ");";

                String sqlUsers = "CREATE TABLE IF NOT EXISTS users (\n"
                                + "    id integer PRIMARY KEY,\n"
                                + "    isadmin integer DEFAULT 0 NOT NULL,\n"
                                + "    username text NOT NULL,\n"
                                + "    password text NOT NULL\n"
                                + ");";
                String sqlBonus = "CREATE TABLE IF NOT EXISTS bonus (\n"
                                + "    id integer PRIMARY KEY,\n"
                                + "    customer_id text NOT NULL,\n"
                                + "    count integer DEFAULT 0 NOT NULL,\n"
                                + "    FOREIGN KEY(customer_id) REFERENCES customers(id)\n"
                                + ");";
                String sqlCustomerPayments = "CREATE TABLE IF NOT EXISTS payment (\n"
                                + "    paymentID INTEGER PRIMARY KEY,\n"
                                + "    customer_id TEXT NOT NULL,\n"
                                + "    amount DECIMAL(10, 2) DEFAULT 0.0 NOT NULL,\n"
                                + "    FOREIGN KEY(customer_id) REFERENCES customers(id)\n"
                                + ");";

                try (Connection conn = DriverManager.getConnection(url);
                                Statement stmt = conn.createStatement()) {
                        stmt.execute(sqlBonus);
                        stmt.execute(sqlUsers);
                        stmt.execute(sqlProducts);
                        stmt.execute(sqlCustomers);
                        stmt.execute(sqlCustomerPayments);
                } catch (SQLException e) {
                        System.out.println(e.getMessage());
                }
        }
}

/*
 * 
 * INSERT INTO products (category, name) VALUES ('coffee', 'Espresso');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Espresso'), 'small', 1.69);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Espresso'), 'big', 2.19);
 * 
 * 
 * INSERT INTO products (category, name) VALUES ('coffee', 'Espresso
 * Macchiato');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Espresso Macchiato'), 'small', 1.79);
 * 
 * INSERT INTO products (category, name) VALUES ('coffee', 'Cafe');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Cafe'), 'small', 1.79);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Cafe'), 'mid', 2.19);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Cafe'), 'big', 2.49);
 * 
 * INSERT INTO products (category, name) VALUES ('coffee', 'Flat White
 * Espresso');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Flat White Espresso'), 'small', 1.89);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Flat White Espresso'), 'mid', 2.29);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Flat White Espresso'), 'big', 2.59);
 * 
 * INSERT INTO products (category, name) VALUES ('coffee', 'Cappuccino');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Cappuccino'), 'small', 1.99);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Cappuccino'), 'mid', 2.39);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Cappuccino'), 'big', 2.69);
 * 
 * INSERT INTO products (category, name) VALUES ('coffee', 'Caffe Latte');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Caffe Latte'), 'small', 1.99);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Caffe Latte'), 'mid', 2.39);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Caffe Latte'), 'big', 2.69);
 * 
 * INSERT INTO products (category, name) VALUES ('coffee', 'Latte Macchiato');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Latte Macchiato'), 'mid', 2.39);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Latte Macchiato'), 'big', 2.69);
 * 
 * 
 * INSERT INTO products (category, name) VALUES ('coffee', 'Mocha Espresso');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Mocha Espresso'), 'small', 2.29);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Mocha Espresso'), 'mid', 2.69);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Mocha Espresso'), 'big', 2.99);
 * 
 * INSERT INTO products (category, name) VALUES ('coffee', 'Vienna');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Vienna'), 'small', 2.29);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Vienna'), 'mid', 2.69);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Vienna'), 'big', 2.99);
 * 
 * 
 * 
 * 
 * 
 * 
 * INSERT INTO products (category, name) VALUES ('gebaeck', 'White Choc Lemon
 * Cookie');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'White Choc Lemon Cookie'), 'universal', 1.59);
 * 
 * INSERT INTO products (category, name) VALUES ('gebaeck', 'Blueberry
 * Cheesecake');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Blueberry Cheesecake'), 'universal', 1.00);
 * 
 * 
 * INSERT INTO products (category, name) VALUES ('frappes', 'Schoko');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Schoko'), 'small', 2.99);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Schoko'), 'big', 3.29);
 * 
 * INSERT INTO products (category, name) VALUES ('frappes', 'Chai');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Chai'), 'small', 2.99);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Chai'), 'big', 3.29);
 * 
 * 
 * INSERT INTO products (category, name) VALUES ('frappes', 'Mocha');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Mocha'), 'small', 2.99);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Mocha'), 'big', 3.29);
 * 
 * INSERT INTO products (category, name) VALUES ('frappes', 'Caramel');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Caramel'), 'small', 2.99);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Caramel'), 'big', 3.29);
 * 
 * 
 * 
 * INSERT INTO products (category, name) VALUES ('iceDrink', 'Iced Schokolade');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Iced Schokolade'), 'small', 2.59);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Iced Schokolade'), 'big', 2.99);
 * 
 * INSERT INTO products (category, name) VALUES ('iceDrink', 'Iced Coffee');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Iced Coffee'), 'small', 2.59);
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Iced Coffee'), 'big', 2.99);
 * 
 * 
 * 
 * INSERT INTO products (category, name) VALUES ('vital', 'Italian Bagel');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Italian Bagel'), 'universal', 2.69);
 * 
 * INSERT INTO products (category, name) VALUES ('vital', 'Chicken Bagel');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Chicken Bagel'), 'universal', 2.69);
 * 
 * 
 * 
 * INSERT INTO products (category, name) VALUES ('kaltgetraenk', 'Stilles
 * Mineralwasser');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Stilles Mineralwasser'), 'universal', 1.99);
 * 
 * INSERT INTO products (category, name) VALUES ('kaltgetraenk', 'Bionade');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Bionade'), 'universal', 2.29);
 * 
 * 
 * 
 * INSERT INTO products (category, name) VALUES ('dessert', 'Zitronenkuchen');
 * INSERT INTO product_sizes (product_id, size, price) VALUES ((SELECT id FROM
 * products WHERE name = 'Zitronenkuchen'), 'universal', 1.00);
 * 
 * 
 * 
 */