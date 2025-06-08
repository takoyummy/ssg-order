package com.ssg.order.api.support.response;

import org.springframework.http.HttpStatus;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseWrapper<T> {

	private final int status;
	private final T data;

	public static ResponseWrapper<Void> ok() {
		return ok(null);
	}

	public static <T> ResponseWrapper<T> ok(@Nullable final T data) {
		return new ResponseWrapper<>(HttpStatus.OK.value(), data);
	}
}
