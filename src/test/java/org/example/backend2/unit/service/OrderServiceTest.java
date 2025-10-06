package org.example.backend2.unit.service;

import org.example.backend2.dto.OrderDTO;
import org.example.backend2.exceptions.UserNotFoundException;
import org.example.backend2.models.AppUser;
import org.example.backend2.models.ProductOrder;
import org.example.backend2.repository.OrderRepository;
import org.example.backend2.repository.UserRepository;
import org.example.backend2.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrderService orderService;

    private AppUser testUser;
    private ProductOrder testOrder1;
    private ProductOrder testOrder2;
    private OrderDTO testOrderDTO1;
    private OrderDTO testOrderDTO2;

    @BeforeEach
    void setUp() {
        testUser = new AppUser();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testOrder1 = new ProductOrder();
        testOrder1.setId(1L);
        testOrder1.setAppUser(testUser);

        testOrder2 = new ProductOrder();
        testOrder2.setId(2L);
        testOrder2.setAppUser(testUser);

        testOrderDTO1 = new OrderDTO();
        testOrderDTO1.setOrderId(1L);

        testOrderDTO2 = new OrderDTO();
        testOrderDTO2.setOrderId(2L);
    }

    @Test
    void testGetCurrentUsername_WhenAuthenticated() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        
        String result = orderService.getCurrentUsername();
        
        assertEquals("testuser", result);
        verify(authentication).isAuthenticated();
        verify(authentication).getName();
    }

    @Test
    void testGetCurrentUsername_WhenNotAuthenticated() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        
        String result = orderService.getCurrentUsername();
        
        assertNull(result);
        verify(authentication).isAuthenticated();
        verify(authentication, never()).getName();
    }

    @Test
    void testGetOrdersByUsername_UserNotFound() {
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> orderService.getOrdersByUsername(username)
        );

        assertEquals("User not found: " + username, exception.getMessage());
        verify(userRepository).findByUsername(username);
        verify(orderRepository, never()).findByAppUser(any());
    }

    @Test
    void completeOrder() {
    }



    @Test
    void testGetAllOrders_Success() {
        List<ProductOrder> orders = Arrays.asList(testOrder1, testOrder2);
        when(orderRepository.findAll()).thenReturn(orders);
        
        List<OrderDTO> result = orderService.getAllOrders();
        
        assertNotNull(result);
        verify(orderRepository).findAll();
    }

    @Test
    void testGetAllOrders_EmptyList() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());
        
        List<OrderDTO> result = orderService.getAllOrders();
        
        assertNotNull(result);
        verify(orderRepository).findAll();
    }

    @Test
    void testDeleteById_Success() {
        long orderId = 1L;
        doNothing().when(orderRepository).deleteById(orderId);
        
        orderService.deleteById(orderId);
        
        verify(orderRepository).deleteById(orderId);
    }

    @Test
    void testDeleteById_NonExistentId() {
        long orderId = 999L;
        doThrow(new IllegalArgumentException("Order not found"))
                .when(orderRepository).deleteById(orderId);
        
        assertThrows(
                IllegalArgumentException.class,
                () -> orderService.deleteById(orderId)
        );

        verify(orderRepository).deleteById(orderId);
    }
    
}