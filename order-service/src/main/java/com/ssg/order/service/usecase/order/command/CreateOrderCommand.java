package com.ssg.order.service.usecase.order.command;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderCommand {
	private List<Item> items;

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Item {
		private Long productId;
		private int quantity;
	}
}