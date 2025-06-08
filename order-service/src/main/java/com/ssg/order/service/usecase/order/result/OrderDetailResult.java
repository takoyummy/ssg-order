package com.ssg.order.service.usecase.order.result;

import java.util.List;
import java.util.stream.Collectors;

import com.ssg.order.service.domain.order.Order;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderDetailResult {

	private List<Item> items;
	private Integer totalAmount;


	@Getter
	@Builder
	public static class Item {
		private Long productId;
		private Integer quantity;
		private Integer purchaseAmount;
		private boolean isCanceled;
	}

	public static OrderDetailResult from(Order order) {
		List<Item> items = order.getOrderItems().stream()
			.map(item -> Item.builder()
				.productId(item.getProductId())
				.quantity(item.getQuantity())
				.purchaseAmount(item.getPurchasePrice() * item.getQuantity())
				.isCanceled(item.isCanceled())
				.build())
			.collect(Collectors.toList());

		int totalAmount = items.stream()
			.filter(item -> !item.isCanceled)
			.mapToInt(Item::getPurchaseAmount)
			.sum();

		return OrderDetailResult.builder()
			.totalAmount(totalAmount)
			.items(items)
			.build();
	}
}