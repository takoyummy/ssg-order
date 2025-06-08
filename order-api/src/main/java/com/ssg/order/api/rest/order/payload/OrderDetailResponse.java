package com.ssg.order.api.rest.order.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
	private List<OrderItem> items;
	private Integer totalAmount;

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderItem {
		private Long productId;
		private Integer quantity;
		private Integer purchaseAmount;
		private boolean canceled;
	}
}
