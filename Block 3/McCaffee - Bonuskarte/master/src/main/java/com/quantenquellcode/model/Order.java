package com.quantenquellcode.model;

import java.util.HashMap;
import java.util.Map;

import com.quantenquellcode.controller.MenuController;
import com.quantenquellcode.dao.CustomerDAO;
// import com.quantenquellcode.dao.ProductDAO;
import com.quantenquellcode.utils.AlertHelper;

import javafx.scene.control.Label;

public class Order {
    private Map<Float, Product> productShopMapByPrice;
    private Map<String, Product> productShopMap;
    private Map<String, Label> countLabels;
    private float totalPrice = 0.0f;

    private CustomerDAO customerDAO;
    // private ProductDAO productDAO;

    public Order(Map<Float, Product> productShopMapByPrice, Map<String, Product> productShopMap,
            Map<String, Label> countLabels, float totalPrice) {
        this.customerDAO = new CustomerDAO();
        // this.productDAO = new ProductDAO();
        this.productShopMap = productShopMap;
        this.productShopMapByPrice = productShopMapByPrice;
        this.countLabels = countLabels;
        this.totalPrice = totalPrice;
    }

    public Map<Float, Product> getProductShopMapByPrice() {
        return productShopMapByPrice;
    }

    public Map<String, Product> getProductShopMap() {
        return productShopMap;
    }

    public Map<String, Label> getCountLabels() {
        return countLabels;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float newPrice) {
        totalPrice = newPrice;
    }

    public void addTotalPrice(float price) {
        totalPrice += price;
    }

    public void subTotalPrice(float price) {
        totalPrice -= price;
    }

    public int getCrrCoffeCount() {
        int bonusAmount = 0;
        for (Map.Entry<String, Product> product : productShopMap.entrySet()) {
            if (product.getValue().getCategory().equals("coffee")) {
                bonusAmount += product.getValue().getTotalCount();
            }
        }
        return bonusAmount;
    }

    public void addShopItem(Product product) {
        productShopMap.put(product.getName(), product);
    }

    public Product getShopItem(String name) {
        return productShopMap.get(name);
    }

    public Boolean checkCustomerBonus(String customerDB_ID) {
        int crrBonusCount = getCrrCoffeCount();
        int dbBonusCount = customerDAO.fetchCustomerBonus(customerDB_ID);

        int bonusCount = crrBonusCount + dbBonusCount;

        int db = 0;
        if (dbBonusCount >= 5) {
            db++;
            if (dbBonusCount >= 10) {
                db++;
                if (dbBonusCount >= 14) {
                    db++;
                }
            }
        }
        int crr = 0;
        if (bonusCount >= 5) {
            crr++;
            if (bonusCount >= 10) {
                crr++;
                if (bonusCount >= 14) {
                    crr++;
                }
            }
        }
        int xBonus = Math.abs(crr - db);
        if (bonusCount > 14) {
            bonusCount = bonusCount - 14;

            if (bonusCount >= 5) {
                crr++;
                if (bonusCount >= 10) {
                    crr++;
                    if (bonusCount >= 14) {
                        crr++;
                    }
                }
            }
            xBonus = Math.abs(crr - db);
        }

        int freeCupCount = xBonus;
        float smallestPrice = Float.MAX_VALUE;
        String name = "";
        int lowestPriceCount = 0;
        // copy to save count for updateBonus
        Map<String, Product> productShopMapByPriceCopy = new HashMap<>();
        for (Map.Entry<String, Product> entry : productShopMap.entrySet()) {
            productShopMapByPriceCopy.put(entry.getKey(), new Product(entry.getValue()));
        }

        while (xBonus > 0 && !productShopMapByPrice.isEmpty()) {
            for (Map.Entry<Float, Product> product : productShopMapByPrice.entrySet()) {
                float price = product.getKey();
                if (smallestPrice > price && product.getValue().getCategory().equals("coffee")) {
                    smallestPrice = price;
                    name = product.getValue().getName();
                    lowestPriceCount = productShopMapByPriceCopy.get(name).getLowestPriceCount();
                }
            }

            if (lowestPriceCount <= xBonus) {
                xBonus -= lowestPriceCount;
                productShopMapByPriceCopy.get(name).setLowestPriceCount(0);
            } else {
                productShopMapByPriceCopy.get(name).setLowestPriceCount(lowestPriceCount - xBonus);
                xBonus = 0;
            }

            if (productShopMapByPriceCopy.get(name).getLowestPriceCount() == 0) {
                productShopMapByPrice.remove(smallestPrice);
            }

            smallestPrice = Float.MAX_VALUE;
        }

        // calc new totalPrice

        float newPrice = 0;
        for (Map.Entry<String, Product> prod : productShopMapByPriceCopy.entrySet()) {
            newPrice += prod.getValue().getTotalPrice();
        }
        if (AlertHelper.alertConfirmOrder(freeCupCount, newPrice, totalPrice, productShopMap)) {
            MenuController.updateTotalPrice(newPrice);
            return true;
        }
        return false; // cancel
    }

    public void addToShopList(Product product, String size) {
        Product existingProduct = productShopMap.get(product.getName());

        if (existingProduct == null) {
            addShopItem(product);
            existingProduct = product;

        }
        existingProduct.incrementCount(size);
        String key = product.getName() + ";" + size;
        Label countLabel = countLabels.get(key);
        if (countLabel != null) {
            countLabel.setText(String.valueOf(existingProduct.getCount(size)));
        }

        String productView = existingProduct.getName() + ";" + size + ";";
        MenuController.updatePrice(productView, existingProduct.getPrice(size)); // dont use static Method

        productShopMapByPrice.put(existingProduct.getPrice(size), existingProduct); // TODO: get the price in cart some
                                                                                    // other way cz duplicating
                                                                                    // shop-products-list
    }

}
