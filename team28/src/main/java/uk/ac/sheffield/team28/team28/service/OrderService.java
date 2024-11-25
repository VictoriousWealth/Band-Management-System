package uk.ac.sheffield.team28.team28.service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.model.Order;
import uk.ac.sheffield.team28.team28.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void changeOrderStatus(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new Exception("Order not found with ID: " + orderId));
        order.setIsFulfilled(true);
    }

}
