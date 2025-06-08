package com.ssg.order.service.usecase.order.usecase;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssg.order.service.domain.order.Order;
import com.ssg.order.service.domain.order.OrderItem;
import com.ssg.order.service.domain.order.OrderRepository;
import com.ssg.order.service.domain.product.Product;
import com.ssg.order.service.domain.product.ProductRepository;
import com.ssg.order.service.support.exception.ProductNotFoundException;
import com.ssg.order.service.usecase.order.command.CreateOrderCommand;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	@Transactional
	public Order createOrder(final CreateOrderCommand command) {
		final List<OrderItem> orderItems = command.getItems().stream()
			.map(item -> {
				Product product = productRepository.findByIdWithLock(item.getProductId())
					.orElseThrow(ProductNotFoundException::new);

				product.decreaseStock(item.getQuantity());

				return OrderItem.builder()
					.productId(product.getId())
					.productName(product.getName())
					.originalPrice(product.getPrice())
					.discountPrice(product.getDiscountAmount())
					.purchasePrice(product.getPurchasePrice())
					.quantity(item.getQuantity())
					.build();
			})
			.collect(Collectors.toList());

		final Order order = Order.create(orderItems);
		return orderRepository.save(order);
	}
}
