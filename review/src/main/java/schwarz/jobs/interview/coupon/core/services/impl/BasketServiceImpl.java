package schwarz.jobs.interview.coupon.core.services.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import schwarz.jobs.interview.coupon.core.services.BasketService;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.core.services.model.Basket;

@RequiredArgsConstructor
@Service
public class BasketServiceImpl implements BasketService {

    private final CouponService couponService;

    @Override
    public Optional<Basket> applyCoupon(final Basket basket, final String code) {
        return couponService.getCouponByCode(code)
            .map(coupon -> {
                if (basket.getValue().compareTo(coupon.getMinBasketValue()) >= 0) {
                    basket.applyDiscount(coupon.getDiscount());
                }
                return basket;
            });
    }
}
