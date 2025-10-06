package org.example.backend2.unit.service;

import org.example.backend2.dto.OrderDTO;
import org.example.backend2.dto.OrderRequest;
import org.example.backend2.dto.ProductDTO;
import org.example.backend2.exceptions.UserNotFoundException;
import org.example.backend2.mapper.OrderMapper;
import org.example.backend2.models.AppUser;
import org.example.backend2.models.OrderItem;
import org.example.backend2.models.Product;
import org.example.backend2.models.ProductOrder;
import org.example.backend2.repository.OrderRepository;
import org.example.backend2.repository.ProductRepository;
import org.example.backend2.repository.UserRepository;
import org.example.backend2.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
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
    private ProductRepository productRepository;

    @Mock
    private SecurityContext securityContext;
    
    @Mock
    private OrderMapper orderMapper;
    
    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrderService orderService;

    private AppUser testUser;
    private ProductOrder testOrder1;
    private ProductOrder testOrder2;
    private OrderDTO testOrderDTO1;
    private OrderDTO testOrderDTO2;
    private Product product1;
    private Product product2;
    private OrderRequest orderRequest;
    private ProductDTO productDTO1;
    private ProductDTO productDTO2;

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

        product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Product 1");
        product1.setPrice(10.99);

        product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Product 2");
        product2.setPrice(25.50);
        
        productDTO1 = new ProductDTO();
        productDTO1.setProductId(1L);
        productDTO1.setQuantity(2);

        productDTO2 = new ProductDTO();
        productDTO2.setProductId(2L);
        productDTO2.setQuantity(1);
        
        orderRequest = new OrderRequest();
        orderRequest.setProducts(Arrays.asList(productDTO1, productDTO2));
    }

    @Test
    void testCompleteOrder_Success() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        when(orderRepository.save(any(ProductOrder.class))).thenAnswer(invocation -> {
            ProductOrder order = invocation.getArgument(0);
            order.setId(100L);
            return order;
        });
        
        boolean result = orderService.completeOrder(orderRequest);
        
        assertTrue(result);
        
        ArgumentCaptor<ProductOrder> orderCaptor = ArgumentCaptor.forClass(ProductOrder.class);
        verify(orderRepository).save(orderCaptor.capture());

        ProductOrder savedOrder = orderCaptor.getValue();
        assertNotNull(savedOrder);
        assertEquals(testUser, savedOrder.getAppUser());
        assertEquals(2, savedOrder.getOrderItems().size());
        
        OrderItem item1 = savedOrder.getOrderItems().get(0);
        assertEquals(product1, item1.getProduct());
        assertEquals(2, item1.getQuantity());
        assertEquals(10.99, item1.getPrice());
        assertEquals(savedOrder, item1.getOrder());
        
        OrderItem item2 = savedOrder.getOrderItems().get(1);
        assertEquals(product2, item2.getProduct());
        assertEquals(1, item2.getQuantity());
        assertEquals(25.50, item2.getPrice());
        assertEquals(savedOrder, item2.getOrder());
        
        verify(userRepository).findByUsername("testuser");
        verify(productRepository).findById(1L);
        verify(productRepository).findById(2L);
        verify(authentication).isAuthenticated();
        verify(authentication).getName();
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