package com.manning.mss.ch02.sample03.service;

import com.manning.mss.ch02.sample03.exceptions.OrderNotFoundException;
import com.manning.mss.ch02.sample03.orderentity.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EnableWebSecurity
@RestController
@RequestMapping("/orders")
@EnableMethodSecurity
public class OrderProcessingService {

    private final Map<String, Order> orders = new HashMap<>();

    @PostMapping
    public ResponseEntity<Order> placeOrder(@AuthenticationPrincipal Jwt jwt, @RequestBody Order order) {

        System.out.println("User : " + jwt.getSubject());
        System.out.println("Received Order For " + order.getItems().size() + " Items");
        order.getItems().forEach((lineItem) -> System.out.println("Item: " + lineItem.getItemCode() +
                " Quantity: " + lineItem.getQuantity()));

        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);
        orders.put(orderId, order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@AuthenticationPrincipal Jwt jwt, @PathVariable String id) throws OrderNotFoundException {

        System.out.println("User : " + jwt.getSubject());
        if(orders.containsKey(id)){
            return new ResponseEntity<>(orders.get(id), HttpStatus.OK);
        } else {
           throw new OrderNotFoundException();
        }
    }

    @PostMapping("/with-role-security")
    @PreAuthorize("hasAuthority('SCOPE_items.write')")
    public ResponseEntity<Order> placeOrderWithRoleBasedSecurity(@RequestBody Order order) {

        var jwt = (Jwt)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println("User : " + jwt.getSubject());
        System.out.println("SecurityContextHolder = " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("Received Order For " + order.getItems().size() + " Items");
        order.getItems().forEach((lineItem) -> System.out.println("Item: " + lineItem.getItemCode() +
                " Quantity: " + lineItem.getQuantity()));

        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);
        orders.put(orderId, order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
