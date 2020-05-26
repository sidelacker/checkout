package exception;

import static java.lang.String.format;

public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String id) {
        super(format("Product not found in inventory: %s", id));
    }

}
