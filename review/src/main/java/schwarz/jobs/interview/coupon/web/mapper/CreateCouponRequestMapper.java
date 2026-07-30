package schwarz.jobs.interview.coupon.web.mapper;

import org.mapstruct.Mapper;
import schwarz.jobs.interview.coupon.constants.MapperConstants;
import schwarz.jobs.interview.coupon.core.services.model.command.CreateCouponCommand;
import schwarz.jobs.interview.coupon.web.dto.request.CreateCouponRequest;

/**
 * Converts a {@link CreateCouponRequest} into a {@link CreateCouponCommand}.
 */
@Mapper(componentModel = MapperConstants.COMPONENT_MODEL)
public interface CreateCouponRequestMapper extends CommandMapper<CreateCouponRequest, CreateCouponCommand> {

}
