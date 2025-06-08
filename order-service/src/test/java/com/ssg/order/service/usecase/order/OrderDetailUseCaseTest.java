package com.ssg.order.service.usecase.order;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ssg.order.service.domain.order.Order;
import com.ssg.order.service.domain.order.OrderItem;
import com.ssg.order.service.domain.order.OrderRepository;
import com.ssg.order.service.domain.order.OrderStatus;
import com.ssg.order.service.support.exception.OrderNotFoundException;
import com.ssg.order.service.usecase.order.result.OrderDetailResult;
import com.ssg.order.service.usecase.order.usecase.OrderDetailUseCase;

@ExtendWith(MockitoExtension.class)
public class OrderDetailUseCaseTest {

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderDetailUseCase orderDetailUseCase;

	@Test
	void 주문_조회_성공() {
		Long orderId = 1L;
		OrderItem item = OrderItem.builder()
			.productId(1000000001L)
			.productName("이마트 생수")
			.originalPrice(800)
			.discountPrice(100)
			.purchasePrice(700)
			.quantity(2)
			.build();

		Order order = Order.builder()
			.id(orderId)
			.orderItems(List.of(item))
			.status(OrderStatus.CREATED)
			.totalAmount(1400)
			.build();

		when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

		OrderDetailResult result = orderDetailUseCase.getOrder(orderId);

		assertThat(result.getTotalAmount()).isEqualTo(1400);
		assertThat(result.getItems()).hasSize(1);
	}

	@Test
	void 주문_조회_실패_존재하지_않는_주문() {
		when(orderRepository.findById(999L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> orderDetailUseCase.getOrder(999L))
			.isInstanceOf(OrderNotFoundException.class);
	}

}
