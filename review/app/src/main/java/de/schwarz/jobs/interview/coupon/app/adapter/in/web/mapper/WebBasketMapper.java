package de.schwarz.jobs.interview.coupon.app.adapter.in.web.mapper;

import de.schwarz.jobs.interview.coupon.api.coupons.v1.dto.WebBasket;
import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = WebCouponMapper.class)
public interface WebBasketMapper {

  @Mapping(target = "applicationSuccessful", source = "applicationSuccessful")
  WebBasket mapToWeb(Basket basket);

  Basket mapToDomain(WebBasket webBasket);
}
