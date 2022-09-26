package com.example.ecomjwt.controller;

import com.example.ecomjwt.model.Product;
import com.example.ecomjwt.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value="/products",method = RequestMethod.GET)
    //@GetMapping(value="/products")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok().body(productService.getAllProduct());
        //return (List<Product>) productService.getAllProduct();
    }

    @RequestMapping( value="/product/{id_product}",method = RequestMethod.GET)
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable int id_product) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product/{id_product}").toUriString());
        return ResponseEntity.ok().body(productService.getProductById(id_product));
    }

    @RequestMapping(value="/products" , method = RequestMethod.POST)
    public ResponseEntity<Product> newProduct(@RequestBody Product product) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/products").toUriString());
        return ResponseEntity.created(uri).body(productService.newProduct(product));
    }

    @RequestMapping(value="/product/:id" , method = RequestMethod.PUT)
    public void updateProduct(@RequestBody Product product,@PathVariable int id_product){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/product/:id").toUriString());
        ResponseEntity.created(uri).body(productService.udpateProduct(product,id_product));
    }

    @RequestMapping(value="/product/:id", method = RequestMethod.DELETE)
    public void deleteProduct(@PathVariable int id_product){productService.deleteProduct(id_product);}

}
