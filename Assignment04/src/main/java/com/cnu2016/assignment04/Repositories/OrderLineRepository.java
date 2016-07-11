package com.cnu2016.assignment04.Repositories;

import com.cnu2016.assignment04.Models.orderLineId;
import org.springframework.data.repository.CrudRepository;
import com.cnu2016.assignment04.Models.orderLine;


public interface OrderLineRepository extends CrudRepository<orderLine, orderLineId> {

}