package de.schwarz.jobs.interview.coupon.app.adapter.out.persistence.mapper;

import de.schwarz.jobs.interview.coupon.app.adapter.out.persistence.entity.CouponEntity;
import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CouponEntityMapper {

  CouponEntity mapToEntity(Coupon coupon);

  Coupon mapToDomain(CouponEntity couponEntity);
}
