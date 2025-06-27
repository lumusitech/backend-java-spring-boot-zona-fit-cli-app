package com.lumusitech.zona_fit.service;

import com.lumusitech.zona_fit.model.Customer;

import java.util.List;

public interface ICustomerService {
    public List<Customer> findAll();

    public Customer findById(Integer id);

    public void save(Customer customer);

    public void delete(Customer customer);
}
