package schwarz.jobs.interview.coupon.web.mapper;


import org.mapstruct.Mapper;
import schwarz.jobs.interview.coupon.constants.MapperConstants;
import schwarz.jobs.interview.coupon.core.services.model.domain.BasketDomain;
import schwarz.jobs.interview.coupon.web.dto.response.BasketResponse;

/**
 * Converts a {@link BasketDomain} into a {@link BasketResponse}.
 */
@Mapper(componentModel = MapperConstants.COMPONENT_MODEL)
public interface BasketResponseMapper extends ResponseMapper<BasketDomain, BasketResponse> {

}
