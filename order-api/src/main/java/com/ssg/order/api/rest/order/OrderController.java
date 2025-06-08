package com.ssg.order.api.rest.order;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssg.order.api.rest.order.converter.OrderCommandConverter;
import com.ssg.order.api.rest.order.converter.OrderResponseConverter;
import com.ssg.order.api.rest.order.payload.CancelOrderItemResponse;
import com.ssg.order.api.rest.order.payload.CreateOrderRequest;
import com.ssg.order.api.rest.order.payload.CreateOrderResponse;
import com.ssg.order.api.rest.order.payload.OrderDetailResponse;
import com.ssg.order.api.support.response.ResponseWrapper;
import com.ssg.order.service.usecase.order.command.CancelOrderItemCommand;
import com.ssg.order.service.usecase.order.command.CreateOrderCommand;
import com.ssg.order.service.usecase.order.usecase.CancelOrderItemUseCase;
import com.ssg.order.service.usecase.order.usecase.CreateOrderUseCase;
import com.ssg.order.service.usecase.order.usecase.OrderDetailUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order API", description = "주문 관련 api")
public class OrderController {
	private final CreateOrderUseCase createOrderUseCase;
	private final CancelOrderItemUseCase cancelOrderItemUseCase;
	private final OrderDetailUseCase orderDetailUseCase;

	@PostMapping
	@Operation(summary = "주문 생성", description = "상품 ID 및 수량 리스트로 주문을 생성합니다.")
	public ResponseWrapper<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest orderCreateRequest) {
		final CreateOrderCommand createOrderCommand = OrderCommandConverter.INSTANCE.toCommand(orderCreateRequest);
		return ResponseWrapper.ok(OrderResponseConverter.INSTANCE.toCreateOrderResponse(
			createOrderUseCase.createOrder(createOrderCommand)));
	}

	@DeleteMapping("/{orderId}/items/{productId}")
	@Operation(summary = "주문 상품 취소", description = "주문 상품을 개별 취소합니다.")
	public ResponseWrapper<CancelOrderItemResponse> cancelOrderItem(
		@Schema(description = "주문 ID", example = "1") @PathVariable Long orderId,
		@Schema(description = "상품 ID", example = "1000000001") @PathVariable Long productId) {
		final CancelOrderItemCommand cancelOrderItemCommand = OrderCommandConverter.INSTANCE.toCommand(orderId, productId);
		return ResponseWrapper.ok(OrderResponseConverter.INSTANCE.toCancelOrderItemResponse(cancelOrderItemUseCase.cancelOrderItem(cancelOrderItemCommand)));
	}

	@GetMapping("/{orderId}")
	@Operation(summary = "주문 조회", description = "주문 ID로 주문을 조회합니다.")
	public ResponseWrapper<OrderDetailResponse> getOrder(@PathVariable Long orderId) {
		return ResponseWrapper.ok(OrderResponseConverter.INSTANCE.toOrderDetailResponse(orderDetailUseCase.getOrder(orderId)));
	}
}
