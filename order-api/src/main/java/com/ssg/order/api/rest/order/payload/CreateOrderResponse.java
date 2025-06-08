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
public class CreateOrderResponse {
	private Long orderId;
	private List<OrderItemResponse> orderItems;
	private Integer totalAmount;


	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderItemResponse {
		Long productId;
		Integer quantity;
		Integer purchaseAmount;
	}
}
