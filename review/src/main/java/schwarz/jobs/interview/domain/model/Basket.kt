package schwarz.jobs.interview.domain.model

import java.math.BigDecimal
import java.math.RoundingMode

data class Basket(
    val value: BigDecimal,
    val appliedDiscount: BigDecimal,
    val applicationSuccessful: Boolean = false,
    val appliedDiscountValue: BigDecimal = BigDecimal.ZERO,
) {
    fun applyDiscount(discount: BigDecimal): Basket = this.copy(
        appliedDiscount = discount,
        applicationSuccessful = true,
        appliedDiscountValue = value.subtract(
            value.multiply(discount.divide(BigDecimal(100), 4, RoundingMode.HALF_UP))
        ).setScale(2, RoundingMode.DOWN),
    )
}
