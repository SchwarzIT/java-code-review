package de.schwarz.jobs.interview.coupon.app.domain.service;

import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;
import de.schwarz.jobs.interview.coupon.app.domain.port.in.PostBasketInPort;
import de.schwarz.jobs.interview.coupon.app.domain.port.out.UpsertBasketOutPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostBasketService implements PostBasketInPort {

  private final UpsertBasketOutPort upsertBasketOutPort;

  @Override
  public Basket createBasket(Basket basket) {
    return upsertBasketOutPort.upsertBasket(basket);
  }
}
