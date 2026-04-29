package schwarz.jobs.interview.coupon.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import schwarz.jobs.interview.coupon.core.domain.CouponEntity;
import schwarz.jobs.interview.coupon.core.services.model.Coupon;
import schwarz.jobs.interview.coupon.core.services.model.CouponFilter;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponFilterDTO;
import schwarz.jobs.interview.coupon.web.dto.CreateCouponDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CouponMapper {

    @Mapping(target = "id", ignore = true)
    Coupon toCoupon(final CreateCouponDTO createCouponDTO);

    Coupon toCoupon(final CouponEntity couponEntity);

    CouponEntity toCouponEntity(final Coupon coupon);

    CouponDTO toCouponDto(final Coupon coupon);

    CouponFilter toCouponFilter(final CouponFilterDTO couponFilterDTO);
}
