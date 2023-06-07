package com.quantenquellcode.model;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private String name;
    private float small;
    private float mid;
    private float big;
    private float universal;
    private String category;

    private Map<String, Integer> counts = new HashMap<>();

    public Product(String name) {
        this.name = name;
    }

    public Product(String name, float small, float mid, float big, float universal, String category) {
        this.name = name;
        this.small = small;
        this.mid = mid;
        this.big = big;
        this.universal = universal;
        this.category = category;
    }

    public Product(Product otherProduct) {
        this.name = otherProduct.name;
        this.small = otherProduct.small;
        this.mid = otherProduct.mid;
        this.big = otherProduct.big;
        this.universal = otherProduct.universal;
        this.category = otherProduct.category;
        this.counts = new HashMap<>(otherProduct.counts);
    }

    
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public float getPrice(String size) {
        switch (size) {
            case "klein":
                return small;
            case "mittel":
                return mid;
            case "groß":
                return big;
            case "universal":
                return universal;
            default:
                return 0.0f;
        }
    }
    
    public int getCount(String size) {
        return counts.getOrDefault(size, 0);
    }
    
    public int getTotalCount() {
        int sumCount = 0;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            sumCount += entry.getValue();
        }
        return sumCount;
    }

    public float getTotalPrice() {
        float sum = 0;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            sum += entry.getValue() * getPrice(entry.getKey());
        }

        return sum;
    }

    public int getLowestPriceCount() {
        if (counts.containsKey("klein"))
            return counts.get("klein");
        if (counts.containsKey("mittel"))
            return counts.get("mittel");
        if (counts.containsKey("groß"))
            return counts.get("groß");
        if (counts.containsKey("universal"))
            return counts.get("universal");

        return 0;
    }

    public float getLowestCoffePrice() {
        if (small > 0.1f) {
            return small;
        }
        if (mid > 0.1f) {
            return mid;
        }
        if (big > 0.1f) {
            return big;
        }
        return 0;
    }
   
    public Map<String, Integer> getCountsMap() {
        return counts;
    }

    public void incrementCount(String size) {
        counts.put(size, getCount(size) + 1);
    }

    public void decrementCount(String size) {
        int currentCount = getCount(size);
        if (currentCount > 0) {
            counts.put(size, currentCount - 1);
        }
    }

    public void resetCountForSize(String size) {
        counts.put(size, 0);
    }

    public void resetAllCounts() {
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            entry.setValue(0);
        }
    }

    public void setLowestPriceCount(int newCount) {
        if (counts.containsKey("klein")) {
            counts.put("klein", newCount);
        } else if (counts.containsKey("mittel")) {
            counts.put("mittel", newCount);
        } else if (counts.containsKey("groß")) {
            counts.put("groß", newCount);
        } else if (counts.containsKey("universal")) {
            counts.put("universal", newCount);
        }
    }

    public void setSmallPrice(float small) {
        this.small = small;
    }

    public void setMidPrice(float mid) {
        this.mid = mid;
    }

    public void setBigPrice(float big) {
        this.big = big;
    }

    public void setUniversalPrice(float universal) {
        this.universal = universal;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Category " + this.category + " name: " + this.name + "\n";
    }

    public float getSmall() {
        return small;
    }

    public float getMid() {
        return mid;
    }

    public float getBig() {
        return big;
    }

    public float getUniversal() {
        return universal;
    }

}