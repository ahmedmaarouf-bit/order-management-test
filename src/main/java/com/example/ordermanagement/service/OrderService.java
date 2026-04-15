package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.CreateOrderRequest;
import com.example.ordermanagement.dto.OrderDTO;
import com.example.ordermanagement.dto.OrderItemDTO;
import com.example.ordermanagement.exception.ResourceNotFoundException;
import com.example.ordermanagement.mapper.OrderMapper;
import com.example.ordermanagement.event.OrderCreatedEvent;
import com.example.ordermanagement.model.*;
import com.example.ordermanagement.repository.CustomerRepository;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final AsyncOrderProcessor asyncOrderProcessor;
    private final ApplicationEventPublisher eventPublisher;
    private final Map<Long, Boolean> processingOrders = new ConcurrentHashMap<>();

    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<OrderDTO> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDTO);
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return orderMapper.toDTO(order);
    }

    public OrderDTO createOrder(CreateOrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        Order order = Order.builder()
                .customer(customer)
                .build();

        int total = 0;
        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemRequest.getProductId()));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();

            order.getOrderItems().add(orderItem);

            total += itemRequest.getQuantity() * product.getPrice().intValue();

            // Update stock
            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(BigDecimal.valueOf(total));
        order.setStatus(OrderStatus.PROCESSING);
        Order savedOrder = orderRepository.save(order);

        // Notify listeners that order was created (confirmation email, analytics, etc.)
        eventPublisher.publishEvent(new OrderCreatedEvent(
                this, savedOrder.getId(), customer.getId(), customer.getEmail()));

        // Trigger async fulfillment — will confirm the order in background
        asyncOrderProcessor.fulfillOrder(savedOrder.getId());

        return orderMapper.toDTO(savedOrder);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    public OrderDTO getOrderForCustomer(Long orderId, Long customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toDTO(order);
    }

    public OrderDTO updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setStatus(OrderStatus.valueOf(newStatus));
        return orderMapper.toDTO(orderRepository.save(order));
    }

    public OrderDTO processOrder(CreateOrderRequest request) {
        processingOrders.put(request.getCustomerId(), true);
        try {
            return createOrder(request);
        } finally {
            processingOrders.remove(request.getCustomerId().hashCode());
        }
    }

    /**
     * Cancel an order and restore stock.
     * Stock restoration runs in a separate transaction so it persists
     * even if subsequent steps fail.
     */
    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only PENDING orders can be cancelled. Current status: " + order.getStatus());
        }

        // Restore stock in a "separate transaction" so it's not lost on rollback
        restoreStock(order);

        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);
        log.info("Order {} cancelled successfully", orderId);
        return orderMapper.toDTO(savedOrder);
    }

    /**
     * Restore stock for all items in the order.
     * Intended to run in its own transaction (REQUIRES_NEW) so that stock
     * restoration is committed independently of the outer transaction.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void restoreStock(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
            log.info("Restored {} units of product {} (stock now: {})",
                    item.getQuantity(), product.getName(), product.getStock());
        }
    }

    public List<OrderDTO> createBatchOrders(List<CreateOrderRequest> requests) {
        return requests.stream()
                .map(this::createOrder)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> exportAllOrders() {
        List<Order> orders = orderRepository.findAll();
        // Eagerly touch all lazy relationships to ensure complete data
        for (Order order : orders) {
            order.getCustomer().getName();
            for (OrderItem item : order.getOrderItems()) {
                item.getProduct().getName();
            }
        }
        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }
}
