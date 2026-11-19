package de.schwarz.jobs.interview.coupon.app.domain.port.in;

import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import java.util.List;

public interface GetCouponInPort {

  Coupon getCoupon(final String code);

  List<Coupon> getCoupons(final List<String> codes);
}
