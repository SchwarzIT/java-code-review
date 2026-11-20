package de.schwarz.jobs.interview.coupon.app.domain.port.out;

import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import java.util.List;
import java.util.Optional;

public interface GetCouponOutPort {

  Optional<Coupon> findCouponByCode(String code);

  List<Coupon> findAllCouponsByCodes(final List<String> codes);

}
