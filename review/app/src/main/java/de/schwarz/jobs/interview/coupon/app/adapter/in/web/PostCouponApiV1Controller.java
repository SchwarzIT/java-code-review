package de.schwarz.jobs.interview.coupon.app.adapter.in.web;

import de.schwarz.jobs.interview.coupon.api.coupons.CouponsApiConstants;
import de.schwarz.jobs.interview.coupon.api.coupons.v1.PostCouponApiV1;
import de.schwarz.jobs.interview.coupon.api.coupons.v1.dto.WebCoupon;
import de.schwarz.jobs.interview.coupon.app.adapter.in.web.mapper.WebCouponMapper;
import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import de.schwarz.jobs.interview.coupon.app.domain.port.in.PostCouponInPort;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAuthority('SCOPE_coupon_admin')")
@RequiredArgsConstructor
public class PostCouponApiV1Controller implements PostCouponApiV1 {

  private final PostCouponInPort postCouponInPort;
  private final WebCouponMapper webCouponMapper;

  @Override
  public ResponseEntity<Void> createCoupon(WebCoupon webCoupon) {
    Coupon coupon = postCouponInPort.createCoupon(webCouponMapper.mapToDomain(webCoupon));
    return ResponseEntity.created(URI.create(CouponsApiConstants.PATH_API_V1_ROOT + CouponsApiConstants.PATH_COUPONS + "/" + coupon.getCode()))
        .build();
  }
}
