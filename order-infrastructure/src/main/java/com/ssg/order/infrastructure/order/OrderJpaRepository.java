package com.ssg.order.infrastructure.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssg.order.service.domain.order.Order;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
