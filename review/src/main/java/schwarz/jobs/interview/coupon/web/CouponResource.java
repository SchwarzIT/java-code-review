package schwarz.jobs.interview.coupon.web;


import static schwarz.jobs.interview.coupon.common.util.MessageKey.COUPON_CREATED;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import schwarz.jobs.interview.coupon.common.mapper.AppResponseDTOMapper;
import schwarz.jobs.interview.coupon.common.mapper.CouponMapper;
import schwarz.jobs.interview.coupon.common.util.Paths;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.web.dto.AppResponseDto;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponFilterDTO;
import schwarz.jobs.interview.coupon.web.dto.CreateCouponDTO;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CouponResource {

    // Services
    private final CouponService couponService;

    // Mappers
    private final CouponMapper couponMapper;
    private final AppResponseDTOMapper responseDTOMapper;

    @PostMapping(Paths.COUPON_CREATE)
    public ResponseEntity<AppResponseDto<Object>> create(@RequestBody @Valid final CreateCouponDTO couponDTO) {
        log.info("CREATE COUPON WITH COUPON_CODE : {}", couponDTO.getCode());

        final Long couponId = couponService.createCoupon(couponMapper.toCoupon(couponDTO));

        log.info("COUPON CREATED SUCCESSFULLY: {}", couponId);

        final URI uri = ServletUriComponentsBuilder.fromUriString(Paths.COUPON_BASE)
            .path("/{id}")
            .buildAndExpand(couponId)
            .toUri();

        return ResponseEntity.created(uri).body(responseDTOMapper.mapSuccess(COUPON_CREATED, couponId));
    }

    @PostMapping(Paths.COUPON_FILTER)
    public ResponseEntity<List<CouponDTO>> filterCoupons(@RequestBody @Valid final CouponFilterDTO couponFilterDTO) {
        final List<CouponDTO> coupons = couponService.filterCoupons(couponMapper.toCouponFilter(couponFilterDTO))
            .stream()
            .map(couponMapper::toCouponDto)
            .toList();
        return ResponseEntity.ok(coupons);
    }
}
