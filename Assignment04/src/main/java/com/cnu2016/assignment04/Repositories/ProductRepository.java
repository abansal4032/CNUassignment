package com.cnu2016.assignment04.Repositories;

import org.springframework.data.repository.CrudRepository;
import com.cnu2016.assignment04.Models.product;


public interface ProductRepository extends CrudRepository<product, Integer> {
    product findByIdAndEnabled(Integer productId, Integer Enabled);
    Iterable<product> findByEnabled(Integer Enabled);
}