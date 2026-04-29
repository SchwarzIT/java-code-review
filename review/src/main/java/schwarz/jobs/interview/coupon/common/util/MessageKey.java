package schwarz.jobs.interview.coupon.common.util;

public enum MessageKey {

    // Info
    COUPON_CREATED("coupon.create.success"),
    COUPON_CREATE_FAILED("coupon.create.failed"),
    BAS_COUAPP_FAILED("basket.coupon.application.failed"),

    // Application errors
    APP_ERR_001("app.error.001"),
    APP_ERR_002("app.err.002"),

    // Validation errors for Coupon
    COU_VAL_ERR_001("coupon.code.required"),
    COU_VAL_ERR_002("coupon.discount.required"),
    COU_VAL_ERR_003("coupon.discount.invalid"),
    COU_VAL_ERR_004("coupon.minimumbasket.required"),
    COU_VAL_ERR_005("coupon.minimumbasket.invalid"),
    COU_VAL_ERR_006("coupon.code.invalid"),

    // Validation errors filter coupon
    COU_FIL_ERR_001("coupon.filter.codes.required"),

    // Validation errors, Basket
    BAS_VAL_ERR_001("basket.required"),
    BAS_VAL_ERR_002("basket.value.invalid"),
    BAS_VAL_ERR_003("basket.value.required");

    private final String key;

    MessageKey(String key) {
        this.key = key;
    }

    public String key() {
        return this.key;
    }
}
