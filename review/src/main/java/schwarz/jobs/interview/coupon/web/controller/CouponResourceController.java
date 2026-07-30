package schwarz.jobs.interview.coupon.web.controller;


import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.core.services.model.domain.BasketDomain;
import schwarz.jobs.interview.coupon.core.services.model.domain.CouponDomain;
import schwarz.jobs.interview.coupon.web.dto.request.ApplyCouponRequest;
import schwarz.jobs.interview.coupon.web.dto.request.CreateCouponRequest;
import schwarz.jobs.interview.coupon.web.dto.response.BasketResponse;
import schwarz.jobs.interview.coupon.web.dto.response.CouponResponse;
import schwarz.jobs.interview.coupon.web.mapper.*;

/**
 * REST endpoints for coupons: apply, create and look up.
 */
@Api(tags = "Coupons")
@RestController
@RequestMapping("/api")
@Slf4j
@Validated
public class CouponResourceController {

    private final CouponService couponService;
    private final BasketRequestMapper basketRequestMapper;
    private final BasketResponseMapper basketResponseMapper;
    private final CreateCouponRequestMapper createCouponRequestMapper;
    private final CouponResponseMapper couponResponseMapper;

    /**
     * Constructor injection.
     */
    public CouponResourceController(final CouponService couponService, final BasketRequestMapper basketRequestMapper,
                                    final BasketResponseMapper basketResponseMapper, final CreateCouponRequestMapper createCouponRequestMapper,
                                    final CouponResponseMapper couponResponseMapper) {
        this.couponService = couponService;
        this.basketRequestMapper = basketRequestMapper;
        this.basketResponseMapper = basketResponseMapper;
        this.createCouponRequestMapper = createCouponRequestMapper;
        this.couponResponseMapper = couponResponseMapper;
    }

    /**
     * Applies a coupon to a basket.
     */
    @ApiOperation("Applies a coupon to a basket")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Coupon applied"),
        @ApiResponse(code = 404, message = "Coupon not found"),
        @ApiResponse(code = 409, message = "Basket doesn't reach the coupon's minimum value"),
        @ApiResponse(code = 400, message = "Invalid request")
    })
    @PostMapping(value = "/apply")
    public ResponseEntity<BasketResponse> applyCoupon(
        @ApiParam(value = "Coupon code and basket to apply it to", required = true)
        @RequestBody @Valid final ApplyCouponRequest applyCouponRequest) {

        log.info("Applying coupon");

        final BasketDomain basket =
            couponService.apply(basketRequestMapper.toDomain(applyCouponRequest.getBasket()), applyCouponRequest.getCode());

        log.info("Applied coupon");

        return ResponseEntity.ok().body(basketResponseMapper.toResponse(basket));
    }

    /**
     * Creates a new coupon.
     */
    @ApiOperation("Creates a new coupon")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Coupon created"),
        @ApiResponse(code = 409, message = "Coupon code already exists"),
        @ApiResponse(code = 400, message = "Invalid request")
    })
    @PostMapping("/create")
    public ResponseEntity<CouponResponse> createCoupon(
        @ApiParam(value = "New coupon data", required = true)
        @RequestBody @Valid final CreateCouponRequest createCouponRequest) {

        final CouponDomain coupon = couponService.createCoupon(createCouponRequestMapper.toCommand(createCouponRequest));

        return ResponseEntity.status(HttpStatus.CREATED).body(couponResponseMapper.toResponse(coupon));
    }

    /**
     * Get coupons by code. Unknown codes are skipped, not an error.
     * If no codes are given, returns every coupon.
     */
    @ApiOperation("Get coupons by code")
    @ApiResponse(code = 200, message = "Matching coupons (unknown codes are skipped); all coupons if none given")
    @GetMapping("/coupons")
    public ResponseEntity<List<CouponResponse>> getCoupons(
        @ApiParam(value = "Coupon codes to look up; omit to get every coupon")
        @RequestParam(required = false) final List<String> codes) {

        return ResponseEntity.ok(couponResponseMapper.toResponses(couponService.getCoupons(codes)));
    }
}
