package com.hutech.trananhtien.service;

import com.hutech.trananhtien.model.CartItem;
import com.hutech.trananhtien.model.Customer;
import com.hutech.trananhtien.model.Order;
import com.hutech.trananhtien.model.OrderDetail;
import com.hutech.trananhtien.repository.OrderDetailRepository;
import com.hutech.trananhtien.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartService cartService;
    private final PointService pointService;

    public Order createOrder(Order order, List<CartItem> cartItems, int pointsToUse) {
        double totalAmount = cartItems.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
        order.setTotalAmount(totalAmount);

        Customer customer = pointService.findOrCreateCustomer(
                order.getCustomerName(),
                order.getPhone(),
                order.getEmail(),
                order.getAddress());
        order.setCustomer(customer);

        int actualPointsUsed = 0;
        double discountAmount = 0;
        if (customer != null && pointsToUse > 0) {
            actualPointsUsed = Math.min(pointsToUse, customer.getPoints());
            discountAmount = Math.min(totalAmount, pointService.calculateDiscountFromPoints(actualPointsUsed));
            pointService.usePoints(customer, actualPointsUsed);
        }

        order.setPointsUsed(actualPointsUsed);
        order.setDiscountAmount(discountAmount);

        int earnedPoints = pointService.calculateEarnedPoints(Math.max(0, totalAmount - discountAmount));
        order.setPointsEarned(earnedPoints);

        Order savedOrder = orderRepository.save(order);
        List<OrderDetail> details = new ArrayList<>();

        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            details.add(orderDetailRepository.save(detail));
        }

        savedOrder.setOrderDetails(details);

        if (customer != null && earnedPoints > 0) {
            pointService.addPoints(customer, earnedPoints, savedOrder.getFinalAmount());
        }

        cartService.clearCart();
        return savedOrder;
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}