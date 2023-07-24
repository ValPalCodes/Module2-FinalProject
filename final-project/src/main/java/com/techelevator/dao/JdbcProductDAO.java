package com.techelevator.dao;

import com.techelevator.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcProductDAO implements ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcProductDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Product product = mapRowToProduct(results);
            products.add(product);
        }
        return products;
    }

    @Override
    public Product getProductById(int id) {
        String sql = "SELECT * FROM product WHERE product_id= ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            return mapRowToProduct(results);
        } else {
            return null;
        }
    }

    @Override
    public List<Product> getProductBySku(String sku) {
        List<Product> productListBySku = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE product_sku = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, sku);
        while (results.next()) {
            Product product = mapRowToProduct(results);
            productListBySku.add(product);
        }
        return productListBySku;
    }

    @Override
    public List<Product> getProductByName(String name) {
        List<Product> productsByName = new ArrayList<>();
        name = "%" + name + "%";
        String sql = "SELECT * FROM product WHERE name ILIKE ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, name);
        while (results.next()) {
            Product product = mapRowToProduct(results);
            productsByName.add(product);
        }
        return productsByName;
    }



    private Product mapRowToProduct(SqlRowSet results) {
        Product product = new Product();
        product.setId(results.getInt("product_id"));
        product.setSku(results.getString("product_sku"));
        product.setName(results.getString("name"));
        product.setDescription(results.getString("description"));
        product.setPrice(results.getBigDecimal("price"));
        product.setImage_name(results.getString("image_name"));
        return product;
    }
}
