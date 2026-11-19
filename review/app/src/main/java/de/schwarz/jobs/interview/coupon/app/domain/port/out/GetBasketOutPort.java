package de.schwarz.jobs.interview.coupon.app.domain.port.out;

import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;
import java.util.Optional;

public interface GetBasketOutPort {

  Optional<Basket> findBasketByid(Long id);

}
