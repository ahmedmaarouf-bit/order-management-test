package com.example.ordermanagement.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Long id;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private LocalDate orderDate;
    private String status;
    private BigDecimal totalAmount;
    private List<OrderItemDTO> items;
}