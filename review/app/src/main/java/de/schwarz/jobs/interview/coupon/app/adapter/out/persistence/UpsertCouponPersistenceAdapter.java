package de.schwarz.jobs.interview.coupon.app.adapter.out.persistence;

import de.schwarz.jobs.interview.coupon.app.adapter.out.persistence.entity.CouponEntity;
import de.schwarz.jobs.interview.coupon.app.adapter.out.persistence.mapper.CouponEntityMapper;
import de.schwarz.jobs.interview.coupon.app.domain.model.Coupon;
import de.schwarz.jobs.interview.coupon.app.domain.port.out.UpsertCouponOutPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UpsertCouponPersistenceAdapter implements UpsertCouponOutPort {

  private final CouponJpaRepository couponJpaRepository;
  private final CouponEntityMapper couponEntityMapper;

  @Override
  public Coupon upsertCoupon(Coupon coupon) {
    CouponEntity couponEntity = couponEntityMapper.mapToEntity(coupon);
    CouponEntity savedCouponEntity = couponJpaRepository.save(couponEntity);
    return couponEntityMapper.mapToDomain(savedCouponEntity);
  }
}
