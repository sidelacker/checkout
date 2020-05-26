package service;

import exception.ProductNotFoundException;
import model.ProductInfo;
import model.ShoppingBasketSummary;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CheckoutServiceTest {

    public static final String PRODUCT_ID_1 = "12345";
    public static final String PRODUCT_ID_2 = "23456";
    public static final String PRODUCT_ID_3 = "34567";
    public static final String PRODUCT_ID_UNKNOWN = "45678";
    
    private CheckoutService subject;
    private ProductInfoService productInfoService;
    private ShoppingBasket userBasket;

    
    @Before
    public void setup() {
        Map<String, ProductInfo> productInfoMap = new HashMap<>();
        productInfoMap.put(PRODUCT_ID_1, new ProductInfo(PRODUCT_ID_1, "Skateboard", BigDecimal.valueOf(10), BigDecimal.valueOf(3)));
        productInfoMap.put(PRODUCT_ID_2, new ProductInfo(PRODUCT_ID_2, "Knee Pads", BigDecimal.valueOf(15), BigDecimal.valueOf(5)));
        productInfoMap.put(PRODUCT_ID_3, new ProductInfo(PRODUCT_ID_3, "Helmet", BigDecimal.valueOf(20), BigDecimal.valueOf(7)));

        productInfoService = new DefaultProductInforService(productInfoMap);
        subject = new CheckoutService(productInfoService);
        
        userBasket = new ShoppingBasket();
    }


    @Test
    public void shopping_basket_is_empty() throws ProductNotFoundException {

        ShoppingBasketSummary result = subject.getShoppingBasketSummary(userBasket);

        assertEquals("", result.getItemsDescription());
        assertEquals(BigDecimal.valueOf(0), result.getPrice());
        assertEquals(BigDecimal.valueOf(0), result.getDiscount());
        assertEquals(BigDecimal.valueOf(0), result.getTotal());
    }

    @Test
    public void can_add_item_to_shopping_basket() throws ProductNotFoundException {

        userBasket.add(PRODUCT_ID_1);

        ShoppingBasketSummary result = subject.getShoppingBasketSummary(userBasket);

        assertEquals("Skateboard(1)", result.getItemsDescription());
        assertEquals(BigDecimal.valueOf(10), result.getPrice());
        assertEquals(BigDecimal.valueOf(3), result.getDiscount());
        assertEquals(BigDecimal.valueOf(7), result.getTotal());
    }


    @Test
    public void can_add_two_items_to_shopping_basket() throws ProductNotFoundException {
        userBasket.add(PRODUCT_ID_1);
        userBasket.add(PRODUCT_ID_2);

        ShoppingBasketSummary result = subject.getShoppingBasketSummary(userBasket);

        assertEquals("Skateboard(1), Knee Pads(1)", result.getItemsDescription());
        assertEquals(BigDecimal.valueOf(25), result.getPrice());
        assertEquals(BigDecimal.valueOf(8), result.getDiscount());
        assertEquals(BigDecimal.valueOf(17), result.getTotal());
    }

    @Test
    public void can_add_three_items_to_shopping_basket() throws ProductNotFoundException {
        userBasket.add(PRODUCT_ID_1);
        userBasket.add(PRODUCT_ID_2);
        userBasket.add(PRODUCT_ID_3);

        ShoppingBasketSummary result = subject.getShoppingBasketSummary(userBasket);

        assertEquals("Skateboard(1), Knee Pads(1), Helmet(1)", result.getItemsDescription());
        assertEquals(BigDecimal.valueOf(45), result.getPrice());
        assertEquals(BigDecimal.valueOf(15), result.getDiscount());
        assertEquals(BigDecimal.valueOf(30), result.getTotal());
    }

    @Test
    public void can_add_multiple_of_single_item() throws ProductNotFoundException {

        userBasket.add(PRODUCT_ID_1);
        userBasket.add(PRODUCT_ID_1);

        ShoppingBasketSummary result = subject.getShoppingBasketSummary(userBasket);

        assertEquals("Skateboard(2)", result.getItemsDescription());
        assertEquals(BigDecimal.valueOf(20), result.getPrice());
        assertEquals(BigDecimal.valueOf(6), result.getDiscount());
        assertEquals(BigDecimal.valueOf(14), result.getTotal());
    }

    @Test
    public void can_add_multiple_of_many_items() throws ProductNotFoundException {

        userBasket.add(PRODUCT_ID_1);
        userBasket.add(PRODUCT_ID_2);
        userBasket.add(PRODUCT_ID_2);
        userBasket.add(PRODUCT_ID_3);
        userBasket.add(PRODUCT_ID_3);
        userBasket.add(PRODUCT_ID_3);

        ShoppingBasketSummary result = subject.getShoppingBasketSummary(userBasket);

        assertEquals("Skateboard(1), Knee Pads(2), Helmet(3)", result.getItemsDescription());
        assertEquals(BigDecimal.valueOf(100), result.getPrice());
        assertEquals(BigDecimal.valueOf(34), result.getDiscount());
        assertEquals(BigDecimal.valueOf(66), result.getTotal());
    }


    @Test
    public void can_remove_item_from_shopping_basket() throws ProductNotFoundException {
        userBasket.add(PRODUCT_ID_1);
        userBasket.add(PRODUCT_ID_2);
        userBasket.add(PRODUCT_ID_3);

        userBasket.remove(PRODUCT_ID_2);

        ShoppingBasketSummary result = subject.getShoppingBasketSummary(userBasket);

        assertEquals("Skateboard(1), Helmet(1)", result.getItemsDescription());
        assertEquals(BigDecimal.valueOf(30), result.getPrice());
        assertEquals(BigDecimal.valueOf(10), result.getDiscount());
        assertEquals(BigDecimal.valueOf(20), result.getTotal());
    }

    @Test
    public void can_some_of_many_items_from_shopping_basket() throws ProductNotFoundException {
        userBasket.add(PRODUCT_ID_1);
        userBasket.add(PRODUCT_ID_2);
        userBasket.add(PRODUCT_ID_2);
        userBasket.add(PRODUCT_ID_3);
        userBasket.add(PRODUCT_ID_3);
        userBasket.add(PRODUCT_ID_3);

        userBasket.remove(PRODUCT_ID_3);
        userBasket.remove(PRODUCT_ID_2);
        userBasket.remove(PRODUCT_ID_3);

        ShoppingBasketSummary result = subject.getShoppingBasketSummary(userBasket);

        assertEquals("Skateboard(1), Knee Pads(1), Helmet(1)", result.getItemsDescription());
        assertEquals(BigDecimal.valueOf(45), result.getPrice());
        assertEquals(BigDecimal.valueOf(15), result.getDiscount());
        assertEquals(BigDecimal.valueOf(30), result.getTotal());
    }


    @Test
    public void remove_item_from_empty_baset() throws ProductNotFoundException {

        userBasket.remove(PRODUCT_ID_1);

        ShoppingBasketSummary result = subject.getShoppingBasketSummary(userBasket);

        assertEquals("", result.getItemsDescription());
        assertEquals(BigDecimal.valueOf(0), result.getPrice());
        assertEquals(BigDecimal.valueOf(0), result.getDiscount());
        assertEquals(BigDecimal.valueOf(0), result.getTotal());
    }


    @Test
    public void remove_item_not_in_baset() throws ProductNotFoundException {

        userBasket.add(PRODUCT_ID_1);
        userBasket.remove(PRODUCT_ID_2);

        ShoppingBasketSummary result = subject.getShoppingBasketSummary(userBasket);

        assertEquals("Skateboard(1)", result.getItemsDescription());
        assertEquals(BigDecimal.valueOf(10), result.getPrice());
        assertEquals(BigDecimal.valueOf(3), result.getDiscount());
        assertEquals(BigDecimal.valueOf(7), result.getTotal());
    }


    @Test(expected = ProductNotFoundException.class)
    public void unknown_product_id_added_to_basket() throws ProductNotFoundException {

        userBasket.add(PRODUCT_ID_1);
        userBasket.add(PRODUCT_ID_2);
        userBasket.add(PRODUCT_ID_UNKNOWN);

        subject.getShoppingBasketSummary(userBasket);
    }
    
    
    /*
     * The tests below allow for negative totals which would be unrealistic. If requirement is confirmed that this shouldn't be possible then we could update the tests to expect and exception?
     */

    @Test
    public void discount_is_greater_than_price() throws ProductNotFoundException {

        Map<String, ProductInfo> inventoryMap = new HashMap<>();
        inventoryMap.put(PRODUCT_ID_1, new ProductInfo(PRODUCT_ID_1, "Skateboard", BigDecimal.valueOf(10), BigDecimal.valueOf(20)));
        inventoryMap.put(PRODUCT_ID_2, new ProductInfo(PRODUCT_ID_2, "Knee Pads", BigDecimal.valueOf(10), BigDecimal.valueOf(20)));
        inventoryMap.put(PRODUCT_ID_3, new ProductInfo(PRODUCT_ID_3, "Helmet", BigDecimal.valueOf(10), BigDecimal.valueOf(20)));

        productInfoService = new DefaultProductInforService(inventoryMap);
        subject = new CheckoutService(productInfoService);

        userBasket.add(PRODUCT_ID_1);
        userBasket.add(PRODUCT_ID_2);
        userBasket.add(PRODUCT_ID_3);

        ShoppingBasketSummary result = subject.getShoppingBasketSummary(userBasket);

        assertEquals("Skateboard(1), Knee Pads(1), Helmet(1)", result.getItemsDescription());
        assertEquals(BigDecimal.valueOf(30), result.getPrice());
        assertEquals(BigDecimal.valueOf(60), result.getDiscount());
        assertEquals(BigDecimal.valueOf(-30), result.getTotal());
    }


    @Deprecated
    @Test
    public void price_is_negative() throws ProductNotFoundException {

        Map<String, ProductInfo> inventoryMap = new HashMap<>();
        inventoryMap.put(PRODUCT_ID_1, new ProductInfo(PRODUCT_ID_1, "Skateboard", BigDecimal.valueOf(-10), BigDecimal.valueOf(3)));
        inventoryMap.put(PRODUCT_ID_2, new ProductInfo(PRODUCT_ID_2, "Knee Pads", BigDecimal.valueOf(-10), BigDecimal.valueOf(3)));
        inventoryMap.put(PRODUCT_ID_3, new ProductInfo(PRODUCT_ID_3, "Helmet", BigDecimal.valueOf(-10), BigDecimal.valueOf(3)));

        productInfoService = new DefaultProductInforService(inventoryMap);
        subject = new CheckoutService(productInfoService);

        userBasket.add(PRODUCT_ID_1);
        userBasket.add(PRODUCT_ID_2);
        userBasket.add(PRODUCT_ID_3);

        ShoppingBasketSummary result = subject.getShoppingBasketSummary(userBasket);

        assertEquals("Skateboard(1), Knee Pads(1), Helmet(1)", result.getItemsDescription());
        assertEquals(BigDecimal.valueOf(-30), result.getPrice());
        assertEquals(BigDecimal.valueOf(9), result.getDiscount());
        assertEquals(BigDecimal.valueOf(-39), result.getTotal());
    }


    @Test
    public void discount_is_negative() throws ProductNotFoundException {
        Map<String, ProductInfo> inventoryMap = new HashMap<>();
        inventoryMap.put(PRODUCT_ID_1, new ProductInfo(PRODUCT_ID_1, "Skateboard", BigDecimal.valueOf(10), BigDecimal.valueOf(-3)));
        inventoryMap.put(PRODUCT_ID_2, new ProductInfo(PRODUCT_ID_2, "Knee Pads", BigDecimal.valueOf(10), BigDecimal.valueOf(-3)));
        inventoryMap.put(PRODUCT_ID_3, new ProductInfo(PRODUCT_ID_3, "Helmet", BigDecimal.valueOf(10), BigDecimal.valueOf(-3)));

        productInfoService = new DefaultProductInforService(inventoryMap);
        subject = new CheckoutService(productInfoService);

        userBasket.add(PRODUCT_ID_1);
        userBasket.add(PRODUCT_ID_2);
        userBasket.add(PRODUCT_ID_3);

        ShoppingBasketSummary result = subject.getShoppingBasketSummary(userBasket);

        assertEquals("Skateboard(1), Knee Pads(1), Helmet(1)", result.getItemsDescription());
        assertEquals(BigDecimal.valueOf(30), result.getPrice());
        assertEquals(BigDecimal.valueOf(-9), result.getDiscount());
        assertEquals(BigDecimal.valueOf(39), result.getTotal());
    }


}
