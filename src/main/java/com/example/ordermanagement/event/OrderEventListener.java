package com.example.ordermanagement.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Handles order lifecycle events.
 * Sends notifications when orders are created.
 */
@Component
@Slf4j
public class OrderEventListener {

    /**
     * Sends order confirmation notification when a new order is created.
     * In production this would call an email service or push notification system.
     */
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info(">>> SENDING order confirmation email to {} for order #{}",
                event.getCustomerEmail(), event.getOrderId());

        // Simulate sending email/notification
        // In real app: emailService.sendOrderConfirmation(event.getCustomerEmail(), event.getOrderId());
        log.info(">>> SENT confirmation for order #{} to customer {}",
                event.getOrderId(), event.getCustomerId());
    }
}
