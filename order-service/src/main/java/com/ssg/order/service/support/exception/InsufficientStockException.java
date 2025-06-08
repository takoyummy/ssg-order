package com.ssg.order.service.support.exception;

public class InsufficientStockException extends BaseException{
	public InsufficientStockException() {
		super(ErrorCode.OUT_OF_STOCK);
	}
}
