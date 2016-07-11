package com.cnu2016.assignment04.Repositories;

import org.springframework.data.repository.CrudRepository;
import com.cnu2016.assignment04.Models.user;


public interface UserRepository extends CrudRepository<user, Integer> {
    user findUniqueByCustomerName(String customerName);
}