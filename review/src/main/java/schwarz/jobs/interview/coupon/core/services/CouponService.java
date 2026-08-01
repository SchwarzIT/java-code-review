package schwarz.jobs.interview.coupon.core.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import schwarz.jobs.interview.coupon.core.domain.Coupon;
import schwarz.jobs.interview.coupon.core.exception.CouponNotFoundException;
import schwarz.jobs.interview.coupon.core.exception.DuplicateCouponException;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.domain.Basket;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;

    public Optional<Coupon> getCoupon(final String code) {
        return couponRepository.findByCode(code);
    }

    public Basket apply(final Basket basket, final String code) {

        final Coupon coupon = getCoupon(code)
                .orElseThrow(() -> new CouponNotFoundException(code));

        final BigDecimal minRequired = coupon.getMinBasketValue() != null
                ? coupon.getMinBasketValue()
                : BigDecimal.ZERO;

        final boolean qualifies = basket.getValue().compareTo(BigDecimal.ZERO) > 0
                && basket.getValue().compareTo(minRequired) >= 0;

        if (qualifies) {
            basket.applyDiscount(coupon.getDiscount());
        } else {
            basket.setApplicationSuccessful(false);
        }

        return basket;
    }

    public void createCoupon(final CouponDTO couponDTO) {

        if (couponRepository.findByCode(couponDTO.getCode()).isPresent()) {
            throw new DuplicateCouponException(couponDTO.getCode());
        }

        couponRepository.save(toCoupon(couponDTO));
    }

    private Coupon toCoupon(final CouponDTO couponDTO) {
        return Coupon.builder()
                .code(couponDTO.getCode())
                .discount(couponDTO.getDiscount())
                .minBasketValue(couponDTO.getMinBasketValue())
                .build();
    }

    public List<CouponDTO> getCoupons(final CouponRequestDTO couponRequestDTO) {

        return couponRequestDTO.getCodes().stream()
                .map(code -> couponRepository.findByCode(code)
                        .orElseThrow(() -> new CouponNotFoundException(code)))
                .map(this::toCouponDTO)
                .toList();
    }

    private CouponDTO toCouponDTO(final Coupon coupon) {
        return CouponDTO.builder()
                .code(coupon.getCode())
                .discount(coupon.getDiscount())
                .minBasketValue(coupon.getMinBasketValue())
                .build();
    }
}
