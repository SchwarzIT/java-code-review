package schwarz.jobs.interview.infrastructure.api.controller;

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import schwarz.jobs.interview.application.dto.BasketDTO
import schwarz.jobs.interview.application.dto.CouponDTO
import schwarz.jobs.interview.application.usecase.ApplyCouponUseCase
import schwarz.jobs.interview.application.usecase.CreateCouponUseCase
import schwarz.jobs.interview.application.usecase.GetCouponsUseCase
import schwarz.jobs.interview.infrastructure.api.dto.ApplicationRequestDTO
import schwarz.jobs.interview.infrastructure.api.dto.CreateCouponRequestDTO

class CouponControllerTest {
    private val getCouponsUseCase: GetCouponsUseCase = mockk()
    private val createCouponUseCase: CreateCouponUseCase = mockk()
    private val applyCouponUseCase: ApplyCouponUseCase = mockk()

    private val easyRandom = EasyRandom()

    private val couponController = CouponController(
        getCouponsUseCase,
        createCouponUseCase,
        applyCouponUseCase,
    )

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should get all coupons`() {
        // GIVEN
        val codes = easyRandom.objects(String::class.java, 3).toList()
        val couponDTO = easyRandom.nextObject(CouponDTO::class.java)
        val expectedResult = listOf(couponDTO)

        every { getCouponsUseCase(codes) } returns expectedResult

        // WHEN
        val result = couponController.getCoupons(codes)

        // THEN
        assertEquals(expectedResult, result)
        verify(exactly = 1) { getCouponsUseCase(codes) }
    }

    @Test
    fun `should create a coupon`() {
        // GIVEN
        val couponRequest = easyRandom.nextObject(CreateCouponRequestDTO::class.java)

        val createdCoupon = CouponDTO(
            code = couponRequest.code,
            discount = couponRequest.discount,
            minBasketValue = couponRequest.minBasketValue,
        )

        val expectedResult = ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdCoupon)

        every { createCouponUseCase(couponRequest) } returns createdCoupon

        // WHEN
        val result = couponController.create(couponRequest)

        // THEN
        assertEquals(expectedResult, result)
        verify(exactly = 1) { createCouponUseCase(couponRequest) }
    }

    @Test
    fun `should apply a coupon`() {
        // GIVEN
        val applicationRequestDTO = easyRandom.nextObject(ApplicationRequestDTO::class.java)
        val basketDTO = easyRandom.nextObject(BasketDTO::class.java)
        val expectedResult = ResponseEntity
            .status(HttpStatus.OK)
            .body(basketDTO);

        every { applyCouponUseCase(applicationRequestDTO) } returns basketDTO

        // WHEN
        val result = couponController.apply(applicationRequestDTO)

        // THEN
        assertEquals(expectedResult, result)
        verify(exactly = 1) { applyCouponUseCase(applicationRequestDTO) }
    }
}