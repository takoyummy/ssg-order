package com.ssg.order.api.rest.order.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ssg.order.api.rest.order.payload.CreateOrderRequest;
import com.ssg.order.service.usecase.order.command.CancelOrderItemCommand;
import com.ssg.order.service.usecase.order.command.CreateOrderCommand;

@Mapper(componentModel = "spring")
public interface OrderCommandConverter {
	OrderCommandConverter INSTANCE = Mappers.getMapper(OrderCommandConverter.class);

	default CreateOrderCommand toCommand(CreateOrderRequest request) {
		List<CreateOrderCommand.Item> items = request.getItems().stream()
			.map(item -> new CreateOrderCommand.Item(item.getProductId(), item.getQuantity()))
			.collect(Collectors.toList());
		return new CreateOrderCommand(items);
	}

	default CancelOrderItemCommand toCommand(Long orderId, Long productId) {
		return new CancelOrderItemCommand(orderId, productId);
	}
}
