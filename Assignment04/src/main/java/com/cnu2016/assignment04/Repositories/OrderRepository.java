package com.cnu2016.assignment04.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.cnu2016.assignment04.Models.order;


public interface OrderRepository extends CrudRepository<order, Integer> {
    order findByOrderIdAndEnabled(Integer orderId, Integer Enabled);
    Iterable<order> findByEnabled(Integer Enabled);
}