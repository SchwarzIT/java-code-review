package schwarz.jobs.interview.coupon.core.persistence.mapper;

import org.mapstruct.Mapper;
import schwarz.jobs.interview.coupon.constants.MapperConstants;
import schwarz.jobs.interview.coupon.core.persistence.entity.CouponEntity;
import schwarz.jobs.interview.coupon.core.services.model.domain.CouponDomain;

/**
 * Converts between {@link CouponDomain} and {@link CouponEntity}.
 */
@Mapper(componentModel = MapperConstants.COMPONENT_MODEL)
public interface CouponDataBaseMapper extends DatabaseMapper<CouponDomain, CouponEntity> {


}
