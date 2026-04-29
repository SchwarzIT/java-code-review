package schwarz.jobs.interview.coupon.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.web.dto.BasketDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BasketMapper {

    @Mapping(target = "applicationSuccessful", ignore = true)
    Basket toBasket(final BasketDTO basketDTO);

    BasketDTO toBasketDTO(final Basket basket);
}
