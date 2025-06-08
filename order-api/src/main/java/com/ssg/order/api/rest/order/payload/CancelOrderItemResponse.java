package com.ssg.order.api.rest.order.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderItemResponse {
	private Long productId;
	private Integer refundAmount;
	private Integer remainingAmount;
}
