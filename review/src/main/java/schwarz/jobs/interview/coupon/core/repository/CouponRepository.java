package schwarz.jobs.interview.coupon.core.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import schwarz.jobs.interview.coupon.core.domain.CouponEntity;

public interface CouponRepository extends JpaRepository<CouponEntity, Long> {

    Optional<CouponEntity> findByCode(final String code);

    List<CouponEntity> findByCodeIn(final Collection<String> codes);
}
