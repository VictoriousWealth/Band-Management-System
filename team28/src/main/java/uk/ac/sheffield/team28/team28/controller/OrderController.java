package uk.ac.sheffield.team28.team28.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team28.team28.model.Order;
import uk.ac.sheffield.team28.team28.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController (OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{orderId}/changeOrderStatus")
    public ResponseEntity<Order> changeOrderStatus(@PathVariable Long orderId) {
        try {
            orderService.changeOrderStatus(orderId);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
