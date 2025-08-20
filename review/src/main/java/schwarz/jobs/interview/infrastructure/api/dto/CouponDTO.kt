package schwarz.jobs.interview.infrastructure.api.dto

import schwarz.jobs.interview.domain.model.Coupon
import java.math.BigDecimal

data class CouponDTO(
    val code: String,
    val discount: BigDecimal,
    val minBasketValue: BigDecimal,
) {
    fun toDomainModel(): Coupon = Coupon(
        code = this.code,
        discount = this.discount,
        minBasketValue = this.minBasketValue,
    )

    companion object {
        fun fromDomain(coupon: Coupon): CouponDTO = CouponDTO(
            code = coupon.code,
            discount = coupon.discount,
            minBasketValue = coupon.minBasketValue,
        )
    }
}