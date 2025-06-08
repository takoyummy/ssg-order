package com.ssg.order.service.domain.order;

import java.util.Optional;

public interface OrderRepository {
	Optional<Order> findById(Long id);
	Order save(Order order);
}