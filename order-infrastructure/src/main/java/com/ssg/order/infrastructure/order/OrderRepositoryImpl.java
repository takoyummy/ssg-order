package com.ssg.order.infrastructure.order;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ssg.order.service.domain.order.Order;
import com.ssg.order.service.domain.order.OrderRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

	private final OrderJpaRepository orderJpaRepository;

	@Override
	public Optional<Order> findById(Long id) {
		return orderJpaRepository.findById(id);
	}

	@Override
	public Order save(Order order) {
		return orderJpaRepository.save(order);
	}
}