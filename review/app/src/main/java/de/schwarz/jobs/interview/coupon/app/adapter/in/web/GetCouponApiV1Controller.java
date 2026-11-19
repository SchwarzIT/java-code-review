package de.schwarz.jobs.interview.coupon.app.adapter.in.web;

import de.schwarz.jobs.interview.coupon.api.coupons.v1.GetCouponApiV1;
import de.schwarz.jobs.interview.coupon.api.coupons.v1.dto.WebCoupon;
import de.schwarz.jobs.interview.coupon.app.adapter.in.web.mapper.WebCouponMapper;
import de.schwarz.jobs.interview.coupon.app.domain.port.in.GetCouponInPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAuthority('SCOPE_coupon_admin')")
@RequiredArgsConstructor
public class GetCouponApiV1Controller implements GetCouponApiV1 {

  private final GetCouponInPort getCouponInPort;
  private final WebCouponMapper webCouponMapper;

  @Override
  public ResponseEntity<WebCoupon> getCoupon(String code) {
    return ResponseEntity.ok(webCouponMapper.mapToWeb(getCouponInPort.getCoupon(code)));
  }

  @Override
  public ResponseEntity<List<WebCoupon>> getCoupons(List<String> codes) {
    return ResponseEntity.ok(getCouponInPort.getCoupons(codes)
        .stream()
        .map(webCouponMapper::mapToWeb)
        .toList());
  }
}
