package de.schwarz.jobs.interview.coupon.app.adapter.in.web.mapper;

import de.schwarz.jobs.interview.coupon.api.coupons.v1.dto.WebCoupon;
import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WebCouponMapper {

  WebCoupon mapToWeb(Coupon coupon);

  Coupon mapToDomain(WebCoupon webCoupon);
}
