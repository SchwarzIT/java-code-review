package schwarz.jobs.interview.infrastructure.persistance.adapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import schwarz.jobs.interview.domain.exception.BadRequestException;
import schwarz.jobs.interview.domain.exception.NotFoundException;
import schwarz.jobs.interview.domain.model.Basket;
import schwarz.jobs.interview.domain.model.Coupon;
import schwarz.jobs.interview.domain.model.CreateCouponRequest;
import schwarz.jobs.interview.domain.repository.CouponRepository;
import schwarz.jobs.interview.infrastructure.persistance.jpa.entity.CouponEntity;
import schwarz.jobs.interview.infrastructure.persistance.jpa.repository.CouponDBRepository;

@RequiredArgsConstructor
@Component
@Slf4j
public class CouponDBAdapter implements CouponRepository {

    private final CouponDBRepository couponDBRepository;

    public Optional<CouponEntity> getCoupon(final String code) {
        return couponDBRepository.findByCodeIgnoreCase(code);
    }

    @NotNull
    @Override
    public Basket applyCoupon(final Basket basket, final String code) {
        CouponEntity coupon = getCoupon(code).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Coupon not found"));
        checkDiscountValue(basket.getValue().doubleValue());
        return applyDiscountOrFail(basket, coupon.getMinBasketValue());
    }

    private Basket applyDiscountOrFail(Basket basket, BigDecimal minBasketValue){
        if(basket.getValue().doubleValue() < minBasketValue.doubleValue()){
            log.debug("Basket value lower than minimum required for coupon");
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Basket value lower than minimum required for coupon");
        } else {
            checkAppliedDiscount(basket.getAppliedDiscount().doubleValue());
            return basket.applyDiscount(basket.getAppliedDiscount());
        }
    }

    private void checkAppliedDiscount(Double appliedDiscount){
        if(appliedDiscount < 0 || appliedDiscount > 100){
            log.debug("Applied discount must be between 0 and 100");
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Applied discount must be between 0 and 100");
        }
    }

    private void checkDiscountValue(Double value){
        if(value < 0){
            log.debug("Basket value can't be negative");
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Basket value can't be negative");
        }
    }

    @NotNull
    @Override
    @Transactional
    public Coupon createCoupon(CreateCouponRequest request) {
        CouponEntity coupon = CouponEntity.builder()
                .code(request.getCode())
                .discount(request.getDiscount())
                .minBasketValue(request.getMinBasketValue())
                .build();

        CouponEntity savedCoupon = couponDBRepository.save(coupon);
        return new Coupon(
                savedCoupon.getCode(),
                savedCoupon.getDiscount(),
                savedCoupon.getMinBasketValue()
        );
    }

    @NotNull
    @Override
    public List<Coupon> getCoupons(List<String> codes) {
        List<CouponEntity> foundCoupons = couponDBRepository.findByCodeIgnoreCaseIn(codes);

        return foundCoupons.stream().map(couponEntity ->
                new Coupon(
                        couponEntity.getCode(),
                        couponEntity.getDiscount(),
                        couponEntity.getMinBasketValue()
                )
        ).toList();
    }
}
