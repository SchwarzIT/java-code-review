package de.schwarz.jobs.interview.coupon.app.adapter.out.persistence;

import de.schwarz.jobs.interview.coupon.app.adapter.out.persistence.mapper.BasketEntityMapper;
import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;
import de.schwarz.jobs.interview.coupon.app.domain.port.out.GetBasketOutPort;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class GetBasketPersistenceAdapter implements GetBasketOutPort {

  private final BasketJpaRepository basketJpaRepository;
  private final BasketEntityMapper basketEntityMapper;

  @Override
  public Optional<Basket> findBasketByid(Long id) {
    return basketJpaRepository.findById(id)
        .map(basketEntityMapper::mapToDomain);
  }
}
