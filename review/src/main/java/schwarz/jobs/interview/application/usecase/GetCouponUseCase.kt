package schwarz.jobs.interview.application.usecase

import org.springframework.stereotype.Service
import schwarz.jobs.interview.domain.repository.CouponRepository

@Service
class GetCouponUseCase(
    private val couponRepository: CouponRepository,
) {
    operator fun invoke(codes: List<String>): List<String> = couponRepository.getCoupon(codes)
}