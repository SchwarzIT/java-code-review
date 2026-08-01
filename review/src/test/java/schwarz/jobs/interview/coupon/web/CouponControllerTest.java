package schwarz.jobs.interview.coupon.web;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import schwarz.jobs.interview.coupon.core.exception.CouponNotFoundException;
import schwarz.jobs.interview.coupon.core.exception.DuplicateCouponException;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.core.domain.Basket;
import schwarz.jobs.interview.coupon.web.dto.ApplicationRequestDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponRequestDTO;
import tools.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CouponService couponService;

    @Nested
    class Apply {

        private ApplicationRequestDTO.ApplicationRequestDTOBuilder validApplicationRequest() {
            return ApplicationRequestDTO.builder()
                    .code("1111")
                    .basket(Basket.builder().value(BigDecimal.valueOf(100)).build());
        }

        @Test
        void should_return_200_when_request_omits_computed_fields() throws Exception {
            final Basket appliedBasket = Basket.builder()
                    .value(BigDecimal.valueOf(90))
                    .appliedDiscount(BigDecimal.TEN)
                    .applicationSuccessful(true)
                    .build();

            when(couponService.apply(any(Basket.class), eq("1111"))).thenReturn(appliedBasket);

            mockMvc.perform(post("/api/coupons/apply")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                {"code": "1111", "basket": {"value": 100}}
                                """))
                    .andExpect(status().isOk());
        }

        @Test
        void should_return_200_when_coupon_applied() throws Exception {
            final ApplicationRequestDTO request = validApplicationRequest()
                    .build();

            final Basket appliedBasket = Basket.builder()
                    .value(BigDecimal.valueOf(90))
                    .appliedDiscount(BigDecimal.TEN)
                    .applicationSuccessful(true)
                    .build();

            when(couponService.apply(any(Basket.class), eq("1111"))).thenReturn(appliedBasket);

            mockMvc.perform(post("/api/coupons/apply")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.applicationSuccessful").value(true))
                    .andExpect(jsonPath("$.appliedDiscount").value(10));
        }

        @Test
        void should_return_409_when_basket_does_not_qualify() throws Exception {
            final ApplicationRequestDTO request = validApplicationRequest()
                    .build();

            final Basket notQualified = Basket.builder()
                    .value(BigDecimal.valueOf(10))
                    .applicationSuccessful(false)
                    .build();

            when(couponService.apply(any(Basket.class), eq("1111"))).thenReturn(notQualified);

            mockMvc.perform(post("/api/coupons/apply")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());
        }

        @Test
        void should_return_404_when_coupon_not_found() throws Exception {
            final ApplicationRequestDTO request = validApplicationRequest()
                    .code("does-not-exist")
                    .build();

            when(couponService.apply(any(Basket.class), eq("does-not-exist")))
                    .thenThrow(new CouponNotFoundException("does-not-exist"));

            mockMvc.perform(post("/api/coupons/apply")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));
        }

        @Test
        void should_return_400_when_basket_value_is_negative() throws Exception {
            final ApplicationRequestDTO request = validApplicationRequest()
                    .basket(Basket.builder().value(BigDecimal.valueOf(-1)).build())
                    .build();

            mockMvc.perform(post("/api/coupons/apply")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class Create {

        @Test
        void should_return_200_when_coupon_created() throws Exception {
            final CouponDTO request = CouponDTO.builder()
                    .code("NEW1")
                    .discount(BigDecimal.TEN)
                    .minBasketValue(BigDecimal.valueOf(50))
                    .build();

            mockMvc.perform(post("/api/coupons/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        void should_return_409_when_code_already_exists() throws Exception {
            final CouponDTO request = CouponDTO.builder()
                    .code("TEST1")
                    .discount(BigDecimal.TEN)
                    .build();

            doThrow(new DuplicateCouponException("TEST1"))
                    .when(couponService).createCoupon(any(CouponDTO.class));

            mockMvc.perform(post("/api/coupons/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());
        }

        @Test
        void should_return_400_when_code_is_blank() throws Exception {
            final CouponDTO request = CouponDTO.builder()
                    .code("")
                    .discount(BigDecimal.TEN)
                    .build();

            mockMvc.perform(post("/api/coupons/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class GetCoupons {

        @Test
        void should_return_200_with_matching_coupons() throws Exception {
            final CouponRequestDTO request = CouponRequestDTO.builder()
                    .codes(List.of("1111"))
                    .build();

            when(couponService.getCoupons(any(CouponRequestDTO.class)))
                    .thenReturn(List.of(CouponDTO.builder().code("1111").discount(BigDecimal.TEN).build()));

            mockMvc.perform(post("/api/coupons/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].code").value("1111"));
        }

        @Test
        void should_return_404_when_code_not_found() throws Exception {
            final CouponRequestDTO request = CouponRequestDTO.builder()
                    .codes(List.of("does-not-exist"))
                    .build();

            when(couponService.getCoupons(any(CouponRequestDTO.class)))
                    .thenThrow(new CouponNotFoundException("does-not-exist"));

            mockMvc.perform(post("/api/coupons/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void should_return_400_when_codes_list_is_empty() throws Exception {
            final CouponRequestDTO request = CouponRequestDTO.builder()
                    .codes(List.of())
                    .build();

            mockMvc.perform(post("/api/coupons/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }
}
