package schwarz.jobs.interview.infrastructure.persistance.adapter

import org.springframework.stereotype.Component
import schwarz.jobs.interview.domain.repository.CouponRepository

@Component
class DBAdapter : CouponRepository {
    override fun getCoupon(codes: List<String>): List<String> = codes
}