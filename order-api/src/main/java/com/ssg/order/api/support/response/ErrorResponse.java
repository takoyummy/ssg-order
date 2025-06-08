package com.ssg.order.api.support.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ErrorResponse {
	private int status;
	private String message;
}