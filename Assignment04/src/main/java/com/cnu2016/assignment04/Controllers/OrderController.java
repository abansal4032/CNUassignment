package com.cnu2016.assignment04.Controllers;

import com.cnu2016.assignment04.Models.*;

import com.cnu2016.assignment04.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cnu2016.assignment04.CheckOut;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * Performs health check for the server
     */

    @RequestMapping(value = "/api/health", method = RequestMethod.GET)
    public ResponseEntity healthCheck() {
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    /**
     * Returs the details of all the orders placed
     * @return The json containing all the order details of all the orders
     */
    @RequestMapping(value = "/api/orders", method = RequestMethod.GET)
    public ResponseEntity orderAllGet() {
        Iterable<order> result =  orderRepository.findByEnabled(1);
            return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Returs the details of a specific order placed
     * @param id The orderid of the order asked for
     * @return The json containing the order details the order if present else a NOT_FOUND
     */
    @RequestMapping(value = "/api/orders/{id}", method = RequestMethod.GET)
    public ResponseEntity orderOneGet(@PathVariable("id") Integer id) {
        order result =   orderRepository.findByOrderIdAndEnabled(id,1);
        if(result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        Map<String, String> response = new HashMap<String, String>();
        response.put("details","Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }


    /**
     * Creates a new order
     * @return The orderid of the newly created order
     */
    /* Create Order */
    @RequestMapping(value = "/api/orders", method = RequestMethod.POST)
    public ResponseEntity orderCreatePost() {
        order newOrder = new order();
        newOrder = orderRepository.save(newOrder);
        Map<String, Integer> response = new HashMap<String, Integer>();
        response.put("id",newOrder.getOrderId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** Checkout the order after adding products to it
     * @param id The orderid to checkout
     * @param checkOut the username and address of the user placing the order
     * @return The details of the order placed if OK else a blank body
     */

    /* Checkout and Complete Order */
    @RequestMapping(value = "/api/orders/{id}", method = RequestMethod.PATCH)
    public ResponseEntity placeOrder(@RequestBody CheckOut checkOut, @PathVariable("id") Integer id) {
        if(checkOut == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        if(checkOut.getUserName() == null || checkOut.getAddress() == null || checkOut.getStatus() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        user tempUser = new user();
        tempUser.setCustomerName(checkOut.getUserName());
        tempUser.setAddressLine1(checkOut.getAddress());
        tempUser = userRepository.save(tempUser);
        if(id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        Iterable<orderLine> orderLineList = orderLineRepository.findAll();
        Float totalSum = new Float(0);
        for(orderLine item : orderLineList) {
            if(item.getId().getOrder().getOrderId().equals(id)) {
                product tempProduct = item.getId().getProduct();
                Integer tempProductAvailable = item.getId().getProduct().getQuantityInStock();
                if(tempProductAvailable == null || tempProductAvailable <= 0 )
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
                Integer quantityInStock = tempProduct.getQuantityInStock();
                Integer quantityAsked = item.getQuantity();
                if(quantityInStock == null || quantityAsked == null || quantityInStock - quantityAsked < 0)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
            }
        }
        for (orderLine item : orderLineList) {
            if(item.getId().getOrder().getOrderId().equals(id)){
                product tempProduct = item.getId().getProduct();
                Integer tempProductAvailable = item.getId().getProduct().getQuantityInStock();
                if(tempProductAvailable == null || tempProductAvailable <= 0 )
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
                Integer quantityInStock = tempProduct.getQuantityInStock();
                Integer quantityAsked = item.getQuantity();
                if(quantityInStock == null || quantityAsked == null || quantityInStock - quantityAsked < 0)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
                tempProduct.setQuantityInStock(quantityInStock - quantityAsked);
                tempProduct = productRepository.save(tempProduct);
            }
        }
        order tempOrder = orderRepository.findByOrderIdAndEnabled(id, 1);
        if(tempOrder == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        tempOrder.setUser(tempUser);
        tempOrder.setStatus("CheckOut");
        tempOrder = orderRepository.save(tempOrder);
        Map<String, String> response = new HashMap<String, String>();
        response.put("status","Placed");
        response.put("totalAmount", totalSum.toString());
        return ResponseEntity.status(HttpStatus.OK).body(tempOrder);
    }

    /**
     * Delete a placed order
     * @param id The id of the placed order
     * @return The detials of the object placed or NOT_FOUND if already deleted
     */

    @RequestMapping(value = "/api/orders/{id}", method = RequestMethod.DELETE)
    public ResponseEntity orderDelete(@PathVariable("id") Integer id) {
        order orderTemp =   orderRepository.findByOrderIdAndEnabled(id,1);
        if (orderTemp == null) {
            Map<String, String> response = new HashMap<String, String>();
            response.put("details","Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            orderTemp.setEnabled(0);
            orderTemp = orderRepository.save(orderTemp);
            return ResponseEntity.status(HttpStatus.OK).body(orderTemp);
        }
    }

}