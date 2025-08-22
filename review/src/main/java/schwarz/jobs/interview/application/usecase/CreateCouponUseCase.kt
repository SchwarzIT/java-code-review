package schwarz.jobs.interview.application.usecase

import org.springframework.stereotype.Service
import schwarz.jobs.interview.domain.repository.CouponRepository
import schwarz.jobs.interview.application.dto.CouponDTO
import schwarz.jobs.interview.infrastructure.api.dto.CreateCouponRequestDTO

@Service
class CreateCouponUseCase(
    private val couponRepository: CouponRepository,
) {
    operator fun invoke(createCouponRequestDTO: CreateCouponRequestDTO): CouponDTO =
        CouponDTO.fromDomain(couponRepository.createCoupon(createCouponRequestDTO.toDomainModel()))
}