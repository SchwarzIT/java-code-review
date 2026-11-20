package de.schwarz.jobs.interview.coupon.app.domain.service;

import de.schwarz.jobs.interview.coupon.app.domain.exception.BasketNotFoundException;
import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;
import de.schwarz.jobs.interview.coupon.app.domain.port.out.GetBasketOutPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetBasketFunction {

  private final GetBasketOutPort getBasketOutPort;

  public Basket getBasket(Long id) {
    return getBasketOutPort.findBasketByid(id)
        .orElseThrow(() -> new BasketNotFoundException("Basket not found"));
  }
}
