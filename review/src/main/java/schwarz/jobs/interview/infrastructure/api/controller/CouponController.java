package schwarz.jobs.interview.infrastructure.api.controller;


import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import schwarz.jobs.interview.application.usecase.GetCouponUseCase;
import schwarz.jobs.interview.infrastructure.api.dto.ApplicationRequestDTO;
import schwarz.jobs.interview.infrastructure.api.dto.Basket;
import schwarz.jobs.interview.infrastructure.api.dto.CouponDTO;
import schwarz.jobs.interview.infrastructure.persistance.adapter.CouponDBAdapter;
import schwarz.jobs.interview.infrastructure.persistance.jpa.entity.CouponEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
@Slf4j
public class CouponController {

    private final CouponDBAdapter couponDBAdapter;
    private final GetCouponUseCase getCouponUseCase;

    /**
     * @param applicationRequestDTO
     * @return
     */
    //@ApiOperation(value = "Applies currently active promotions and coupons from the request to the requested Basket - Version 1")
    @PostMapping(value = "/apply")
    public ResponseEntity<Basket> apply(
        //@ApiParam(value = "Provides the necessary basket and customer information required for the coupon application", required = true)
        @RequestBody @Valid final ApplicationRequestDTO applicationRequestDTO) {

        log.info("Applying coupon");

        final Optional<Basket> basket =
            couponDBAdapter.apply(applicationRequestDTO.getBasket(), applicationRequestDTO.getCode());

        if (basket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!applicationRequestDTO.getBasket().isApplicationSuccessful()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        log.info("Applied coupon");

        return ResponseEntity.ok().body(applicationRequestDTO.getBasket());
    }

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody @Valid final CouponDTO couponDTO) {

        final CouponEntity coupon = couponDBAdapter.createCoupon(couponDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public List<CouponEntity> getCoupons(@RequestParam List<String> codes) {
        System.out.println(getCouponUseCase.invoke(codes));
        return couponDBAdapter.getCoupons(codes);
    }
}
