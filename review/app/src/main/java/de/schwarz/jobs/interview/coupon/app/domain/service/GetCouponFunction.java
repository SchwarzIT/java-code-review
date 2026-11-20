package de.schwarz.jobs.interview.coupon.app.domain.service;

import de.schwarz.jobs.interview.coupon.app.domain.exception.CouponNotFoundException;
import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import de.schwarz.jobs.interview.coupon.app.domain.port.out.GetCouponOutPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCouponFunction {

  private final GetCouponOutPort getCouponOutPort;

  public Coupon getCoupon(String code) {
    return getCouponOutPort.findCouponByCode(code)
        .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));
  }

  public List<Coupon> getCoupons(List<String> codes) {
    return getCouponOutPort.findAllCouponsByCodes(codes);
  }
}
