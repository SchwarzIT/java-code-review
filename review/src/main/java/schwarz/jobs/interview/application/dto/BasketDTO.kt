package schwarz.jobs.interview.application.dto

import schwarz.jobs.interview.domain.model.Basket
import java.math.BigDecimal

data class BasketDTO(
    val value: BigDecimal,
    val appliedDiscount: BigDecimal,
    val applicationSuccessful: Boolean,
    val appliedDiscountValue: BigDecimal,
) {
    companion object {
        fun fromDomain(basket: Basket): BasketDTO = BasketDTO(
            value = basket.value,
            appliedDiscount = basket.appliedDiscount,
            applicationSuccessful = basket.applicationSuccessful,
            appliedDiscountValue = basket.appliedDiscountValue,
        )
    }
}