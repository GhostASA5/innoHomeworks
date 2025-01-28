package org.project.api;

import lombok.RequiredArgsConstructor;
import org.project.model.Order;
import org.project.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody Order order) {
        if(order.getQuantity() <= 0 || order.getAmount() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        orderService.createOrder(order);
        return ResponseEntity.ok().build();
    }
}
