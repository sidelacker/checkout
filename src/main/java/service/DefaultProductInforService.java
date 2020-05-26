package service;

import exception.ProductNotFoundException;
import model.ProductInfo;

import java.util.Map;

public class DefaultProductInforService implements ProductInfoService {

    private Map<String, ProductInfo> productInfoMap;

    public DefaultProductInforService(Map<String, ProductInfo> productInfoMap) {
        this.productInfoMap = productInfoMap;
    }


    @Override
    public ProductInfo getProductInfo(String id) throws ProductNotFoundException {

        if (!productInfoMap.containsKey(id) || productInfoMap.get(id) == null) {
            throw new ProductNotFoundException(id);
        }

        return productInfoMap.get(id);
    }


}
