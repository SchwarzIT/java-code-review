package schwarz.jobs.interview.coupon.core.services.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import schwarz.jobs.interview.coupon.common.mapper.CouponMapper;
import schwarz.jobs.interview.coupon.core.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.core.services.model.Coupon;
import schwarz.jobs.interview.coupon.core.services.model.CouponFilter;

@RequiredArgsConstructor
@Service
public class CouponServiceImpl implements CouponService {

    // Repositories
    private final CouponRepository couponRepository;

    // Mappers
    private final CouponMapper couponMapper;

    @Override
    public Optional<Coupon> getCouponByCode(final String code) {
        return couponRepository.findByCode(code)
            .map(couponMapper::toCoupon);
    }

    @Override
    public Optional<Coupon> getCouponById(Long id) {
        return couponRepository.findById(id)
            .map(couponMapper::toCoupon);
    }

    @Override
    public Long createCoupon(final Coupon coupon) {
        return couponRepository.save(couponMapper.toCouponEntity(coupon)).getId();
    }

    @Override
    public List<Coupon> filterCoupons(final CouponFilter couponFilter) {
        return couponRepository.findByCodeIn(couponFilter.getCodes())
            .stream()
            .map(couponMapper::toCoupon)
            .toList();
    }
}
