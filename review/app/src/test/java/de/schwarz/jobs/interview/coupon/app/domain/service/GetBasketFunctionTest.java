package de.schwarz.jobs.interview.coupon.app.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.schwarz.jobs.interview.coupon.app.domain.exception.BasketNotFoundException;
import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;
import de.schwarz.jobs.interview.coupon.app.domain.port.out.GetBasketOutPort;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetBasketFunctionTest {

  @InjectMocks
  private GetBasketFunction getBasketFunction;

  @Mock
  private GetBasketOutPort getBasketOutPort;

  @Test
  void shouldReturnBasketById() {
    final Long basketId = 1L;
    final Basket expectedBasket = Basket.builder()
        .id(basketId)
        .amount(BigDecimal.valueOf(100))
        .build();
    when(getBasketOutPort.findBasketByid(basketId)).thenReturn(Optional.of(expectedBasket));

    assertThat(getBasketFunction.getBasket(basketId)).isSameAs(expectedBasket);
    verify(getBasketOutPort).findBasketByid(basketId);
  }

  @Test
  void shouldThrowBasketNotFoundWhenIdDoesNotExist() {
    final Long basketId = 1L;
    when(getBasketOutPort.findBasketByid(basketId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> getBasketFunction.getBasket(basketId))
        .isInstanceOf(BasketNotFoundException.class)
        .hasMessage("Basket not found");
  }
}
