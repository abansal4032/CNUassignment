package com.cnu2016.assignment04.Controllers;

import com.cnu2016.assignment04.Models.*;

import com.cnu2016.assignment04.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cnu2016.assignment04.CheckOut;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @RequestMapping(value = "/api/health", method = RequestMethod.GET)
    public ResponseEntity healthCheck() {
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    /* Create Order */
    @RequestMapping(value = "/api/order", method = RequestMethod.POST)
    public ResponseEntity orderCreatePost() {
        order newOrder = new order();
        newOrder = orderRepository.save(newOrder);
        Map<String, Integer> response = new HashMap<String, Integer>();
        response.put("orderId",newOrder.getOrderId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /* Checkout and Complete Order */
    @RequestMapping(value = "/api/order/{id}", method = RequestMethod.PATCH)
    public ResponseEntity placeOrder(@RequestBody CheckOut checkOut, @PathVariable("id") Integer id) {
        user tempUser = userRepository.findUniqueByCustomerName(checkOut.getUserName());
        if(id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        if (tempUser == null) {
            tempUser = new user();
            tempUser.setCustomerName(checkOut.getUserName());
            tempUser.setAddressLine1(checkOut.getAddress());
            tempUser = userRepository.save(tempUser);
        }
        Iterable<orderLine> orderLineList = orderLineRepository.findAll();
        Float totalSum = new Float(0);
        for (orderLine item : orderLineList) {
            if(item.getId().getOrder().getOrderId().equals(id)){
                totalSum += item.getPrice()*item.getQuantity();
                product tempProduct = item.getId().getProduct();
                Integer quantityInStock = tempProduct.getQuantityInStock();
                Integer quantityAsked = item.getQuantity();
                if(quantityInStock - quantityAsked < 0)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
                tempProduct.setQuantityInStock(quantityInStock - quantityAsked);
                tempProduct = productRepository.save(tempProduct);
            }
        }
        order tempOrder = orderRepository.findOne(id);
        tempOrder.setUser(tempUser);
        tempOrder.setStatus("CheckOut");
        tempOrder = orderRepository.save(tempOrder);
        Map<String, String> response = new HashMap<String, String>();
        response.put("status","Placed");
        response.put("totalAmount", totalSum.toString());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}