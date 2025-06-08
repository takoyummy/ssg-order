package com.ssg.order.service.domain.product;

import com.ssg.order.service.support.exception.InsufficientStockException;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "PRODUCT")
@Builder
public class Product {

	@Id
	private Long id;
	private String name;
	private int price;
	private int discountAmount;
	private int stock;

	public int getPurchasePrice() {
		return price - discountAmount;
	}

	public void decreaseStock(int quantity) {
		if (stock < quantity) {
			throw new InsufficientStockException();
		}
		stock -= quantity;
	}

	public void increaseStock(int quantity) {
		stock += quantity;
	}
}