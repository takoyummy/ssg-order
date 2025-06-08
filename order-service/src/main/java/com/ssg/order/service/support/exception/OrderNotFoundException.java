package com.ssg.order.service.support.exception;

public class OrderNotFoundException extends BaseException{
	public OrderNotFoundException() {
		super(ErrorCode.ORDER_NOT_FOUND);
	}
}
