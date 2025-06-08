package com.ssg.order.api.rest.order.payload;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateOrderRequest {
	List<OrderItemRequest> items;

	@Value
	@Builder
	public static class OrderItemRequest {
		Long productId;
		int quantity;
	}
}
