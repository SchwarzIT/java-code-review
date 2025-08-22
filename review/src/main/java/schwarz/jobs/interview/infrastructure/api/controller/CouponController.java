package schwarz.jobs.interview.infrastructure.api.controller;


import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
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
import schwarz.jobs.interview.application.usecase.ApplyCouponUseCase;
import schwarz.jobs.interview.application.usecase.CreateCouponUseCase;
import schwarz.jobs.interview.application.usecase.GetCouponsUseCase;
import schwarz.jobs.interview.infrastructure.api.dto.ApplicationRequestDTO;
import schwarz.jobs.interview.application.dto.BasketDTO;
import schwarz.jobs.interview.application.dto.CouponDTO;
import schwarz.jobs.interview.infrastructure.api.dto.CreateCouponRequestDTO;
import schwarz.jobs.interview.infrastructure.persistance.adapter.CouponDBAdapter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
@Slf4j
public class CouponController {

    private final CouponDBAdapter couponDBAdapter;
    private final GetCouponsUseCase getCouponsUseCase;
    private final CreateCouponUseCase createCouponsUseCase;
    private final ApplyCouponUseCase applyCouponUseCase;

    @Operation(summary = "Applies currently active promotions and coupons from the request to the requested Basket")
    @PostMapping(value = "/apply")
    public ResponseEntity<BasketDTO> apply(
            @RequestBody @Valid final ApplicationRequestDTO applicationRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(applyCouponUseCase.invoke(applicationRequestDTO));
    }

    @Operation(summary = "Create a coupon")
    @PostMapping()
    public ResponseEntity<CouponDTO> create(@RequestBody @Valid final CreateCouponRequestDTO couponDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createCouponsUseCase.invoke(couponDTO));
    }

    @Operation(summary = "Get coupon list")
    @GetMapping()
    public List<CouponDTO> getCoupons(@RequestParam List<String> codes) {
        return getCouponsUseCase.invoke(codes);
    }
}
