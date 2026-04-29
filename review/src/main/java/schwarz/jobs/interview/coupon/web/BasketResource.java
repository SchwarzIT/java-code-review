package schwarz.jobs.interview.coupon.web;

import jakarta.validation.Valid;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import schwarz.jobs.interview.coupon.common.exception.CouponApplicationException;
import schwarz.jobs.interview.coupon.common.mapper.BasketMapper;
import schwarz.jobs.interview.coupon.common.service.MessageService;
import schwarz.jobs.interview.coupon.common.util.MessageKey;
import schwarz.jobs.interview.coupon.common.util.Paths;
import schwarz.jobs.interview.coupon.core.services.BasketService;
import schwarz.jobs.interview.coupon.core.services.model.Basket;
import schwarz.jobs.interview.coupon.web.dto.ApplyDiscountRequestDTO;
import schwarz.jobs.interview.coupon.web.dto.BasketDTO;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BasketResource {

    // Service
    private final BasketService basketService;
    private final MessageService messageService;

    // Mappers
    private final BasketMapper basketMapper;

    @PostMapping(value = Paths.BASKET_APPLY_COUPON)
    public ResponseEntity<BasketDTO> applyCoupon(@RequestBody @Valid final ApplyDiscountRequestDTO applyDiscountRequestDTO) {

        log.info("Applying coupon with coupon code : {}", applyDiscountRequestDTO.getCode());

        final Basket basketForCouponApplication = basketMapper.toBasket(applyDiscountRequestDTO.getBasket());

        final Basket discountedBasket = basketService.applyCoupon(basketForCouponApplication, applyDiscountRequestDTO.getCode())
            .orElseThrow(complainCouponNotFound(applyDiscountRequestDTO.getCode()));

        if (!discountedBasket.isApplicationSuccessful()) {
            log.info("Coupon code '{}' couldn't be applied for basket", applyDiscountRequestDTO.getCode());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(basketMapper.toBasketDTO(discountedBasket));
        }

        log.info("Successfully applied coupon for the basket items, Coupon code : {}", applyDiscountRequestDTO.getCode());

        return ResponseEntity.ok(basketMapper.toBasketDTO(discountedBasket));
    }

    private Supplier<CouponApplicationException> complainCouponNotFound(final String couponCode) {
        return () -> CouponApplicationException.complain(messageService.message(MessageKey.BAS_COUAPP_FAILED.name(), couponCode), couponCode);
    }
}
