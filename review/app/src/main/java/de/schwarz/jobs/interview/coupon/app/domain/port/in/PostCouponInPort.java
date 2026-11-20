package de.schwarz.jobs.interview.coupon.app.domain.port.in;

import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;

public interface PostCouponInPort {

  Coupon createCoupon(Coupon coupon);
}
