package schwarz.jobs.interview.infrastructure.persistance.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import schwarz.jobs.interview.infrastructure.api.dto.Basket;
import schwarz.jobs.interview.infrastructure.api.dto.CouponDTO;
import schwarz.jobs.interview.infrastructure.api.dto.CouponRequestDTO;
import schwarz.jobs.interview.infrastructure.persistance.jpa.entity.CouponEntity;
import schwarz.jobs.interview.infrastructure.persistance.jpa.repository.CouponRepository;

@Service
@RequiredArgsConstructor
public class CouponDBAdapter {

    private final CouponRepository couponRepository;

    public Optional<CouponEntity> getCoupon(final String code) {
        return couponRepository.findByCode(code);
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

    public CouponEntity createCoupon(final CouponDTO couponDTO) {

        CouponEntity coupon = null;

        try {
            coupon = CouponEntity.builder()
                .code(couponDTO.getCode().toLowerCase())
                .discount(couponDTO.getDiscount())
                .minBasketValue(couponDTO.getMinBasketValue())
                .build();

        } catch (final NullPointerException e) {

            // Don't coupon when code is null
        }

        return couponRepository.save(coupon);
    }

    public List<CouponEntity> getCoupons(List<String> codes) {

        final ArrayList<CouponEntity> foundCoupons = new ArrayList<>();

        codes.forEach(code -> foundCoupons.add(couponRepository.findByCode(code).get()));

        return foundCoupons;
    }
}
