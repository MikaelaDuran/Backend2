package org.example.backend2.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend2.dto.OrderDTO;
import org.example.backend2.dto.OrderRequest;
import org.example.backend2.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public String completeCheckout(@RequestBody OrderRequest orderRequest) {
        String userName = getCurrentUsername();
        boolean success = orderService.completeOrder(orderRequest, userName);
        if (success) {
            return "redirect:/order-confirmation";
        } else {
            return "order-form";
        }
    }
    
    @GetMapping("/orders")
    public String showOrders(Model model) {
        List<OrderDTO> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "adminOrderList";
    }

    @GetMapping("/order-confirmation")
    public String showOrderConfirmation(Model model) {
        return "order-confirmation";
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
