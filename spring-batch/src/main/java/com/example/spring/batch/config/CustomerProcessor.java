package com.example.spring.batch.config;

import com.example.spring.batch.entity.Customers;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customers,Customers> {
    @Override
    public Customers process(Customers customers) throws Exception {
//        if(customers.getCountry().equals("United States")) {
//            return customers;
//        }else{
//            return null;

        return  customers;
//        }
    }
}
