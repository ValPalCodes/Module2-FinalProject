package com.techelevator.controller;


import com.techelevator.dao.JdbcProductDAO;
import com.techelevator.dao.ProductDao;
import com.techelevator.model.Product;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductDao dao;

    public ProductController(ProductDao dao) {
        this.dao = dao;
    }

    @RequestMapping(path= "", method = RequestMethod.GET)
    public List<Product> list(
            @RequestParam (required = false) String name,
            @RequestParam (required = false) String sku
            ) {

        if(name != null) {
            return dao.getProductByName(name);
        } else if
            (sku != null) {
            return dao.getProductBySku(sku);
        }
        return dao.findAllProducts();
    }

    @RequestMapping (path = "/{id}", method = RequestMethod.GET)
    public Product getProductById (@PathVariable int id) {
        return dao.getProductById(id);
    }

}

