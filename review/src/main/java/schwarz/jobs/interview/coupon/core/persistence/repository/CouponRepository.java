package schwarz.jobs.interview.coupon.core.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import schwarz.jobs.interview.coupon.core.persistence.entity.CouponEntity;

/**
 * JPA Data access for coupons.
 */
public interface CouponRepository extends JpaRepository<CouponEntity, Long> {

    /**
     * Finds a coupon by its code, regardless of case.
     */
    Optional<CouponEntity> findByCodeIgnoreCase(final String code);

    /**
     * Finds all coupons whose code matches any of the given codes, regardless of case.
     */
    List<CouponEntity> findByCodeIgnoreCaseIn(final Collection<String> codes);

}
