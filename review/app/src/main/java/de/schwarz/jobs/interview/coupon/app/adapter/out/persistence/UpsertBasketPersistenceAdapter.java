package de.schwarz.jobs.interview.coupon.app.adapter.out.persistence;

import de.schwarz.jobs.interview.coupon.app.adapter.out.persistence.entity.BasketEntity;
import de.schwarz.jobs.interview.coupon.app.adapter.out.persistence.mapper.BasketEntityMapper;
import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;
import de.schwarz.jobs.interview.coupon.app.domain.port.out.UpsertBasketOutPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UpsertBasketPersistenceAdapter implements UpsertBasketOutPort {

  private final BasketJpaRepository basketJpaRepository;
  private final BasketEntityMapper basketEntityMapper;

  @Override
  public Basket upsertBasket(Basket basket) {
    BasketEntity basketEntity = basketEntityMapper.mapToEntity(basket);
    BasketEntity savedBasketEntity = basketJpaRepository.save(basketEntity);
    return basketEntityMapper.mapToDomain(savedBasketEntity);
  }
}
