package de.schwarz.jobs.interview.coupon.app.adapter.out.persistence;

import de.schwarz.jobs.interview.coupon.app.adapter.out.persistence.mapper.CouponEntityMapper;
import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import de.schwarz.jobs.interview.coupon.app.domain.port.out.GetCouponOutPort;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class GetCouponPersistenceAdapter implements GetCouponOutPort {

  private final CouponJpaRepository couponJpaRepository;
  private final CouponEntityMapper couponEntityMapper;

  @Override
  public Optional<Coupon> findCouponByCode(String code) {
    return couponJpaRepository.findById(code)
        .map(couponEntityMapper::mapToDomain);
  }

  @Override
  public List<Coupon> findAllCouponsByCodes(List<String> codes) {
    return couponJpaRepository.findAllById(codes)
        .stream()
        .map(couponEntityMapper::mapToDomain)
        .toList();
  }
}
