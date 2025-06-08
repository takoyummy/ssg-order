package com.ssg.order.service.usecase.order.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssg.order.service.domain.order.Order;
import com.ssg.order.service.domain.order.OrderRepository;
import com.ssg.order.service.support.exception.OrderNotFoundException;
import com.ssg.order.service.usecase.order.result.OrderDetailResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderDetailUseCase {

	private final OrderRepository orderRepository;

	@Transactional(readOnly = true)
	public OrderDetailResult getOrder(final Long orderId) {
		final Order order = orderRepository.findById(orderId)
			.orElseThrow(OrderNotFoundException::new);

		return OrderDetailResult.from(order);
	}
}
