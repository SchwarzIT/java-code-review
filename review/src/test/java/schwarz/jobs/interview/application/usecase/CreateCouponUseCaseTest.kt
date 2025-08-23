package schwarz.jobs.interview.application.usecase

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import schwarz.jobs.interview.application.dto.CouponDTO
import schwarz.jobs.interview.domain.model.Coupon
import schwarz.jobs.interview.domain.repository.CouponRepository
import schwarz.jobs.interview.infrastructure.api.dto.CreateCouponRequestDTO
import java.math.BigDecimal

class CreateCouponUseCaseTest {
    private val couponRepository: CouponRepository = mockk()
    private val useCase = CreateCouponUseCase(couponRepository)

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `invoke should create coupon`() {
        // GIVEN
        val createCouponRequestDTO = CreateCouponRequestDTO(
            "DISCOUNT10",
            BigDecimal.valueOf(10.0),
            BigDecimal.valueOf(50.0),
        )

        val coupon = Coupon(
            code = createCouponRequestDTO.code,
            discount = createCouponRequestDTO.discount,
            minBasketValue = createCouponRequestDTO.minBasketValue,
        )

        val expectedResult = CouponDTO.fromDomain(coupon)

        every { couponRepository.createCoupon(createCouponRequestDTO.toDomainModel())  } returns coupon

        // WHEN
        val result = useCase(createCouponRequestDTO)

        // THEN
        assertEquals(expectedResult, result)
        verify(exactly = 1) { couponRepository.createCoupon(createCouponRequestDTO.toDomainModel()) }
    }
}