package schwarz.jobs.interview.application.usecase

import org.springframework.stereotype.Service
import schwarz.jobs.interview.domain.repository.CouponRepository
import schwarz.jobs.interview.infrastructure.api.dto.CouponDTO

@Service
class GetCouponsUseCase(
    private val couponRepository: CouponRepository,
) {
    operator fun invoke(codes: List<String>): List<CouponDTO> =
        couponRepository.getCoupons(codes).map(CouponDTO::fromDomain)
}