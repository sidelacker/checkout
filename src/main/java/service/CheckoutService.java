package service;

import exception.ProductNotFoundException;
import model.ProductInfo;
import model.ShoppingBasketSummary;

import java.math.BigDecimal;
import java.util.Map;

public class CheckoutService {

    private ProductInfoService productInfoService;

    public CheckoutService(ProductInfoService productInfoService) {
        this.productInfoService = productInfoService;
    }


    public ShoppingBasketSummary getShoppingBasketSummary(ShoppingBasket basket) throws ProductNotFoundException {

        String itemsDescription = "";
        BigDecimal price = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;

        Map<String, Integer> items = basket.getItems();

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            ProductInfo productInfo = productInfoService.getProductInfo(entry.getKey());

            String itemName = productInfo.getName();
            BigDecimal itemCost = productInfo.getPrice().multiply(new BigDecimal(entry.getValue()));
            BigDecimal itemDiscount = productInfo.getDiscount().multiply(new BigDecimal(entry.getValue()));

            itemsDescription += String.format("%s(%s), ", itemName, entry.getValue());

            price = price.add(itemCost);
            discount = discount.add(itemDiscount);
        }

        BigDecimal total = price.subtract(discount);

        return new ShoppingBasketSummary(itemsDescription.replaceAll(", $", ""), price, discount, total);
    }

}

