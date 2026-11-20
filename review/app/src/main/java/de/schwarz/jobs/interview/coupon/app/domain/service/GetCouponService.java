package de.schwarz.jobs.interview.coupon.app.domain.service;

import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import de.schwarz.jobs.interview.coupon.app.domain.port.in.GetCouponInPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCouponService implements GetCouponInPort {

  private final GetCouponFunction getCouponFunction;

  @Override
  public Coupon getCoupon(String code) {
    return getCouponFunction.getCoupon(code);
  }

  @Override
  public List<Coupon> getCoupons(List<String> codes) {
    return getCouponFunction.getCoupons(codes);
  }
}
