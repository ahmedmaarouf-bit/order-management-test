package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.CreateOrderRequest;
import com.example.ordermanagement.exception.ResourceNotFoundException;
import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.model.OrderItem;
import com.example.ordermanagement.model.Product;
import com.example.ordermanagement.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setCategory(productDetails.getCategory());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public void updateStock(List<StockUpdateRequest> updates) {
        for (StockUpdateRequest update : updates) {
            Product product = getProductById(update.getProductId());
            product.setStock(product.getStock() + update.getQuantityChange());
            productRepository.save(product);
            log.info("Updated stock for product {}: new stock = {}", product.getId(), product.getStock());
        }
    }

    public List<Product> searchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .filter(p -> name == null || p.getName().equals(name))
                .filter(p -> category == null || category.equals(p.getCategory()))
                .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
                .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
                .collect(java.util.stream.Collectors.toList());
    }

    public Product updatePrice(Long productId, BigDecimal newPrice) {
        Product product = getProductById(productId);
        product.setPrice(newPrice);
        return productRepository.save(product);
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class StockUpdateRequest {
        private Long productId;
        private Integer quantityChange;
    }
}