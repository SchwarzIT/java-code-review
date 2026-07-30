package schwarz.jobs.interview.coupon.web.mapper;


import org.mapstruct.Mapper;
import schwarz.jobs.interview.coupon.constants.MapperConstants;
import schwarz.jobs.interview.coupon.core.services.model.domain.CouponDomain;
import schwarz.jobs.interview.coupon.web.dto.response.CouponResponse;

/**
 * Converts a {@link CouponDomain} into a {@link CouponResponse}.
 */
@Mapper(componentModel = MapperConstants.COMPONENT_MODEL)
public interface CouponResponseMapper extends ResponseMapper<CouponDomain, CouponResponse> {

}
