package com.ssg.order.service.usecase.order;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ssg.order.service.domain.order.Order;
import com.ssg.order.service.domain.order.OrderRepository;
import com.ssg.order.service.domain.product.Product;
import com.ssg.order.service.domain.product.ProductRepository;
import com.ssg.order.service.support.exception.InsufficientStockException;
import com.ssg.order.service.support.exception.ProductNotFoundException;
import com.ssg.order.service.usecase.order.command.CreateOrderCommand;
import com.ssg.order.service.usecase.order.usecase.CreateOrderUseCase;

@ExtendWith(MockitoExtension.class)
public class CreateOrderUseCaseTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private CreateOrderUseCase createOrderUseCase;

	@Test
	void 주문_생성_성공() {
		Product product1 = Product.builder()
			.id(1000000001L)
			.name("이마트 생수")
			.price(800)
			.discountAmount(100)
			.stock(1000)
			.build();

		CreateOrderCommand command = new CreateOrderCommand(List.of(
			new CreateOrderCommand.Item(1000000001L, 2)
		));

		when(productRepository.findByIdWithLock(1000000001L))
			.thenReturn(Optional.of(product1));

		ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
		when(orderRepository.save(any(Order.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		Order savedOrder = createOrderUseCase.createOrder(command);

		verify(productRepository).findByIdWithLock(1000000001L);
		verify(orderRepository).save(orderCaptor.capture());

		assertThat(savedOrder.getTotalAmount()).isEqualTo((800 - 100) * 2);
	}

	@Test
	void 주문_생성_실패_상품없음() {
		CreateOrderCommand command = new CreateOrderCommand(List.of(
			new CreateOrderCommand.Item(999L, 1)
		));

		when(productRepository.findByIdWithLock(999L))
			.thenReturn(Optional.empty());

		assertThatThrownBy(() -> createOrderUseCase.createOrder(command))
			.isInstanceOf(ProductNotFoundException.class);
	}

	@Test
	void 주문_생성_실패_재고부족() {
		Product product = Product.builder()
			.id(1000000001L)
			.name("이마트 생수")
			.price(800)
			.discountAmount(100)
			.stock(1)
			.build();

		CreateOrderCommand command = new CreateOrderCommand(List.of(
			new CreateOrderCommand.Item(1000000001L, 5)
		));

		when(productRepository.findByIdWithLock(1000000001L)).thenReturn(Optional.of(product));

		assertThatThrownBy(() -> createOrderUseCase.createOrder(command))
			.isInstanceOf(InsufficientStockException.class);
	}

	@Test
	void 주문_생성_여러_상품_성공() {
		Product product1 = Product.builder()
			.id(1000000001L)
			.name("이마트 생수")
			.price(800)
			.discountAmount(100)
			.stock(100)
			.build();

		Product product2 = Product.builder()
			.id(1000000002L)
			.name("신라면 멀티팩")
			.price(4200)
			.discountAmount(500)
			.stock(100)
			.build();

		CreateOrderCommand command = new CreateOrderCommand(List.of(
			new CreateOrderCommand.Item(1000000001L, 2),
			new CreateOrderCommand.Item(1000000002L, 1)
		));

		when(productRepository.findByIdWithLock(1000000001L)).thenReturn(Optional.of(product1));
		when(productRepository.findByIdWithLock(1000000002L)).thenReturn(Optional.of(product2));
		when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Order order = createOrderUseCase.createOrder(command);

		AssertionsForInterfaceTypes.assertThat(order.getOrderItems()).hasSize(2);
		assertThat(order.getTotalAmount()).isEqualTo((800 - 100) * 2 + (4200 - 500));
	}

	@Test
	void 주문_생성_시_재고_차감() {
		Product product = Product.builder()
			.id(1000000001L)
			.name("이마트 생수")
			.price(800)
			.discountAmount(100)
			.stock(10)
			.build();

		CreateOrderCommand command = new CreateOrderCommand(List.of(
			new CreateOrderCommand.Item(1000000001L, 3)
		));

		when(productRepository.findByIdWithLock(1000000001L)).thenReturn(Optional.of(product));
		when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

		createOrderUseCase.createOrder(command);

		assertThat(product.getStock()).isEqualTo(7);
	}

}
