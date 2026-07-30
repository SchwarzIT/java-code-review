package schwarz.jobs.interview.coupon.web.mapper;

import org.mapstruct.Mapper;
import schwarz.jobs.interview.coupon.constants.MapperConstants;
import schwarz.jobs.interview.coupon.core.services.model.domain.BasketDomain;
import schwarz.jobs.interview.coupon.web.dto.request.BasketRequest;

/**
 * Converts a {@link BasketRequest} into a {@link BasketDomain}.
 */
@Mapper(componentModel = MapperConstants.COMPONENT_MODEL)
public interface BasketRequestMapper extends RequestMapper<BasketRequest, BasketDomain> {

}
