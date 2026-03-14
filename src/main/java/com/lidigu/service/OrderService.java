package com.lidigu.service;

import com.lidigu.common.ApiResponse;
import com.lidigu.dto.OrderDTO;
import com.lidigu.dto.PlaceOrderRequest;
import com.lidigu.entity.*;
import com.lidigu.mapper.OrderMapper;
import com.lidigu.repository.*;
import com.lidigu.service.EmailService;
import com.lidigu.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderShippingRepository shippingRepository;
    @Autowired
    private OrderPaymentRepository paymentRepository;
    @Autowired
    private CartItemRepository cartRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private PrimeMembershipRepository primeMembershipRepository;
    @Autowired
    private UserAddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public Object placeOrder(PlaceOrderRequest request) {
        try {
            String orderId = "ORDER_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            Order order = new Order();
            order.setOrderId(orderId);
            order.setUserId(request.getUserId());
            order.setSubtotal(request.getSubtotal());
            order.setDiscountValue(request.getDiscount());
            order.setTaxAmount(request.getTax());
            order.setDeliveryCharges(request.getDeliveryCharges());
            order.setTotalAmount(request.getFinalTotal());
            order.setPaymentMethod(request.getPaymentMethod());
            order.setStatus("Pending");
            order.setPaymentStatus("Pending");
            order.setTransactionStatus(order.getPaymentMethod().equals("COD") ? "N/A" : "Pending");

            orderRepository.save(order);

            if (request.getCartItems() != null) {
                for (PlaceOrderRequest.CartItemRequest itemData : request.getCartItems()) {
                    OrderItem item = new OrderItem();
                    item.setOrderId(orderId);
                    item.setProductId(itemData.getProductId());
                    item.setQuantity(itemData.getQuantity());
                    item.setPrice(itemData.getQuantityAmountPrice());
                    item.setTax(itemData.getTaxAmount());
                    item.setTotalPrice(itemData.getQuantityAmountPriceWithTax());
                    orderItemRepository.save(item);
                }
            }

            if (request.getShippingAddressId() != null) {
                addressRepository.findById(request.getShippingAddressId()).ifPresent(addr -> {
                    OrderShipping shipping = new OrderShipping();
                    shipping.setOrderId(orderId);
                    shipping.setUserId(order.getUserId());
                    shipping.setShippingStatus("Pending");
                    shippingRepository.save(shipping);
                });
            } else {
                OrderShipping shipping = new OrderShipping();
                shipping.setOrderId(orderId);
                shipping.setUserId(order.getUserId());
                shipping.setShippingStatus("Pending");
                shippingRepository.save(shipping);
            }

            userRepository.findById(order.getUserId()).ifPresent(user -> {
                try {
                    emailService.sendOrderConfirmation(user.getEmail(), user.getFirstName(), orderId,
                            "Order placed successfully");
                    if (user.getFcmToken() != null) {
                        notificationService.sendPushNotification(user.getFcmToken(), "Order Placed",
                                "Your order " + orderId + " has been placed successfully!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            return ApiResponse.success("Order placed successfully.", orderId);

        } catch (Exception e) {
            return ApiResponse.error("Order placement failed: " + e.getMessage());
        }
    }

    public Object getUserOrders(Long user_id) {
        try {
            List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(user_id);
            if (orders.isEmpty()) {
                return ApiResponse.phpError("No orders found for this user.");
            }

            List<OrderDTO> structuredOrders = orders.stream()
                    .map(order -> {
                        List<OrderItem> items = orderItemRepository.findByOrderId(order.getOrderId());
                        return orderMapper.toDto(order, items);
                    })
                    .collect(Collectors.toList());

            return ApiResponse.success("Orders retrieved successfully.", structuredOrders);
        } catch (Exception e) {
            return ApiResponse.error("Error: " + e.getMessage());
        }
    }

    @Transactional
    public Object cancelOrder(Long user_id, String order_id) {
        try {
            Optional<Order> orderOpt = orderRepository.findByOrderIdAndUserId(order_id, user_id);
            if (orderOpt.isEmpty()) {
                return ApiResponse.phpError("Order not found or does not belong to the user.");
            }

            Order order = orderOpt.get();
            if ("Cancelled".equalsIgnoreCase(order.getStatus())) {
                return ApiResponse.phpError("Order is already cancelled.");
            } else if ("Delivered".equalsIgnoreCase(order.getStatus())) {
                return ApiResponse.phpError("Delivered orders cannot be cancelled.");
            }

            order.setStatus("Cancelled");
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);

            if ("Paid".equalsIgnoreCase(order.getPaymentStatus())) {
                paymentRepository.findByOrderId(order_id).ifPresent(payment -> {
                    payment.setTransactionStatus("RefundPending");
                    payment.setUpdatedAt(LocalDateTime.now());
                    paymentRepository.save(payment);
                });
            }

            List<OrderItem> items = orderItemRepository.findByOrderId(order_id);
            for (OrderItem item : items) {
                item.setItemStatus("Cancelled");
                orderItemRepository.save(item);
            }

            shippingRepository.findByOrderId(order_id).ifPresent(shipping -> {
                shipping.setShippingStatus("Cancelled");
                shippingRepository.save(shipping);
            });

            userRepository.findById(user_id).ifPresent(user -> {
                try {
                    if (user.getFcmToken() != null) {
                        notificationService.sendPushNotification(user.getFcmToken(), "Order Cancelled",
                                "Your order " + order_id + " has been cancelled.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            return ApiResponse.success("Order has been cancelled successfully.", null);

        } catch (Exception e) {
            return ApiResponse.error("Error: " + e.getMessage());
        }
    }

    public Object getOrderDetails(String user_id, String coupon_code) {
        try {
            Long userId = Long.valueOf(user_id);
            List<CartItem> cartItems = cartRepository.findByUserId(user_id);

            if (cartItems.isEmpty()) {
                return ApiResponse.phpError("No items in cart");
            }

            BigDecimal subtotal = BigDecimal.ZERO;
            BigDecimal totalTax = BigDecimal.ZERO;
            List<Map<String, Object>> itemsList = new ArrayList<>();

            for (CartItem item : cartItems) {
                BigDecimal itemPrice = item.getProduct().getProductDiscountPrice() != null
                        ? item.getProduct().getProductDiscountPrice()
                        : item.getProduct().getProductPrice();

                BigDecimal quantityAmountPrice = itemPrice.multiply(new BigDecimal(item.getQuantity()));
                BigDecimal taxPerItem = quantityAmountPrice.multiply(new BigDecimal("0.18"));
                BigDecimal quantityAmountPriceWithTax = quantityAmountPrice.add(taxPerItem);

                subtotal = subtotal.add(quantityAmountPrice);
                totalTax = totalTax.add(taxPerItem);

                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("product_id", item.getProduct().getProductId());
                itemMap.put("product_name", item.getProduct().getProductName());
                itemMap.put("quantity", item.getQuantity());
                itemMap.put("quantity_amount_price", quantityAmountPrice.setScale(2, RoundingMode.HALF_UP));
                itemMap.put("tax_amount", taxPerItem.setScale(2, RoundingMode.HALF_UP));
                itemMap.put("quantity_amount_price_with_tax",
                        quantityAmountPriceWithTax.setScale(2, RoundingMode.HALF_UP));
                itemMap.put("product_image_url", item.getProduct().getProductImageUrl());

                itemsList.add(itemMap);
            }

            BigDecimal discount = BigDecimal.ZERO;
            int couponApplied = 0;
            String message = "Order details retrieved successfully";

            if (coupon_code != null && !coupon_code.isEmpty()) {
                Optional<Coupon> couponOpt = couponRepository.findByCouponCodeAndStatus(coupon_code, "active");
                if (couponOpt.isPresent()) {
                    Coupon coupon = couponOpt.get();
                    if (coupon.getExpiryDate().isAfter(LocalDate.now().minusDays(1))) {
                        if (subtotal.compareTo(coupon.getMinOrderAmount()) >= 0) {
                            if (coupon.getUsedCount() < coupon.getUsageLimit()) {
                                if ("percentage".equalsIgnoreCase(coupon.getDiscountType())) {
                                    discount = subtotal
                                            .multiply(coupon.getDiscountValue().divide(new BigDecimal("100")));
                                    if (coupon.getMaxDiscount() != null) {
                                        discount = discount.min(coupon.getMaxDiscount());
                                    }
                                } else {
                                    discount = coupon.getDiscountValue();
                                }
                                couponApplied = 1;
                                message = "Coupon applied successfully";
                            } else {
                                message = "Coupon usage limit reached";
                            }
                        } else {
                            message = "Minimum order amount not met for this coupon";
                        }
                    } else {
                        message = "Invalid or expired coupon";
                    }
                } else {
                    message = "Invalid or expired coupon";
                }
            }

            BigDecimal deliveryCharge = new BigDecimal("199");
            Optional<PrimeMembership> primeOpt = primeMembershipRepository.findByUserId(userId);
            if (primeOpt.isPresent()) {
                PrimeMembership prime = primeOpt.get();
                if (prime.getExpiryDate() != null && prime.getExpiryDate().isAfter(LocalDateTime.now())) {
                    deliveryCharge = BigDecimal.ZERO;
                }
            }

            BigDecimal finalTotal = subtotal.subtract(discount).add(totalTax).add(deliveryCharge);

            Map<String, Object> data = new HashMap<>();
            data.put("coupon_applied", couponApplied);
            data.put("Subtotal", subtotal.setScale(2, RoundingMode.HALF_UP).toString());
            data.put("Discount", discount.setScale(2, RoundingMode.HALF_UP).toString());
            data.put("Tax", totalTax.setScale(2, RoundingMode.HALF_UP).toString());
            data.put("Delivery Charges", deliveryCharge.setScale(2, RoundingMode.HALF_UP).toString());
            data.put("Final Total", finalTotal.setScale(2, RoundingMode.HALF_UP).toString());
            data.put("cart_items", itemsList);

            Optional<UserAddress> addrOpt = addressRepository.findByUserId(userId).stream()
                    .filter(a -> a.getDefaultAddress() == 1)
                    .findFirst();

            if (addrOpt.isPresent()) {
                data.put("shippingAddress", addrOpt.get());
            } else {
                data.put("shippingAddress", null);
            }

            return ApiResponse.success(message, data);

        } catch (Exception e) {
            return ApiResponse.error("Error: " + e.getMessage());
        }
    }

    public Object getPlaceOrderDetails(String order_id) {
        Optional<Order> orderOpt = orderRepository.findById(order_id);

        if (orderOpt.isEmpty()) {
            return ApiResponse.error("Order not found");
        }

        Order order = orderOpt.get();
        List<OrderItem> items = orderItemRepository.findByOrderId(order_id);

        OrderDTO orderDTO = orderMapper.toDto(order, items);
        return ApiResponse.success("Order details retrieved.", orderDTO);
    }
}
