package org.project.managment;

import io.micrometer.core.instrument.MeterRegistry;
import org.project.model.Order;
import org.project.repository.OrderRepository;
import org.springframework.stereotype.Component;


@Component
public class OrderMetrics {

    private final OrderRepository orderRepository;

    public OrderMetrics(MeterRegistry registry, OrderRepository orderRepository) {
        this.orderRepository = orderRepository;

        registry.gauge("order.count", this, OrderMetrics::getOrdersCount);
        registry.gauge("order.average.amount", this, OrderMetrics::getOrdersAverage);
    }

    public Long getOrdersCount() {
        return orderRepository.count();
    }

    public Double getOrdersAverage() {
        return orderRepository.findAll()
                .stream()
                .mapToDouble(Order::getAmount)
                .average()
                .orElse(0.0);
    }
}
