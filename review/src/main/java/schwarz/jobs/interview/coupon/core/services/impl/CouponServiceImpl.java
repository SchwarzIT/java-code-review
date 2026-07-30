package schwarz.jobs.interview.coupon.core.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import schwarz.jobs.interview.coupon.core.exception.CouponNotFoundException;
import schwarz.jobs.interview.coupon.core.exception.MinBasketValueNotMetException;
import schwarz.jobs.interview.coupon.core.persistence.entity.CouponEntity;
import schwarz.jobs.interview.coupon.core.persistence.mapper.CouponDataBaseMapper;
import schwarz.jobs.interview.coupon.core.persistence.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.core.services.model.command.CreateCouponCommand;
import schwarz.jobs.interview.coupon.core.services.model.domain.BasketDomain;
import schwarz.jobs.interview.coupon.core.services.model.domain.CouponDomain;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of {@link CouponService}.
 */
@Service
@Validated
@Slf4j
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    private final CouponDataBaseMapper couponDataBaseMapper;

    /**
     * Constructor injection.
     */
    public CouponServiceImpl(final CouponRepository couponRepository, final CouponDataBaseMapper couponDataBaseMapper) {
        this.couponRepository = couponRepository;
        this.couponDataBaseMapper = couponDataBaseMapper;
    }

    private Optional<CouponEntity> getCoupon(@NotBlank final String code) {
        return couponRepository.findByCodeIgnoreCase(code);
    }

    @Override
    public BasketDomain apply(@Valid final BasketDomain basket, @NotBlank final String code) {

        final CouponEntity couponEntity = getCoupon(code)
            .orElseThrow(() -> new CouponNotFoundException(code));

        final CouponDomain coupon = couponDataBaseMapper.entityToDomain(couponEntity);

        final BigDecimal minBasketValue = coupon.getMinBasketValue();

        if (minBasketValue != null && basket.getValue().compareTo(minBasketValue) < 0) {
            throw new MinBasketValueNotMetException(code, basket.getValue(), minBasketValue);
        }

        basket.applyDiscount(coupon.getDiscount());

        return basket;
    }

    @Override
    public CouponDomain createCoupon(@Valid final CreateCouponCommand command) {

        final CouponDomain couponDomain = CouponDomain.builder()
            .code(command.getCode().toUpperCase())
            .discount(command.getDiscount())
            .minBasketValue(command.getMinBasketValue())
            .build();

        final CouponEntity couponEntity = couponDataBaseMapper.domainToEntity(couponDomain);

        return couponDataBaseMapper.entityToDomain(couponRepository.save(couponEntity));
    }

    @Override
    public List<CouponDomain> getCoupons(final List<String> codes) {
        final List<CouponEntity> couponEntities = (codes == null || codes.isEmpty())
            ? couponRepository.findAll()
            : couponRepository.findByCodeIgnoreCaseIn(codes);

        return couponDataBaseMapper.entityToDomain(couponEntities);
    }
}
