package de.schwarz.jobs.interview.coupon.app.domain.service;

import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;
import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import de.schwarz.jobs.interview.coupon.app.domain.port.in.PostBasketDiscountInPort;
import de.schwarz.jobs.interview.coupon.app.domain.port.out.UpsertBasketOutPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostBasketDiscountService implements PostBasketDiscountInPort {

  private final GetBasketFunction getBasketFunction;
  private final UpsertBasketOutPort upsertBasketOutPort;
  private final GetCouponFunction getCouponFunction;

  @Override
  public Basket applyDiscount(Long basketid, String code) {
    Basket basket = getBasketFunction.getBasket(basketid);
    Coupon coupon = getCouponFunction.getCoupon(code);
    basket.applyDiscount(coupon);
    return upsertBasketOutPort.upsertBasket(basket);
  }
}
