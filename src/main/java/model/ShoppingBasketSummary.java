package model;

import java.math.BigDecimal;

public class ShoppingBasketSummary {

    private String itemsDescription;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal total;


    public ShoppingBasketSummary(String itemsDescription, BigDecimal price, BigDecimal discount, BigDecimal total) {
        this.itemsDescription = itemsDescription;
        this.price = price;
        this.discount = discount;
        this.total = total;
    }

    public String getItemsDescription() {
        return itemsDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
