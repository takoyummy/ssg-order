package com.ssg.order.service.support.exception;


public class ProductNotFoundException extends BaseException {
	public ProductNotFoundException() {
		super(ErrorCode.PRODUCT_NOT_FOUND);
	}
}
