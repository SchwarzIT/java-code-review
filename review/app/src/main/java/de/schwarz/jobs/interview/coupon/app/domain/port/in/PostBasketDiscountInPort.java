package de.schwarz.jobs.interview.coupon.app.domain.port.in;

import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;

public interface PostBasketDiscountInPort {

  Basket applyDiscount(Long basketid, String code);
}
