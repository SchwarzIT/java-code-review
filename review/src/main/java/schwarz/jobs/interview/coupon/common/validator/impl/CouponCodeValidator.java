package schwarz.jobs.interview.coupon.common.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import schwarz.jobs.interview.coupon.common.validator.ValidCouponCode;
import schwarz.jobs.interview.coupon.core.services.CouponService;

@RequiredArgsConstructor
@Component
public class CouponCodeValidator implements ConstraintValidator<ValidCouponCode, String> {

    private final CouponService couponService;

    @Override
    public void initialize(ValidCouponCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String couponCode, ConstraintValidatorContext context) {
        if (couponCode == null) {
            return true;
        }
        return couponService.getCouponByCode(couponCode).isPresent();
    }
}
