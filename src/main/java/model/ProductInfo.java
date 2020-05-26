package model;

import java.math.BigDecimal;

public class ProductInfo {

    private String id;
    private String name;
    private BigDecimal price;
    private BigDecimal discount;

    public ProductInfo(String id, String name, BigDecimal price, BigDecimal discount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

}
