package schwarz.jobs.interview.infrastructure.persistance.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import schwarz.jobs.interview.domain.model.Coupon;
import schwarz.jobs.interview.domain.model.CreateCouponRequest;
import schwarz.jobs.interview.domain.repository.CouponRepository;
import schwarz.jobs.interview.infrastructure.api.dto.Basket;
import schwarz.jobs.interview.infrastructure.persistance.jpa.entity.CouponEntity;
import schwarz.jobs.interview.infrastructure.persistance.jpa.repository.CouponDBRepository;

@RequiredArgsConstructor
@Component
public class CouponDBAdapter implements CouponRepository {

    private final CouponDBRepository couponDBRepository;

    public Optional<CouponEntity> getCoupon(final String code) {
        return couponDBRepository.findByCodeIgnoreCase(code);
    }

    public Optional<Basket> apply(final Basket basket, final String code) {

        return getCoupon(code).map(coupon -> {

            if (basket.getValue().doubleValue() >= 0) {

                if (basket.getValue().doubleValue() > 0) {

                    basket.applyDiscount(coupon.getDiscount());

                } else if (basket.getValue().doubleValue() == 0) {
                    return basket;
                }

            } else {
                System.out.println("DEBUG: TRIED TO APPLY NEGATIVE DISCOUNT!");
                throw new RuntimeException("Can't apply negative discounts");
            }

            return basket;
        });
    }

    @NotNull
    @Override
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

        final ArrayList<CouponEntity> foundCoupons = new ArrayList<>();

        codes.forEach(code -> foundCoupons.add(couponDBRepository.findByCodeIgnoreCase(code).get()));

        return foundCoupons.stream().map(couponEntity ->
                new Coupon(
                        couponEntity.getCode(),
                        couponEntity.getDiscount(),
                        couponEntity.getMinBasketValue()
                )
        ).toList();
    }
}
