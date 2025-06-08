package com.ssg.order.infrastructure.product;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ssg.order.service.domain.product.Product;
import com.ssg.order.service.domain.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

	private final ProductJpaRepository productJpaRepository;

	@Override
	public Optional<Product> findById(Long id) {
		return productJpaRepository.findById(id);
	}

	@Override
	public Optional<Product> findByIdWithLock(Long id) {
		return productJpaRepository.findByIdWithLock(id);
	}

	@Override
	public Product save(Product product) {
		return productJpaRepository.save(product);
	}
}
