package org.example.backend2.service;

import lombok.RequiredArgsConstructor;
import org.example.backend2.dto.OrderDTO;
import org.example.backend2.dto.OrderRequest;
import org.example.backend2.dto.ProductDTO;
import org.example.backend2.exceptions.ProductNotFoundException;
import org.example.backend2.exceptions.UserNotFoundException;
import org.example.backend2.mapper.OrderMapper;
import org.example.backend2.models.AppUser;
import org.example.backend2.models.OrderItem;
import org.example.backend2.models.Product;
import org.example.backend2.models.ProductOrder;
import org.example.backend2.repository.OrderRepository;
import org.example.backend2.repository.ProductRepository;
import org.example.backend2.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public boolean completeOrder(OrderRequest orderRequest, String userName) {
        try {
            if (orderRequest.getProducts() == null || orderRequest.getProducts().isEmpty()) {
                throw new IllegalArgumentException("Order must contain at least one product");
            }

            AppUser user = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + userName));

            ProductOrder order = new ProductOrder();
            order.setAppUser(user);

            for (ProductDTO productDTO : orderRequest.getProducts()) {
                if (productDTO.getQuantity() <= 0) {
                    throw new IllegalArgumentException("Invalid quantity for product: " + productDTO.getProductId());
                }

                Product product = productRepository.findById(productDTO.getProductId())
                        .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productDTO.getProductId()));

                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProduct(product);
                item.setQuantity(productDTO.getQuantity());

                order.getOrderItems().add(item);
            }

            orderRepository.save(order);
            return true;

        } catch (Exception e) {

            throw e; 
        }
    }
    
    
    public List<OrderDTO> getOrdersByUsername(String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        List<ProductOrder> orders = orderRepository.findByAppUser(user);
        return OrderMapper.multipleToDTO(orders);
    }

    public List<OrderDTO> getAllOrders() {
        List<ProductOrder> orders = orderRepository.findAll();
        
        return OrderMapper.multipleToDTO(orders);
    }
}
