package com.techelevator.dao;

import com.techelevator.model.Product;

import java.util.List;

public interface ProductDao {

    List<Product> findAllProducts();

    Product getProductById(int id);

    List<Product> getProductBySku(String sku);

    List<Product> getProductByName(String name);

}
