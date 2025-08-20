package schwarz.jobs.interview.domain.repository

import schwarz.jobs.interview.domain.model.Coupon
import schwarz.jobs.interview.domain.model.CreateCouponRequest

interface CouponRepository {
    fun getCoupons(codes: List<String>): List<Coupon>
    fun createCoupon(coupon: CreateCouponRequest): Coupon
}
