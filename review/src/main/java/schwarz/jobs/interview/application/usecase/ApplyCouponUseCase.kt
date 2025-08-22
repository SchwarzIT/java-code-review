package schwarz.jobs.interview.application.usecase

import org.springframework.stereotype.Service
import schwarz.jobs.interview.domain.model.Basket
import schwarz.jobs.interview.domain.repository.CouponRepository
import schwarz.jobs.interview.infrastructure.api.dto.ApplicationRequestDTO
import schwarz.jobs.interview.infrastructure.api.dto.BasketDTO

@Service
class ApplyCouponUseCase(
    private val couponRepository: CouponRepository,
) {
    operator fun invoke(
        request: ApplicationRequestDTO,
    ): BasketDTO = BasketDTO.fromDomain(
        couponRepository.applyCoupon(
            basket = Basket(
                value = request.basket.value,
                appliedDiscount = request.basket.appliedDiscount,
            ),
            code = request.code,
        )
    )
}