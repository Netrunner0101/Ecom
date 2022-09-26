package com.example.ecomjwt.service;

import com.example.ecomjwt.model.Product;
import com.example.ecomjwt.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ProductService {

    @Autowired
    ProductRepository productRepo;

    public List<Product> getAllProduct(){
        return productRepo.findAll();
    }

    public Optional<Product> getProductById(int id_product){
        return productRepo.findById(id_product);
    }

    public Product newProduct(Product product){
        log.info("New product create {}",product);
        return productRepo.save(product);
    }

    public Product udpateProduct(Product productDetails, int id_product){
        Product productUpdate = (Product) productRepo.findById(id_product).get();
        productUpdate.setName(productDetails.getName());
        productUpdate.setDescription(productDetails.getDescription());
        productUpdate.setPrice(productDetails.getPrice());
        return productRepo.save(productUpdate);
    }

    public void deleteProduct(int id_product){
        productRepo.deleteById(id_product);
    }


}
