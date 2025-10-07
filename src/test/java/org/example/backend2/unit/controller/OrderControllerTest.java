package org.example.backend2.unit.controller;

import org.example.backend2.controller.OrderController;
import org.example.backend2.dto.OrderDTO;
import org.example.backend2.dto.OrderRequest;
import org.example.backend2.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private OrderController orderController;

    @Test
    void completeCheckout_WithValidationErrors_ReturnsFormView() {
        OrderRequest orderRequest = new OrderRequest();
        when(bindingResult.hasErrors()).thenReturn(true);
        
        String viewName = orderController.completeCheckout(orderRequest, bindingResult, model);
        
        assertEquals("order-form", viewName);
        verify(model).addAttribute("orderRequest", orderRequest);
        verify(orderService, never()).completeOrder(any());
    }

    @Test
    void completeCheckout_WithSuccessfulOrder_RedirectsToConfirmation() {
        OrderRequest orderRequest = new OrderRequest();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(orderService.completeOrder(orderRequest)).thenReturn(true);
        
        String viewName = orderController.completeCheckout(orderRequest, bindingResult, model);
        
        assertEquals("redirect:/order-confirmation", viewName);
        verify(orderService).completeOrder(orderRequest);
        verify(model, never()).addAttribute(any(), any());
    }

    @Test
    void completeCheckout_WithFailedOrder_ReturnsFormView() {
        OrderRequest orderRequest = new OrderRequest();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(orderService.completeOrder(orderRequest)).thenReturn(false);

        String viewName = orderController.completeCheckout(orderRequest, bindingResult, model);
        
        assertEquals("order-form", viewName);
        verify(orderService).completeOrder(orderRequest);
        verify(model).addAttribute("orderRequest", orderRequest);
    }

    @Test
    void showOrders_AddsOrdersToModelAndReturnsCorrectView() {
        List<OrderDTO> orders = List.of(new OrderDTO(), new OrderDTO());
        when(orderService.getAllOrders()).thenReturn(orders);
        
        String viewName = orderController.showOrders(model);
        
        assertEquals("adminOrderList", viewName);
        verify(model).addAttribute("orders", orders);
        verify(orderService).getAllOrders();
    }

    @Test
    void deleteOrderById_CallsServiceAndRedirects() {
        long orderId = 123L;
        
        String viewName = orderController.deleteOrderById(orderId);
        
        assertEquals("redirect:/orders", viewName);
        verify(orderService).deleteById(orderId);
    }

    @Test
    void showCheckout_AddsOrderRequestToModelAndReturnsFormView() {
        ArgumentCaptor<OrderRequest> orderRequestCaptor = ArgumentCaptor.forClass(OrderRequest.class);
        
        String viewName = orderController.showCheckout(model);
        
        assertEquals("order-form", viewName);
        verify(model).addAttribute(eq("orderRequest"), orderRequestCaptor.capture());
        assertNotNull(orderRequestCaptor.getValue());
    }

    @Test
    void showOrderConfirmation_ReturnsConfirmationView() {
        String viewName = orderController.showOrderConfirmation(model);
        
        assertEquals("order-confirmation", viewName);
        verifyNoInteractions(model);
        verifyNoInteractions(orderService);
    }
}