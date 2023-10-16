package com.example.bookstore.controller;

import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderRequest;
import com.example.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<String> addNewOrder(@RequestBody @NotNull OrderRequest orderRequest) {
        try {
            orderService.addNewOrder(orderRequest);
            return new ResponseEntity<>("Order successfully saved.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Order request failed!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity getOrderByUserId(@PathVariable String userId) {
        try {
            List<Order> order = orderService.getOrderByUserId(userId);
            return new ResponseEntity(order, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Order could not found!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity getOrderById(@RequestParam String orderId) {
        try {
            Order order = orderService.getOrderById(UUID.fromString(orderId));
            return new ResponseEntity(order, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Order could not found!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
