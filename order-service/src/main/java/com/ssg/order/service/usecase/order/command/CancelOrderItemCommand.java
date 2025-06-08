package com.ssg.order.service.usecase.order.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderItemCommand {
	private Long orderId;
	private Long productId;
}