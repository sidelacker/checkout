package service;

import java.util.Map;
import java.util.TreeMap;

public class ShoppingBasket {

    private Map<String, Integer> items = new TreeMap<>();

    public void add(String productId) {
        int count = items.containsKey(productId) ? items.get(productId) : 0;
        items.put(productId, count + 1);
    }

    public void remove(String productId) {

        if (!items.containsKey(productId) || items.get(productId) < 2) {
            items.remove(productId);
        } else {
            int current = items.get(productId);
            items.put(productId, current - 1);
        }
    }

    public Map<String, Integer> getItems() {
        return items;
    }

}
