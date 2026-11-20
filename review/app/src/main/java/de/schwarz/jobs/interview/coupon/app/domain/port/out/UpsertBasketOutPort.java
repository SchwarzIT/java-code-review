package de.schwarz.jobs.interview.coupon.app.domain.port.out;

import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;

public interface UpsertBasketOutPort {

  Basket upsertBasket(final Basket basket);
}
