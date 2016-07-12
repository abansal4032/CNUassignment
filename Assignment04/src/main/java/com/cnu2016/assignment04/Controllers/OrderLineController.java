package com.cnu2016.assignment04.Controllers;

import com.cnu2016.assignment04.Models.*;
import java.util.*;

import com.cnu2016.assignment04.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.cnu2016.assignment04.AddProduct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class OrderLineController extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("Executed");
        return true;
    }

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    /* Add Product */
    @RequestMapping(value = "/api/orders/{id}/orderLineItem", method = RequestMethod.POST)
    public ResponseEntity productAddPost(@RequestBody AddProduct addProduct, @PathVariable("id") Integer id) {
        if(id == null || addProduct == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        Integer productId = addProduct.getProductId();
        Integer quantity = addProduct.getQuantity();
        product tempProduct = productRepository.findByIdAndEnabled(productId, 1);
        if(quantity == null || tempProduct == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        //order tempOrder = orderRepository.findOne(id);//ByIdAndEnabled(id, 1);
        order tempOrder = orderRepository.findByOrderIdAndEnabled(id, 1);
        if(tempOrder == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        Integer remainingQuantity = tempProduct.getQuantityInStock() - quantity;
        //if(remainingQuantity < 0)
        //    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        Float price = tempProduct.getBuyPrice();
        orderLine tempOrderLine = new orderLine();
        orderLineId tempOrderLineId = new orderLineId();
        tempOrderLineId.setProduct(tempProduct);
        tempOrderLineId.setOrder(tempOrder);
        tempOrderLine.setId(tempOrderLineId);
        tempOrderLine.setPrice(price);
        tempOrderLine.setQuantity(quantity);
        orderLineRepository.save(tempOrderLine);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }



}