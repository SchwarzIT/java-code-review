package de.schwarz.jobs.interview.coupon.app.adapter.out.persistence.mapper;

import de.schwarz.jobs.interview.coupon.app.adapter.out.persistence.entity.BasketEntity;
import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CouponEntityMapper.class)
public interface BasketEntityMapper {

  BasketEntity mapToEntity(Basket basket);

  Basket mapToDomain(BasketEntity basketEntity);
}
