package com.ssg.order.service.usecase.order;

import static org.assertj.core.api.AssertionsForClassTypes.*;
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
import com.ssg.order.service.domain.product.Product;
import com.ssg.order.service.domain.product.ProductRepository;
import com.ssg.order.service.support.exception.AlreadyCanceledException;
import com.ssg.order.service.support.exception.OrderItemNotFoundException;
import com.ssg.order.service.support.exception.OrderNotFoundException;
import com.ssg.order.service.support.exception.ProductNotFoundException;
import com.ssg.order.service.usecase.order.command.CancelOrderItemCommand;
import com.ssg.order.service.usecase.order.result.CancelOrderItemResult;
import com.ssg.order.service.usecase.order.usecase.CancelOrderItemUseCase;

@ExtendWith(MockitoExtension.class)
public class CancelOrderItemUseCaseTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private CancelOrderItemUseCase cancelOrderItemUseCase;

	@Test
	void 주문상품_취소_성공() {
		Long productId = 1000000001L;
		OrderItem item = OrderItem.builder()
			.id(1L)
			.productId(productId)
			.originalPrice(800)
			.discountPrice(100)
			.purchasePrice(700)
			.quantity(2)
			.canceled(false)
			.build();

		Order order = Order.builder()
			.id(1L)
			.orderItems(List.of(item))
			.status(OrderStatus.CREATED)
			.totalAmount(1400)
			.build();

		Product product = Product.builder()
			.id(productId)
			.name("이마트 생수")
			.price(800)
			.discountAmount(100)
			.stock(1000)
			.build();

		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		when(productRepository.findByIdWithLock(productId)).thenReturn(Optional.of(product));

		CancelOrderItemCommand command = new CancelOrderItemCommand(1L, productId);

		CancelOrderItemResult result = cancelOrderItemUseCase.cancelOrderItem(command);

		assertThat(result.getProductId()).isEqualTo(productId);
		assertThat(result.getRefundAmount()).isEqualTo(1400);
		assertThat(result.getRemainingAmount()).isEqualTo(0);
		assertThat(item.isCanceled()).isTrue();
	}

	@Test
	void 주문상품_취소_실패_없는_주문() {
		when(orderRepository.findById(1L)).thenReturn(Optional.empty());

		CancelOrderItemCommand command = new CancelOrderItemCommand(1L, 100L);

		assertThatThrownBy(() -> cancelOrderItemUseCase.cancelOrderItem(command))
			.isInstanceOf(OrderNotFoundException.class);
	}

	@Test
	void 주문상품_취소_실패_없는_상품() {
		Order order = Order.builder()
			.id(1L)
			.orderItems(List.of())
			.status(OrderStatus.CREATED)
			.totalAmount(0)
			.build();

		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

		CancelOrderItemCommand command = new CancelOrderItemCommand(1L, 999L);

		assertThatThrownBy(() -> cancelOrderItemUseCase.cancelOrderItem(command))
			.isInstanceOf(OrderItemNotFoundException.class);
	}

	@Test
	void 주문상품_취소_실패_이미_취소됨() {
		Long productId = 100L;
		OrderItem item = OrderItem.builder()
			.id(1L)
			.productId(productId)
			.purchasePrice(1000)
			.quantity(1)
			.canceled(true)
			.build();

		Order order = Order.builder()
			.id(1L)
			.orderItems(List.of(item))
			.status(OrderStatus.CREATED)
			.totalAmount(1000)
			.build();

		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

		CancelOrderItemCommand command = new CancelOrderItemCommand(1L, productId);

		assertThatThrownBy(() -> cancelOrderItemUseCase.cancelOrderItem(command))
			.isInstanceOf(AlreadyCanceledException.class);
	}

	@Test
	void 주문상품_취소_실패_재고조회불가() {
		Long productId = 100L;
		OrderItem item = OrderItem.builder()
			.id(1L)
			.productId(productId)
			.purchasePrice(1000)
			.quantity(1)
			.canceled(false)
			.build();

		Order order = Order.builder()
			.id(1L)
			.orderItems(List.of(item))
			.status(OrderStatus.CREATED)
			.totalAmount(1000)
			.build();

		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		when(productRepository.findByIdWithLock(productId)).thenReturn(Optional.empty());

		CancelOrderItemCommand command = new CancelOrderItemCommand(1L, productId);

		assertThatThrownBy(() -> cancelOrderItemUseCase.cancelOrderItem(command))
			.isInstanceOf(ProductNotFoundException.class);
	}

	@Test
	void 주문취소_시_재고_복구() {
		Long productId = 1000000001L;

		OrderItem item = OrderItem.builder()
			.id(1L)
			.productId(productId)
			.originalPrice(800)
			.discountPrice(100)
			.purchasePrice(700)
			.quantity(3)
			.canceled(false)
			.build();

		Order order = Order.builder()
			.id(1L)
			.orderItems(List.of(item))
			.status(OrderStatus.CREATED)
			.totalAmount(2100)
			.build();

		Product product = Product.builder()
			.id(productId)
			.name("이마트 생수")
			.price(800)
			.discountAmount(100)
			.stock(5)
			.build();

		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		when(productRepository.findByIdWithLock(productId)).thenReturn(Optional.of(product));

		CancelOrderItemCommand command = new CancelOrderItemCommand(1L, productId);

		cancelOrderItemUseCase.cancelOrderItem(command);

		assertThat(product.getStock()).isEqualTo(8);
	}
}
