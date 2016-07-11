package com.cnu2016.assignment04.Controllers;

import com.cnu2016.assignment04.Models.product;
import java.util.*;

import com.cnu2016.assignment04.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ProductController extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("Executed");
        return true;
    }

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/api/products", method = RequestMethod.GET)
    public ResponseEntity productAllGet() {
        Iterable<product> result =  productRepository.findByEnabled(1);
        if(result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        Map<String, String> response = new HashMap<String, String>();
        response.put("details","Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @RequestMapping(value = "/api/products/{id}", method = RequestMethod.GET)
    public ResponseEntity productOneGet(@PathVariable("id") Integer id) {
        if(id == null){
            Map<String, String> response = new HashMap<String, String>();
            response.put("details","Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        product result =   productRepository.findByIdAndEnabled(id,1);
        if(result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        Map<String, String> response = new HashMap<String, String>();
        response.put("details","Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

    @RequestMapping(value = "/api/products", method = RequestMethod.POST)
    public ResponseEntity productAddPost(@RequestBody product Product) {
        if(Product.getCode() == null) {
            Map<String, String> response = new HashMap<String, String>();
            response.put("details","Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        productRepository.save(Product);
        return ResponseEntity.status(HttpStatus.CREATED).body(Product);
    }

    @RequestMapping(value = "/api/products/{id}", method = RequestMethod.PUT)
    public ResponseEntity productAddPut(@RequestBody product Product, @PathVariable("id") Integer id) {
        if(id == null || Product.getCode() == null) {
            Map<String, String> response = new HashMap<String, String>();
            response.put("details","Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        product productTemp =   productRepository.findByIdAndEnabled(id,1);
        if (productTemp == null) {
            Map<String, String> response = new HashMap<String, String>();
            response.put("details","Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } else {
            Product.setId(id);
            productRepository.save(Product);
            return ResponseEntity.status(HttpStatus.OK).body(Product);
        }
    }

    @RequestMapping(value = "/api/products/{id}", method = RequestMethod.PATCH)
    public ResponseEntity productAddPatch(@RequestBody product Product, @PathVariable("id") Integer id) {
        if(id == null) {
            Map<String, String> response = new HashMap<String, String>();
            response.put("details","Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        product productTemp =   productRepository.findByIdAndEnabled(id,1);
        if (productTemp == null) {
            Map<String, String> response = new HashMap<String, String>();
            response.put("details","Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } else {
            productTemp.update(Product);
            productRepository.save(productTemp);
            return ResponseEntity.status(HttpStatus.OK).body(productTemp);
        }
    }

    @RequestMapping(value = "/api/products/{id}", method = RequestMethod.DELETE)
    public ResponseEntity productDelete(@PathVariable("id") Integer id) {
        if(id == null) {
            Map<String, String> response = new HashMap<String, String>();
            response.put("details","Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        product productTemp =   productRepository.findByIdAndEnabled(id,1);
        if (productTemp == null) {
            Map<String, String> response = new HashMap<String, String>();
            response.put("details","Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            productTemp.setEnabled(0);
            productRepository.save(productTemp);
            return ResponseEntity.status(HttpStatus.OK).body(productTemp);
        }
    }

}