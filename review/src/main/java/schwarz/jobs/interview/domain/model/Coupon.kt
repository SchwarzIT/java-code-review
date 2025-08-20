package schwarz.jobs.interview.domain.model

import java.math.BigDecimal

data class Coupon(
    val code: String,
    val discount: BigDecimal,
    val minBasketValue: BigDecimal,
)
