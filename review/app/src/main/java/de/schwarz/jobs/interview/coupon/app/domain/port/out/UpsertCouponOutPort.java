package de.schwarz.jobs.interview.coupon.app.domain.port.out;

import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;

public interface UpsertCouponOutPort {

  Coupon upsertCoupon(final Coupon coupon);
}
