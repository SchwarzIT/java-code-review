package schwarz.jobs.interview.domain.repository

interface CouponRepository {
    fun getCoupon(codes: List<String>): List<String>
}
