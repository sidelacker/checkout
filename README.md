# Website Checkout
The three main classes in this solution are:

* CheckoutService
* ProductInfoService (interface)
* ShoppingBasket

#### CheckoutService
The CheckoutService contains the main business logic for this solution, and is intended to be used within the service layer of an application. In the context of a spring web application, CheckoutService could be instantiated as a Spring Bean.

The CheckoutService constructor takes in an interface type of ProductInfoService. Any implementation of ProductInfoService can be injected; the tests inject a simple DefaultProductInfoService instance which just holds project data in memory.

CheckoutService performs business logic to return a summary of pricing information from a users shopping cart. It delegates the retrieval of up-to-date product price information to the ProductInfoService implementation.

The idea is that other implementations of the ProductInfoService can be wired into the application; these may get product information from a database or file system etc.

The  **CheckoutService.getShoppingBasketSummary** method takes a  ShoppingBasket input parameter which is simply a container of product ids (and number of), that the user has selected for their shopping cart. CheckoutService then queries  ProductInfoService for up to date product details, and creates a  ShoppingBasketSummary to return to the calling class.

In the context of a Spring application CheckoutService and  ProductInfoService could be spring beans, wired at startup by the application.

ShoppingBasket woulld be sessions scoped to the user.

The CheckoutServiceTest unit tests setup an instance of  DefaultProductInfoService an inject this into CheckoutService.

#### ProductInfoService
Interface that encapsulates access to product price data. Different implementations could be wired up to the application. In this case we use a simple in-memory implementation called DefaultProductInfoService.

#### ShoppingBasket
ShoppingBasket is a simple represetnation of a shopping cart that store a map of product Ids and the number of each product. The idea is that this class only has a reference to products Ids that a user has added to their cart. Product details such as price and discounts are encapsulated behind an implementation of ProductInfoService.

ShoppingBasket would be a session scoped object for a single user of the system. The application can pass this instance of  ShoppingBasket to the **CheckoutService.getShoppingBasketSummary()** service method to obtain a summary of price information for the currently selected products in their session’s shopping basket.
