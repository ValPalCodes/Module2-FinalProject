package com.techelevator.controller;


import com.techelevator.dao.CartItemDao;
import com.techelevator.dao.ProductDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.CartDto;
import com.techelevator.model.CartItem;
import com.techelevator.model.TaxResponseDto;
import com.techelevator.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")
public class CartController {
    private CartItemDao cartItemDao;
    private UserDao userDao;
    private ProductDao productDao;

    public CartController(CartItemDao cartItemDao, UserDao userDao, ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @RequestMapping(path="", method = RequestMethod.GET)
    public CartDto getCart(Principal principal){
        CartDto cart = new CartDto();
        String username = principal.getName();
        User user = userDao.findByUsername(username);
        int userId = user.getId();
        String userState = user.getStateCode();
        List<CartItem> cartItems = cartItemDao.getCartItemsByUserId(userId);
        cart.setCartItemsList(cartItems);

        BigDecimal subtotal = BigDecimal.valueOf(0);
        for (CartItem cartItem : cartItems) {
            subtotal = subtotal.add(
                    cartItem.getProduct().getPrice().multiply(
                            new BigDecimal(cartItem.getQuantity())
                    )
            );
        }
        cart.setSubtotal(subtotal);

        RestTemplate restTemplate = new RestTemplate();
        TaxResponseDto responseDto = restTemplate.getForObject("https://teapi.netlify.app/api/statetax?state=" + userState,
                TaxResponseDto.class);

        BigDecimal taxRate = responseDto.getSalesTax().divide(new BigDecimal(100));
        BigDecimal tax = subtotal.multiply(taxRate);

        cart.setTaxAmount(tax);

        BigDecimal total = subtotal.add(tax);

        cart.setTotal(total);

        return cart;
    }


    @RequestMapping(path="/items", method = RequestMethod.POST)
    public void addItemToCart(@RequestBody CartItem cartItem, Principal principal) {
        String username = principal.getName();
        int userId = userDao.findIdByUsername(username);
        cartItem.setUser_id(userId);
        if(cartItem.getQuantity() < 0) {
            cartItem.setQuantity(0);
        }
        CartItem existingCartItem = cartItemDao.getCartItem(cartItem.getUser_id(), cartItem.getProductId());
        if(existingCartItem != null) {
            cartItemDao.updateQuantity(cartItem.getProductId(), cartItem.getUser_id(), cartItem.getQuantity());
        } else {
            cartItemDao.addItemToCart(cartItem);
        }
    }

    @RequestMapping(path="/items/{productId}", method = RequestMethod.DELETE)
    public void deleteItemFromCart(@PathVariable int productId, Principal principal) {
        String username = principal.getName();
        int userId = userDao.findIdByUsername(username);
        cartItemDao.removeItemFromCart(productId, userId);
    }

    @RequestMapping(path="", method = RequestMethod.DELETE)
    public void clearCart(Principal principal){
        String username = principal.getName();
        int userId = userDao.findIdByUsername(username);
        cartItemDao.clearCart(userId);
    }





}
