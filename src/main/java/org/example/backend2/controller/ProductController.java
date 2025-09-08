package org.example.backend2.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend2.models.Product;
import org.example.backend2.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products/{category}")
    public String getProductsByCategory(@PathVariable String category, Model model) {
        List<Product> products = productService.getProductsByCategory(category);
        model.addAttribute(products);
        return "";
    }
    
    @GetMapping("/products/{id}")
    public String getProductById(@PathVariable long id, Model model)
    {
        Product product = productService.getProductById(id);
        model.addAttribute(product);
        return "";
    }

    @GetMapping("/products/all")
    public String getAllProducts( Model model)
    {
        List<Product> products = productService.getAllProducts();
        model.addAttribute(products);
        return "";
    }
    
    
}
