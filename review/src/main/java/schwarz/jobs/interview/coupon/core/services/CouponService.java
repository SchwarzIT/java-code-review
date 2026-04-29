package schwarz.jobs.interview.coupon.core.services;

import java.util.List;
import java.util.Optional;
import schwarz.jobs.interview.coupon.core.services.model.Coupon;
import schwarz.jobs.interview.coupon.core.services.model.CouponFilter;

public interface CouponService {

    Optional<Coupon> getCouponByCode(final String code);

    Optional<Coupon> getCouponById(final Long id);

    Long createCoupon(final Coupon coupon);

    List<Coupon> filterCoupons(final CouponFilter couponFilter);
}
