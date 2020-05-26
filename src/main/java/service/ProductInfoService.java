package service;

import exception.ProductNotFoundException;
import model.ProductInfo;

public interface ProductInfoService {

    ProductInfo getProductInfo(String id) throws ProductNotFoundException;

}
