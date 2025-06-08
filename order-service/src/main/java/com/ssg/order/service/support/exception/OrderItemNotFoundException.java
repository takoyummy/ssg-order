package com.ssg.order.service.support.exception;

public class OrderItemNotFoundException extends BaseException {
	public OrderItemNotFoundException() {
		super(ErrorCode.ORDER_ITEM_NOT_FOUND);
	}
}