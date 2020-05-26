package service;

import exception.ProductNotFoundException;
import model.ProductInfo;
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


    @Before
    public void setup() {
        Map<String, ProductInfo> inventoryMap = new HashMap<>();
        inventoryMap.put(PRODUCT_ID_1, new ProductInfo(PRODUCT_ID_1, "Skateboard", BigDecimal.valueOf(10), BigDecimal.valueOf(3)));
        inventoryMap.put(PRODUCT_ID_2, new ProductInfo(PRODUCT_ID_2, "Knee Pads", BigDecimal.valueOf(10), BigDecimal.valueOf(3)));
        inventoryMap.put(PRODUCT_ID_3, new ProductInfo(PRODUCT_ID_3, "Helmet", BigDecimal.valueOf(10), BigDecimal.valueOf(3)));

        productInfoService = new DefaultProductInforService(inventoryMap);
        subject = new CheckoutService(new ShoppingBasket(), productInfoService);
    }


    @Test
    public void shopping_basket_is_empty() throws ProductNotFoundException {

        assertEquals("", subject.getShoppingBasketSummary().getItemsDescription());
        assertEquals(BigDecimal.valueOf(0), subject.getShoppingBasketSummary().getPrice());
        assertEquals(BigDecimal.valueOf(0), subject.getShoppingBasketSummary().getDiscount());
        assertEquals(BigDecimal.valueOf(0), subject.getShoppingBasketSummary().getTotal());
    }

    @Test
    public void can_add_item_to_shopping_basket() throws ProductNotFoundException {

        subject.addItem(PRODUCT_ID_1);

        assertEquals("Skateboard(1)", subject.getShoppingBasketSummary().getItemsDescription());
        assertEquals(BigDecimal.valueOf(10), subject.getShoppingBasketSummary().getPrice());
        assertEquals(BigDecimal.valueOf(3), subject.getShoppingBasketSummary().getDiscount());
        assertEquals(BigDecimal.valueOf(7), subject.getShoppingBasketSummary().getTotal());
    }


    @Test
    public void can_add_two_items_to_shopping_basket() throws ProductNotFoundException {
        subject.addItem(PRODUCT_ID_1);
        subject.addItem(PRODUCT_ID_2);

        assertEquals("Skateboard(1), Knee Pads(1)", subject.getShoppingBasketSummary().getItemsDescription());
        assertEquals(BigDecimal.valueOf(20), subject.getShoppingBasketSummary().getPrice());
        assertEquals(BigDecimal.valueOf(6), subject.getShoppingBasketSummary().getDiscount());
        assertEquals(BigDecimal.valueOf(14), subject.getShoppingBasketSummary().getTotal());
    }

    @Test
    public void can_add_three_items_to_shopping_basket() throws ProductNotFoundException {
        subject.addItem(PRODUCT_ID_1);
        subject.addItem(PRODUCT_ID_2);
        subject.addItem(PRODUCT_ID_3);

        assertEquals("Skateboard(1), Knee Pads(1), Helmet(1)", subject.getShoppingBasketSummary().getItemsDescription());
        assertEquals(BigDecimal.valueOf(30), subject.getShoppingBasketSummary().getPrice());
        assertEquals(BigDecimal.valueOf(9), subject.getShoppingBasketSummary().getDiscount());
        assertEquals(BigDecimal.valueOf(21), subject.getShoppingBasketSummary().getTotal());
    }

    @Test
    public void can_add_multiple_of_single_item() throws ProductNotFoundException {

        subject.addItem(PRODUCT_ID_1);
        subject.addItem(PRODUCT_ID_1);

        assertEquals("Skateboard(2)", subject.getShoppingBasketSummary().getItemsDescription());
        assertEquals(BigDecimal.valueOf(20), subject.getShoppingBasketSummary().getPrice());
        assertEquals(BigDecimal.valueOf(6), subject.getShoppingBasketSummary().getDiscount());
        assertEquals(BigDecimal.valueOf(14), subject.getShoppingBasketSummary().getTotal());
    }

    @Test
    public void can_add_multiple_of_many_items() throws ProductNotFoundException {

        subject.addItem(PRODUCT_ID_1);
        subject.addItem(PRODUCT_ID_2);
        subject.addItem(PRODUCT_ID_2);
        subject.addItem(PRODUCT_ID_3);
        subject.addItem(PRODUCT_ID_3);
        subject.addItem(PRODUCT_ID_3);

        assertEquals("Skateboard(1), Knee Pads(2), Helmet(3)", subject.getShoppingBasketSummary().getItemsDescription());
        assertEquals(BigDecimal.valueOf(60), subject.getShoppingBasketSummary().getPrice());
        assertEquals(BigDecimal.valueOf(18), subject.getShoppingBasketSummary().getDiscount());
        assertEquals(BigDecimal.valueOf(42), subject.getShoppingBasketSummary().getTotal());
    }


    @Test
    public void can_remove_item_from_shopping_basket() throws ProductNotFoundException {
        subject.addItem(PRODUCT_ID_1);
        subject.addItem(PRODUCT_ID_2);
        subject.addItem(PRODUCT_ID_3);

        subject.removeItem(PRODUCT_ID_2);

        assertEquals("Skateboard(1), Helmet(1)", subject.getShoppingBasketSummary().getItemsDescription());
        assertEquals(BigDecimal.valueOf(20), subject.getShoppingBasketSummary().getPrice());
        assertEquals(BigDecimal.valueOf(6), subject.getShoppingBasketSummary().getDiscount());
        assertEquals(BigDecimal.valueOf(14), subject.getShoppingBasketSummary().getTotal());
    }

    @Test
    public void can_some_of_many_items_from_shopping_basket() throws ProductNotFoundException {
        subject.addItem(PRODUCT_ID_1);
        subject.addItem(PRODUCT_ID_2);
        subject.addItem(PRODUCT_ID_2);
        subject.addItem(PRODUCT_ID_3);
        subject.addItem(PRODUCT_ID_3);
        subject.addItem(PRODUCT_ID_3);

        subject.removeItem(PRODUCT_ID_3);
        subject.removeItem(PRODUCT_ID_2);
        subject.removeItem(PRODUCT_ID_3);

        assertEquals("Skateboard(1), Knee Pads(1), Helmet(1)", subject.getShoppingBasketSummary().getItemsDescription());
        assertEquals(BigDecimal.valueOf(30), subject.getShoppingBasketSummary().getPrice());
        assertEquals(BigDecimal.valueOf(9), subject.getShoppingBasketSummary().getDiscount());
        assertEquals(BigDecimal.valueOf(21), subject.getShoppingBasketSummary().getTotal());
    }


    @Test
    public void remove_item_from_empty_baset() throws ProductNotFoundException {

        subject.removeItem(PRODUCT_ID_1);

        assertEquals("", subject.getShoppingBasketSummary().getItemsDescription());
        assertEquals(BigDecimal.valueOf(0), subject.getShoppingBasketSummary().getPrice());
        assertEquals(BigDecimal.valueOf(0), subject.getShoppingBasketSummary().getDiscount());
        assertEquals(BigDecimal.valueOf(0), subject.getShoppingBasketSummary().getTotal());
    }


    @Test
    public void remove_item_not_in_baset() throws ProductNotFoundException {

        subject.addItem(PRODUCT_ID_1);
        subject.removeItem(PRODUCT_ID_2);

        assertEquals("Skateboard(1)", subject.getShoppingBasketSummary().getItemsDescription());
        assertEquals(BigDecimal.valueOf(10), subject.getShoppingBasketSummary().getPrice());
        assertEquals(BigDecimal.valueOf(3), subject.getShoppingBasketSummary().getDiscount());
        assertEquals(BigDecimal.valueOf(7), subject.getShoppingBasketSummary().getTotal());
    }


    @Test(expected = ProductNotFoundException.class)
    public void unknown_product_id_added_to_basket() throws ProductNotFoundException {

        subject.addItem(PRODUCT_ID_1);
        subject.addItem(PRODUCT_ID_2);
        subject.addItem(PRODUCT_ID_UNKNOWN);

        subject.getShoppingBasketSummary();
    }
    
    
    /*
     *The tests below allow for negative totals which would be unrealistic. If requirement was confirmed that this shouldn't be posible then we could update the tests to expect and exception?
     */

    @Test
    public void discount_is_greater_than_price() throws ProductNotFoundException {

        Map<String, ProductInfo> inventoryMap = new HashMap<>();
        inventoryMap.put(PRODUCT_ID_1, new ProductInfo(PRODUCT_ID_1, "Skateboard", BigDecimal.valueOf(10), BigDecimal.valueOf(20)));
        inventoryMap.put(PRODUCT_ID_2, new ProductInfo(PRODUCT_ID_2, "Knee Pads", BigDecimal.valueOf(10), BigDecimal.valueOf(20)));
        inventoryMap.put(PRODUCT_ID_3, new ProductInfo(PRODUCT_ID_3, "Helmet", BigDecimal.valueOf(10), BigDecimal.valueOf(20)));

        productInfoService = new DefaultProductInforService(inventoryMap);
        subject = new CheckoutService(new ShoppingBasket(), productInfoService);

        subject.addItem(PRODUCT_ID_1);
        subject.addItem(PRODUCT_ID_2);
        subject.addItem(PRODUCT_ID_3);

        assertEquals("Skateboard(1), Knee Pads(1), Helmet(1)", subject.getShoppingBasketSummary().getItemsDescription());
        assertEquals(BigDecimal.valueOf(30), subject.getShoppingBasketSummary().getPrice());
        assertEquals(BigDecimal.valueOf(60), subject.getShoppingBasketSummary().getDiscount());
        assertEquals(BigDecimal.valueOf(-30), subject.getShoppingBasketSummary().getTotal());
    }


    @Deprecated
    @Test
    public void price_is_negative() throws ProductNotFoundException {

        Map<String, ProductInfo> inventoryMap = new HashMap<>();
        inventoryMap.put(PRODUCT_ID_1, new ProductInfo(PRODUCT_ID_1, "Skateboard", BigDecimal.valueOf(-10), BigDecimal.valueOf(3)));
        inventoryMap.put(PRODUCT_ID_2, new ProductInfo(PRODUCT_ID_2, "Knee Pads", BigDecimal.valueOf(-10), BigDecimal.valueOf(3)));
        inventoryMap.put(PRODUCT_ID_3, new ProductInfo(PRODUCT_ID_3, "Helmet", BigDecimal.valueOf(-10), BigDecimal.valueOf(3)));

        productInfoService = new DefaultProductInforService(inventoryMap);
        subject = new CheckoutService(new ShoppingBasket(), productInfoService);

        subject.addItem(PRODUCT_ID_1);
        subject.addItem(PRODUCT_ID_2);
        subject.addItem(PRODUCT_ID_3);

        assertEquals("Skateboard(1), Knee Pads(1), Helmet(1)", subject.getShoppingBasketSummary().getItemsDescription());
        assertEquals(BigDecimal.valueOf(-30), subject.getShoppingBasketSummary().getPrice());
        assertEquals(BigDecimal.valueOf(9), subject.getShoppingBasketSummary().getDiscount());
        assertEquals(BigDecimal.valueOf(-39), subject.getShoppingBasketSummary().getTotal());
    }


    @Test
    public void discount_is_negative() throws ProductNotFoundException {
        Map<String, ProductInfo> inventoryMap = new HashMap<>();
        inventoryMap.put(PRODUCT_ID_1, new ProductInfo(PRODUCT_ID_1, "Skateboard", BigDecimal.valueOf(10), BigDecimal.valueOf(-3)));
        inventoryMap.put(PRODUCT_ID_2, new ProductInfo(PRODUCT_ID_2, "Knee Pads", BigDecimal.valueOf(10), BigDecimal.valueOf(-3)));
        inventoryMap.put(PRODUCT_ID_3, new ProductInfo(PRODUCT_ID_3, "Helmet", BigDecimal.valueOf(10), BigDecimal.valueOf(-3)));

        productInfoService = new DefaultProductInforService(inventoryMap);
        subject = new CheckoutService(new ShoppingBasket(), productInfoService);

        subject.addItem(PRODUCT_ID_1);
        subject.addItem(PRODUCT_ID_2);
        subject.addItem(PRODUCT_ID_3);

        assertEquals("Skateboard(1), Knee Pads(1), Helmet(1)", subject.getShoppingBasketSummary().getItemsDescription());
        assertEquals(BigDecimal.valueOf(30), subject.getShoppingBasketSummary().getPrice());
        assertEquals(BigDecimal.valueOf(-9), subject.getShoppingBasketSummary().getDiscount());
        assertEquals(BigDecimal.valueOf(39), subject.getShoppingBasketSummary().getTotal());
    }


}
