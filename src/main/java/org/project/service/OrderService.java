package org.project.service;

import lombok.RequiredArgsConstructor;
import org.project.model.Order;
import org.project.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void createOrder(Order request) {
        Order order = new Order();
        order.setProductCode(request.getProductCode());
        order.setQuantity(request.getQuantity());
        order.setAmount(request.getAmount());
        order.setOrderDate(LocalDateTime.now());

        orderRepository.save(order);
    }
}