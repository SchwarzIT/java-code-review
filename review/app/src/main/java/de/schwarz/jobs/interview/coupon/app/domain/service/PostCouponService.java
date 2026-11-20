package de.schwarz.jobs.interview.coupon.app.domain.service;

import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import de.schwarz.jobs.interview.coupon.app.domain.port.in.PostCouponInPort;
import de.schwarz.jobs.interview.coupon.app.domain.port.out.UpsertCouponOutPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCouponService implements PostCouponInPort {

  private final UpsertCouponOutPort upsertCouponOutPort;

  @Override
  public Coupon createCoupon(Coupon coupon) {
    return upsertCouponOutPort.upsertCoupon(coupon);
  }
}
