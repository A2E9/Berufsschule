package com.quantenquellcode.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.quantenquellcode.model.Product;
import com.quantenquellcode.utils.DatabaseConnection;

public class ProductDAO {
    private DatabaseConnection dbConnection;

    public ProductDAO() {
        // dbConnection = new DatabaseConnection("caffeshop.db");
    }

    public void updateProduct(Product product) {
        dbConnection = new DatabaseConnection("caffeshop.db");
    
        String productSql = "UPDATE products SET category = ? WHERE name = ?";
        String sizeUpdateSql = "UPDATE prices SET price = ? WHERE product_id = (SELECT id FROM products WHERE name = ?) AND size = ?";
        String sizeInsertSql = "INSERT INTO prices (product_id, size, price) VALUES ((SELECT id FROM products WHERE name = ?), ?, ?)";
    
        try {
            // Update the product first
            PreparedStatement productStmt = dbConnection.getConnection().prepareStatement(productSql);
            productStmt.setString(1, product.getCategory());
            productStmt.setString(2, product.getName());
            productStmt.executeUpdate();
    
            // PreparedStatements for sizes
            PreparedStatement sizeUpdateStmt = dbConnection.getConnection().prepareStatement(sizeUpdateSql);
            PreparedStatement sizeInsertStmt = dbConnection.getConnection().prepareStatement(sizeInsertSql);
    
            // For each size, update or insert as appropriate
            for (String size : new String[] {"small", "mid", "big", "universal"}) {
                float price = 0.0f;
                switch(size) {
                    case "small": price = product.getSmall(); break;
                    case "mid": price = product.getMid(); break;
                    case "big": price = product.getBig(); break;
                    case "universal": price = product.getUniversal(); break;
                }
    
                // Check if a record exists for this product and size
                PreparedStatement checkStmt = dbConnection.getConnection().prepareStatement("SELECT COUNT(*) FROM prices WHERE product_id = (SELECT id FROM products WHERE name = ?) AND size = ?");
                checkStmt.setString(1, product.getName());
                checkStmt.setString(2, size);
                ResultSet rs = checkStmt.executeQuery();
    
                if (rs.next() && rs.getInt(1) > 0) { // Record exists, so update it
                    sizeUpdateStmt.setFloat(1, price);
                    sizeUpdateStmt.setString(2, product.getName());
                    sizeUpdateStmt.setString(3, size);
                    sizeUpdateStmt.executeUpdate();
                } else { // No record exists, so insert a new one (even if the price is 0.0f)
                    sizeInsertStmt.setString(1, product.getName());
                    sizeInsertStmt.setString(2, size);
                    sizeInsertStmt.setFloat(3, price);
                    sizeInsertStmt.executeUpdate();
                }
    
                rs.close();
                checkStmt.close();
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
    
    public void deleteProductByName(String productName) {
        dbConnection = new DatabaseConnection("caffeshop.db");
    
        String deleteProductSql = "DELETE FROM products WHERE name = ?";
        String deleteSizesSql = "DELETE FROM prices WHERE product_id IN (SELECT id FROM products WHERE name = ?)";
    
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement deleteProductStatement = connection.prepareStatement(deleteProductSql);
             PreparedStatement deleteSizesStatement = connection.prepareStatement(deleteSizesSql)) {
    
            deleteSizesStatement.setString(1, productName);
            deleteSizesStatement.executeUpdate();
    
            deleteProductStatement.setString(1, productName);
            int affectedRows = deleteProductStatement.executeUpdate();
    
            if (affectedRows > 0) {
                System.out.println("Product and related sizes deleted successfully.");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }
    
    public void createProduct(Product product) {
        dbConnection = new DatabaseConnection("caffeshop.db");
    
        String productSql = "INSERT INTO products (category, name) VALUES (?, ?)";
        String sizesSql = "INSERT INTO prices (product_id, size, price) VALUES ((SELECT id FROM products WHERE name = ?), ?, ?)";
    
        try {
            PreparedStatement productStmt = dbConnection.getConnection().prepareStatement(productSql);
            productStmt.setString(1, product.getCategory());
            productStmt.setString(2, product.getName());
            productStmt.executeUpdate();
    
            PreparedStatement sizesStmt = dbConnection.getConnection().prepareStatement(sizesSql);
            
            if (product.getSmall() > 0.0f) {
                sizesStmt.setString(1, product.getName());
                sizesStmt.setString(2, "small");
                sizesStmt.setFloat(3, product.getSmall());
                sizesStmt.executeUpdate();
            }
    
            if (product.getMid() > 0.0f) {
                sizesStmt.setString(1, product.getName());
                sizesStmt.setString(2, "mid");
                sizesStmt.setFloat(3, product.getMid());
                sizesStmt.executeUpdate();
            }
    
            if (product.getBig() > 0.0f) {
                sizesStmt.setString(1, product.getName());
                sizesStmt.setString(2, "big");
                sizesStmt.setFloat(3, product.getBig());
                sizesStmt.executeUpdate();
            }
    
            if (product.getUniversal() > 0.0f) {
                sizesStmt.setString(1, product.getName());
                sizesStmt.setString(2, "universal");
                sizesStmt.setFloat(3, product.getUniversal());
                sizesStmt.executeUpdate();
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //  finally {
        //     dbConnection.getConnection().close();
        // }
    }

    public List<String> fetchCategories()  {
        dbConnection = new DatabaseConnection("caffeshop.db");
        String getCategoriesSql = "SELECT DISTINCT category FROM products";
        List<String> categories = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(getCategoriesSql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // finally {
        //     dbConnection.getConnection().close();
        // }
        return categories;
    }

    public List<Product> fetchProducts()  {
        dbConnection = new DatabaseConnection("caffeshop.db");
        String getUserSql = "SELECT DISTINCT "
        + "name, "
        + "category, "
        + "COALESCE((SELECT CASE WHEN price = NULL THEN 0 ELSE price END FROM prices "
        + "WHERE size = 'small' AND products.id = prices.product_id), 0) || ',' || "
        + "COALESCE((SELECT CASE WHEN price = NULL THEN 0 ELSE price END FROM prices "
        + "WHERE size = 'mid' AND products.id = prices.product_id), 0) || ',' || "
        + "COALESCE((SELECT CASE WHEN price = NULL THEN 0 ELSE price END FROM prices "
        + "WHERE size = 'big' AND products.id = prices.product_id), 0) || ',' || "
        + "COALESCE((SELECT CASE WHEN price = NULL THEN 0 ELSE price END FROM prices "
        + "WHERE size = 'universal' AND products.id = prices.product_id), 0) AS "
        + "price_size "
        + "FROM "
        + "products "
        + "INNER JOIN prices ON products.id = prices.product_id ";
        
        List<Product> prList = new ArrayList<Product>();
        
        try (Connection connection = dbConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(getUserSql)) {

                    ResultSet rs = pstmt.executeQuery();
                    
                    String[] result = {};
                    
                    while (rs.next()) {
                        String name = rs.getString("name");
                        String category = rs.getString("category");
                        String price_size = rs.getString("price_size");
                        
                        result = price_size.split(",");
                        
                        float small = Float.parseFloat(result[0]);
                        float mid = Float.parseFloat(result[1]);
                        float big = Float.parseFloat(result[2]);
                        float universal = Float.parseFloat(result[3]);
                        
                        Product pr = new Product(name, small, mid, big, universal, category);
                        prList.add(pr);
                    }
                    
                    return prList;
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                // finally {
                //     dbConnection.getConnection().close();
                // }

                return null;
    }

    public List<Product> fetchProductsByCategory(String category)  {
        dbConnection = new DatabaseConnection("caffeshop.db");
        String getUserSql = "SELECT po.name, po.category, pi.size, pi.price "
                + " FROM products po "
                + " INNER JOIN prices pi ON pi.product_id = po.id "
                + " WHERE po.category = ?";

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(getUserSql)) {

            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            Map<String, Product> productMap = new HashMap<>();

            while (rs.next()) {
                String name = rs.getString("name");
                String size = rs.getString("size");
                float price = rs.getFloat("price");

                Product product = productMap.get(name);
                if (product == null) {
                    product = new Product(name);
                    productMap.put(name, product);
                }
                product.setCategory(category);

                switch (size) {
                    case "small":
                        product.setSmallPrice(price);
                        break;
                    case "mid":
                        product.setMidPrice(price);
                        break;
                    case "big":
                        product.setBigPrice(price);
                        break;
                    case "universal":
                        product.setUniversalPrice(price);
                        break;
                }
            }
            List<Product> products = new ArrayList<>(productMap.values());
            return products;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // finally {
        //     dbConnection.getConnection().close();
        // }
        return null;
    }

}
