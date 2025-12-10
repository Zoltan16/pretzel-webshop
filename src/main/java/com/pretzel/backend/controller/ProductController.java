package com.pretzel.backend.controller;

import com.pretzel.backend.model.Product;
import com.pretzel.backend.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Product> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Product> getById(@PathVariable Long id) {
        return repo.findById(id);
    }

    @GetMapping("/type/{type}")
    public List<Product> getByType(@PathVariable String type) {
        return repo.findByType(type);
    }
}
