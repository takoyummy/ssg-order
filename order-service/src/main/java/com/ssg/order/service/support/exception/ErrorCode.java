package com.ssg.order.service.support.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	PRODUCT_NOT_FOUND("상품이 존재하지 않습니다."),
	ORDER_NOT_FOUND("주문이 존재하지 않습니다."),
	ORDER_ITEM_NOT_FOUND("주문 상품이 존재하지 않습니다."),
	OUT_OF_STOCK("재고가 부족합니다."),
	ALREADY_CANCELED("이미 취소된 상품입니다.");

	private final String message;
}