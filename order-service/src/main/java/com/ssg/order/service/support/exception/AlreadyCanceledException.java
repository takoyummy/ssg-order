package com.ssg.order.service.support.exception;

public class AlreadyCanceledException extends BaseException {
	public AlreadyCanceledException() {
		super(ErrorCode.ALREADY_CANCELED);
	}
}