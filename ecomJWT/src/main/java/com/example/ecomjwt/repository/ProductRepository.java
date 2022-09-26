package com.example.ecomjwt.repository;

import com.example.ecomjwt.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ProductRepository  extends JpaRepository<Product,Integer> {
}
