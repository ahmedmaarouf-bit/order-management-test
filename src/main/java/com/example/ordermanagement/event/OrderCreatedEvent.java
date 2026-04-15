package com.example.ordermanagement.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Published when a new order is created.
 * Listeners can use this to send confirmation emails,
 * update analytics, notify warehouse, etc.
 */
@Getter
public class OrderCreatedEvent extends ApplicationEvent {

    private final Long orderId;
    private final Long customerId;
    private final String customerEmail;

    public OrderCreatedEvent(Object source, Long orderId, Long customerId, String customerEmail) {
        super(source);
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerEmail = customerEmail;
    }
}
