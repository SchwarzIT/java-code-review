package schwarz.jobs.interview.infrastructure.persistance.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import schwarz.jobs.interview.infrastructure.persistance.jpa.entity.CouponEntity;


public interface CouponRepository extends JpaRepository<CouponEntity, Long> {

    @Query(value = "SELECT * FROM COUPONS WHERE CODE = :code", nativeQuery = true)
    Optional<CouponEntity> findByCode(final String code);

}
