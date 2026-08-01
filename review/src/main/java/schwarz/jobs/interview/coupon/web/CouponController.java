package schwarz.jobs.interview.coupon.web;


import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.core.domain.Basket;
import schwarz.jobs.interview.coupon.web.dto.ApplicationRequestDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
@Slf4j
public class CouponController {

    private final CouponService couponService;

    /**
     * Applies the coupon identified by {@code applicationRequestDTO.getCode()} to the given basket.
     *
     * @param applicationRequestDTO the coupon code and basket to apply it to
     * @return 200 with the updated basket if applied, 409 if the basket doesn't qualify and 404 if the coupon code doesn't exist
     */
    @PostMapping(value = "/apply")
    public ResponseEntity<Basket> apply(
        @RequestBody @Valid final ApplicationRequestDTO applicationRequestDTO) {
        log.info("Applying coupon");
        final Basket basket = couponService.apply(applicationRequestDTO.getBasket(), applicationRequestDTO.getCode());
        if (!basket.getApplicationSuccessful()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        log.info("Applied coupon");
        return ResponseEntity.ok().body(basket);
    }

    /**
     * Creates a new coupon.
     *
     * @param couponDTO the coupon data to create
     * @return 200 if coupon created succesfully
     */
    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody @Valid final CouponDTO couponDTO) {
        couponService.createCoupon(couponDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves the coupons matching the given codes.
     *
     * @param couponRequestDTO the list of coupon codes to look up
     * @return the matching coupons; throws CouponNotFoundException if any code doesn't exist
     */
    @PostMapping("/search")
    public List<CouponDTO> getCoupons(@RequestBody @Valid final CouponRequestDTO couponRequestDTO) {
        return couponService.getCoupons(couponRequestDTO);
    }
}
