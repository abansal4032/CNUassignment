package com.cnu2016.assignment04.Repositories;

import com.cnu2016.assignment04.Models.orderLineId;
import org.springframework.data.repository.CrudRepository;
import com.cnu2016.assignment04.Models.*;


public interface FeedbackRepository extends CrudRepository<feedback, Integer> {

}