package de.schwarz.jobs.interview.coupon.app.adapter.out.persistence;

import de.schwarz.jobs.interview.coupon.app.adapter.out.persistence.entity.BasketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketJpaRepository extends JpaRepository<BasketEntity, Long> {

}
