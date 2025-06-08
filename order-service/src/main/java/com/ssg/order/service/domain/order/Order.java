package com.ssg.order.service.domain.order;
import java.util.ArrayList;
import java.util.List;

import com.ssg.order.service.support.exception.OrderItemNotFoundException;
import com.ssg.order.service.support.exception.ProductNotFoundException;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "ORDER_INFO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int totalAmount;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id")
	@Builder.Default
	private List<OrderItem> orderItems = new ArrayList<>();

	public static Order create(List<OrderItem> items) {
		int total = items.stream()
			.mapToInt(i -> i.getPurchasePrice() * i.getQuantity())
			.sum();

		return Order.builder()
			.orderItems(items)
			.totalAmount(total)
			.status(OrderStatus.CREATED)
			.build();
	}

	public OrderItem getOrderItem(Long productId) {
		return orderItems.stream()
			.filter(i -> i.getProductId().equals(productId))
			.findFirst()
			.orElseThrow(() -> new OrderItemNotFoundException());
	}

	public int calculateTotalAmount() {
		return orderItems.stream()
			.filter(i -> !i.isCanceled())
			.mapToInt(i -> i.getPurchasePrice() * i.getQuantity())
			.sum();
	}
}