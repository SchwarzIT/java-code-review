package de.schwarz.jobs.interview.coupon.app.domain.port.in;

import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;

public interface PostBasketInPort {

  Basket createBasket(Basket basket);

}
