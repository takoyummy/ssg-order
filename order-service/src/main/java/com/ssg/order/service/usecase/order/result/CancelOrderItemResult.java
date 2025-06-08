package com.ssg.order.service.usecase.order.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CancelOrderItemResult {
	private Long productId;
	private Integer refundAmount;
	private Integer remainingAmount;
}