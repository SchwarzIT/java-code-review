package schwarz.jobs.interview.infrastructure.persistance.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import schwarz.jobs.interview.infrastructure.persistance.jpa.entity.CouponEntity;


public interface CouponDBRepository extends JpaRepository<CouponEntity, Long> {
    Optional<CouponEntity> findByCodeIgnoreCase(final String code);
    List<CouponEntity> findByCodeIgnoreCaseIn(final List<String> codes);
}
