package com.example.ordermanagement.service;

import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.model.OrderStatus;
import com.example.ordermanagement.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Handles asynchronous order fulfillment processing.
 * After an order is created, this processor validates inventory,
 * reserves items, and transitions the order to CONFIRMED.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncOrderProcessor {

    private final OrderRepository orderRepository;

    /**
     * Asynchronously fulfills an order. This runs in a separate thread
     * to avoid blocking the API response.
     *
     * Validates the order, simulates warehouse processing, and updates status.
     */
    @Async
    public void fulfillOrder(Long orderId) {
        log.info("Starting async fulfillment for order {}", orderId);

        // Simulate warehouse processing time
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // Validation: orders with more than 3 items require manual review
        if (order.getOrderItems().size() > 3) {
            throw new RuntimeException(
                    "Order " + orderId + " has " + order.getOrderItems().size() +
                    " items — requires manual fulfillment review");
        }

        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
        log.info("Order {} fulfilled successfully", orderId);
    }
}
