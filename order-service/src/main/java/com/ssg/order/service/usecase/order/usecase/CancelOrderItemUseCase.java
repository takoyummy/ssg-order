package com.ssg.order.service.usecase.order.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssg.order.service.domain.order.Order;
import com.ssg.order.service.domain.order.OrderItem;
import com.ssg.order.service.domain.order.OrderRepository;
import com.ssg.order.service.domain.product.Product;
import com.ssg.order.service.domain.product.ProductRepository;
import com.ssg.order.service.support.exception.AlreadyCanceledException;
import com.ssg.order.service.support.exception.OrderItemNotFoundException;
import com.ssg.order.service.support.exception.OrderNotFoundException;
import com.ssg.order.service.support.exception.ProductNotFoundException;
import com.ssg.order.service.usecase.order.command.CancelOrderItemCommand;
import com.ssg.order.service.usecase.order.result.CancelOrderItemResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CancelOrderItemUseCase {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	@Transactional
	public CancelOrderItemResult cancelOrderItem(final CancelOrderItemCommand command) {
		final Order order = orderRepository.findById(command.getOrderId())
			.orElseThrow(OrderNotFoundException::new);

		final OrderItem item = order.getOrderItem(command.getProductId());
		if (item == null) {
			throw new OrderItemNotFoundException();
		}
		if (item.isCanceled()) {
			throw new AlreadyCanceledException();
		}

		item.cancel();

		final Product product = productRepository.findByIdWithLock(command.getProductId())
			.orElseThrow(ProductNotFoundException::new);

		product.increaseStock(item.getQuantity());

		final int refundAmount = item.getPurchasePrice() * item.getQuantity();
		final int remainingAmount = order.calculateTotalAmount();

		return CancelOrderItemResult.builder()
			.productId(item.getProductId())
			.refundAmount(refundAmount)
			.remainingAmount(remainingAmount)
			.build();
	}
}
