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
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Returs the details of all the products
     * @return The json containing all the product details of all the products
     */
    @RequestMapping(value = "/api/products", method = RequestMethod.GET)
    public ResponseEntity productAllGet() {
        Iterable<product> result =  productRepository.findByEnabled(1);
            return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Returs the details of a specific product
     * @param id The productid of the product asked for
     * @return The json containing the product details the product if present else a NOT_FOUND
     */
    @RequestMapping(value = "/api/products/{id}", method = RequestMethod.GET)
    public ResponseEntity productOneGet(@PathVariable("id") Integer id) {
        product result =   productRepository.findByIdAndEnabled(id,1);
        if(result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        Map<String, String> response = new HashMap<String, String>();
        response.put("details","Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

    /**
     * Creates a new product
     * @param Product The product details of the product to be added
     * @return The added product if success else a NOT_FOUND
     */

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

    /**
     * Edit all the fields of a product
     * @param Product The product details of the product to be changed
     * @param id The is of the product to edited
     * @return The updated Product object if success else a NOT_FOUND
     */
    @RequestMapping(value = "/api/products/{id}", method = RequestMethod.PUT)
    public ResponseEntity productAddPut(@RequestBody product Product, @PathVariable("id") Integer id) {
        if(Product.getCode() == null) {
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

    /**
     * Edit some fields of a product
     * @param Product The product details of the product to be changed
     * @param id The is of the product to edited
     * @return The updated Product object if success else a NOT_FOUND
     */
    @RequestMapping(value = "/api/products/{id}", method = RequestMethod.PATCH)
    public ResponseEntity productAddPatch(@RequestBody product Product, @PathVariable("id") Integer id) {
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

    /**
     * Delete a product
     * @param id The id of the product
     * @return The details of the product or NOT_FOUND if already deleted
     */


    @RequestMapping(value = "/api/products/{id}", method = RequestMethod.DELETE)
    public ResponseEntity productDelete(@PathVariable("id") Integer id) {
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