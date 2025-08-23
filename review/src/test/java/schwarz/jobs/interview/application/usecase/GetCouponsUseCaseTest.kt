package schwarz.jobs.interview.application.usecase

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import schwarz.jobs.interview.application.dto.CouponDTO
import schwarz.jobs.interview.domain.model.Coupon
import schwarz.jobs.interview.domain.repository.CouponRepository

class GetCouponsUseCaseTest {
    private val couponRepository: CouponRepository = mockk()

    private val easyRandom = EasyRandom()

    private val useCase = GetCouponsUseCase(couponRepository)

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `invoke should get coupon list`() {
        // GIVEN
        val codes = easyRandom.objects(String::class.java, 3).toList()
        val couponList = easyRandom.objects(Coupon::class.java, 3).toList()

        val expectedValue = couponList.map(CouponDTO::fromDomain)

        every { couponRepository.getCoupons(codes) } returns couponList

        // WHEN
        val result = useCase(codes)

        // THEN
        assertEquals(expectedValue, result)
        verify(exactly = 1) { couponRepository.getCoupons(codes) }
    }
}