package com.techelevator.dao;

import com.techelevator.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CartItemDao {

    List<CartItem> getCartItemsByUserId(int userId);

    void addItemToCart(CartItem cartItem);

    void removeItemFromCart(int productId, int userId);

    CartItem getCartItem(int userId, int productId);

    void updateQuantity(int productId, int userId, int quantity);

    void clearCart(int userId);
}
