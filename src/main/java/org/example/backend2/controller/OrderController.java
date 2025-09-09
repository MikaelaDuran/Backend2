package org.example.backend2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend2.dto.OrderDTO;
import org.example.backend2.dto.OrderRequest;
import org.example.backend2.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String completeCheckout(@Valid @RequestBody OrderRequest orderRequest,
                                   BindingResult bindingResult,
                                   Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("orderRequest", orderRequest);
            return "order-form";
        }

        boolean success = orderService.completeOrder(orderRequest);
        if (success) {
            return "redirect:/order-confirmation";
        } else {
            model.addAttribute("orderRequest", orderRequest);
            return "order-form";
        }
    }
    
    @GetMapping("/orders")
    public String showOrders(Model model) {
        List<OrderDTO> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "adminOrderList";
    }

    @PostMapping("/admin/orders/{id}/delete")
    public String deleteOrderById(@PathVariable long id) {
        orderService.deleteById(id);
        return "adminOrderList";
    }
    
    @GetMapping("/order-confirmation")
    public String showOrderConfirmation(Model model) {
        return "order-confirmation";
    }
}
