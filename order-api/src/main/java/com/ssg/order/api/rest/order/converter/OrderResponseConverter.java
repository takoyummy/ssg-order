package com.ssg.order.api.rest.order.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ssg.order.api.rest.order.payload.CancelOrderItemResponse;
import com.ssg.order.api.rest.order.payload.CreateOrderResponse;
import com.ssg.order.api.rest.order.payload.OrderDetailResponse;
import com.ssg.order.service.domain.order.Order;
import com.ssg.order.service.domain.order.OrderItem;
import com.ssg.order.service.usecase.order.result.CancelOrderItemResult;
import com.ssg.order.service.usecase.order.result.OrderDetailResult;

@Mapper
public interface OrderResponseConverter {

	OrderResponseConverter INSTANCE = Mappers.getMapper(OrderResponseConverter.class);

	@Mapping(target = "orderId", source = "id")
	@Mapping(target = "totalAmount", source = "totalAmount")
	@Mapping(target = "orderItems", source = "orderItems")
	CreateOrderResponse toCreateOrderResponse(Order order);

	@Mapping(target = "productId", source = "productId")
	@Mapping(target = "refundAmount", source = "refundAmount")
	@Mapping(target = "remainingAmount", source = "remainingAmount")
	CancelOrderItemResponse toCancelOrderItemResponse(CancelOrderItemResult source);

	@Mapping(target = "totalAmount", source = "totalAmount")
	@Mapping(target = "items", source = "items")
	OrderDetailResponse toOrderDetailResponse(OrderDetailResult source);
	
	OrderDetailResponse.OrderItem toOrderItem(OrderDetailResult.Item source);

	default CreateOrderResponse.OrderItemResponse toOrderItemResponse(OrderItem item) {
		return CreateOrderResponse.OrderItemResponse.builder()
			.productId(item.getProductId())
			.quantity(item.getQuantity())
			.purchaseAmount(item.getPurchasePrice() * item.getQuantity())
			.build();
	}

	List<CreateOrderResponse.OrderItemResponse> toOrderItemResponseList(List<OrderItem> items);
}
