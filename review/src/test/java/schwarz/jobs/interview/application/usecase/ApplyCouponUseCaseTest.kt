package schwarz.jobs.interview.application.usecase

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import schwarz.jobs.interview.domain.model.Basket
import schwarz.jobs.interview.domain.repository.CouponRepository
import schwarz.jobs.interview.infrastructure.api.dto.ApplicationRequestDTO
import schwarz.jobs.interview.infrastructure.api.dto.BasketRequestDTO
import java.math.BigDecimal

class ApplyCouponUseCaseTest {
    private val couponRepository: CouponRepository = mockk()

    private val useCase = ApplyCouponUseCase(couponRepository)

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `invoke should apply coupon`() {
        // GIVEN
        val basketRequest = BasketRequestDTO(
            BigDecimal.valueOf(90.0),
            BigDecimal.valueOf(10.0)
        )
        val code = "DISCOUNT10"
        val request = ApplicationRequestDTO(
            code,
            basketRequest,
        )
        val basket = Basket(
            value = request.basket.value,
            appliedDiscount = request.basket.appliedDiscount,
        )

        val expectedResult = Basket(
            value = BigDecimal.valueOf(90.0),
            appliedDiscount = BigDecimal.valueOf(10.0),
            applicationSuccessful = true,
            appliedDiscountValue = BigDecimal.valueOf(81.0)
        )

        every { couponRepository.applyCoupon(basket = basket, code = request.code) } returns expectedResult

        // WHEN
        val result = useCase(request)

        // THEN
        assertEquals(expectedResult.value, result.value)
        verify(exactly = 1) { couponRepository.applyCoupon(basket = basket, code = request.code) }
    }

}