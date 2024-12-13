package org.project;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Product product = new Product();
            product.setId((long) i);
            product.setDescription("Product " + i);
            product.setPrice(i * 10);
            product.setQuantity(i * 5);
            products.add(product);
        }
        products.forEach(System.out::println);
    }
}