package org.example.backend2.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend2.dto.OrderDTO;
import org.example.backend2.dto.OrderRequest;
import org.example.backend2.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/order-form")
    public String showCheckout(Model model) {
        model.addAttribute("orderRequest", new OrderRequest());
        return "order-form";
    }

    @PostMapping("/order-form")
    public String completeCheckout(OrderRequest orderRequest) {
        boolean success = orderService.completeOrder(orderRequest);
        if (success) {
            return "redirect:/receipt";
        } else {
            return "order-form";
        }
    }
    
    @GetMapping("/orders")
    public String showOrders(Model model) {
        List<OrderDTO> orders = orderService.getAllOrders();
        model.addAttribute(orders);
        return "adminOrderList";
    }
    
}
