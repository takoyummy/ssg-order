package com.ssg.order.infrastructure.product;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.ssg.order.service.domain.product.Product;

import jakarta.persistence.LockModeType;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Product p WHERE p.id = :id")
	Optional<Product> findByIdWithLock(Long id);
}
