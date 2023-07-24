package com.techelevator.dao;

import com.techelevator.model.CartItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCartItemDAO implements CartItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final ProductDao productDao;

    public JdbcCartItemDAO(JdbcTemplate jdbcTemplate, ProductDao productDao) {

        this.jdbcTemplate = jdbcTemplate;
        this.productDao = productDao;
    }


    @Override
    public List<CartItem> getCartItemsByUserId(int userId) {
        List<CartItem> cartItemList = new ArrayList<>();
        String sql = "SELECT * FROM cart_item WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            cartItemList.add(mapRowToCartItem(results));
        }
        return cartItemList;
    }


    @Override
    public void addItemToCart(CartItem cartItem) {
            jdbcTemplate.update(
                    "INSERT INTO cart_item " +
                            "(user_id, product_id, quantity) " +
                            "VALUES (?,?,?);",
                    cartItem.getUser_id(),
                    cartItem.getProductId(),
                    cartItem.getQuantity());
    }

    @Override
    public void removeItemFromCart(int productId, int userId) {
        jdbcTemplate.update(
                "DELETE FROM cart_item WHERE product_id = ? AND user_id= ?;",
                productId, userId
        );
    }

    @Override
    public CartItem getCartItem(int userId, int productId) {
        SqlRowSet results = jdbcTemplate.queryForRowSet(
                "SELECT * FROM cart_item WHERE user_id = ? AND product_id = ?;",
                userId, productId);

        if (results.next()) {
            return mapRowToCartItem(results);
        } else {
            return null;
        }

    }

    @Override
    public void updateQuantity(int productId, int userId, int quantity) {
        jdbcTemplate.update(
                "UPDATE cart_item " +
                        "SET quantity = quantity + ? " +
                        "WHERE user_id = ?" +
                        "AND product_id = ?;",
                quantity,
                userId,
                productId
        );
    }

    @Override
    public void clearCart(int userId) {
        jdbcTemplate.update(
                "DELETE FROM cart_item " +
                        "WHERE user_id = ?;",
                userId
        );
    }





    private CartItem mapRowToCartItem(SqlRowSet results) {
        CartItem cartItem = new CartItem();
        cartItem.setId(results.getInt("cart_item_id"));
        cartItem.setUser_id(results.getInt("user_id"));
        cartItem.setProductId(results.getInt("product_id"));
        cartItem.setQuantity(results.getInt("quantity"));
        cartItem.setProduct(productDao.getProductById(cartItem.getProductId()));
        return cartItem;
    }

}
