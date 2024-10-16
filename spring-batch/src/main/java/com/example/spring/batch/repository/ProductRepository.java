package com.example.spring.batch.repository;


import com.example.spring.batch.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Customers,Integer> {
}
