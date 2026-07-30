package schwarz.jobs.interview.coupon.web.controller;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import schwarz.jobs.interview.coupon.core.persistence.entity.CouponEntity;
import schwarz.jobs.interview.coupon.core.persistence.repository.CouponRepository;
import schwarz.jobs.interview.coupon.web.dto.request.ApplyCouponRequest;
import schwarz.jobs.interview.coupon.web.dto.request.BasketRequest;
import schwarz.jobs.interview.coupon.web.dto.request.CreateCouponRequest;
import schwarz.jobs.interview.coupon.web.dto.response.BasketResponse;
import schwarz.jobs.interview.coupon.web.dto.response.CouponResponse;
import schwarz.jobs.interview.coupon.web.dto.response.ErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Interaaiont Tests that call the real coupon endpoints: applying a coupon to a basket, creating
 * a new one, and looking coupons up by code. Each test cleans up after itself, so it
 * doesn't matter what order they run in.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@DisplayName("CouponResourceController (integration)")
class CouponResourceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CouponRepository couponRepository;

    @Nested
    @DisplayName("POST /api/apply")
    class Apply {

        @Test
        @DisplayName("Given a qualifying basket, when apply, then the discount is applied")
        void givenQualifyingBasketWhenApplyThenDiscountIsApplied() throws Exception {
            final ApplyCouponRequest request = ApplyCouponRequest.builder()
                .code("TEST1")
                .basket(BasketRequest.builder().value(BigDecimal.valueOf(100)).build())
                .build();

            final BasketResponse response = postAndReturn("/api/apply", request, 200, BasketResponse.class);

            assertThat(response.isApplicationSuccessful()).isTrue();
            assertThat(response.getAppliedDiscount()).isEqualByComparingTo(BigDecimal.TEN);
            assertThat(response.getValue()).isEqualByComparingTo(BigDecimal.valueOf(100));
        }

        @Test
        @DisplayName("Given a code in a different case, when apply, then the coupon is still matched")
        void givenCodeInDifferentCaseWhenApplyThenCouponIsMatched() throws Exception {
            final ApplyCouponRequest request = ApplyCouponRequest.builder()
                .code("test1")
                .basket(BasketRequest.builder().value(BigDecimal.valueOf(100)).build())
                .build();

            final BasketResponse response = postAndReturn("/api/apply", request, 200, BasketResponse.class);

            assertThat(response.isApplicationSuccessful()).isTrue();
        }

        @Test
        @DisplayName("Given a basket below the coupon's minimum, when apply, then 409 is returned")
        void givenBasketBelowMinimumWhenApplyThen409IsReturned() throws Exception {
            final ApplyCouponRequest request = ApplyCouponRequest.builder()
                .code("TEST2")
                .basket(BasketRequest.builder().value(BigDecimal.TEN).build())
                .build();

            final ErrorResponse error = postAndReturn("/api/apply", request, 409, ErrorResponse.class);

            assertThat(error.getMessage()).contains("TEST2");
        }

        @Test
        @DisplayName("Given an unknown coupon code, when apply, then 404 is returned")
        void givenUnknownCouponCodeWhenApplyThen404IsReturned() throws Exception {
            final ApplyCouponRequest request = ApplyCouponRequest.builder()
                .code("NOPE")
                .basket(BasketRequest.builder().value(BigDecimal.TEN).build())
                .build();

            final ErrorResponse error = postAndReturn("/api/apply", request, 404, ErrorResponse.class);

            assertThat(error.getMessage()).contains("NOPE");
        }

        @Test
        @DisplayName("Given a blank code, when apply, then 400 is returned with a field error")
        void givenBlankCodeWhenApplyThen400IsReturnedWithFieldError() throws Exception {
            final ApplyCouponRequest request = ApplyCouponRequest.builder()
                .code(" ")
                .basket(BasketRequest.builder().value(BigDecimal.TEN).build())
                .build();

            final ErrorResponse error = postAndReturn("/api/apply", request, 400, ErrorResponse.class);

            assertThat(error.getFieldErrors()).containsKey("code");
        }

        @Test
        @DisplayName("Given a negative basket value, when apply, then 400 is returned with a field error")
        void givenNegativeBasketValueWhenApplyThen400IsReturnedWithFieldError() throws Exception {
            final ApplyCouponRequest request = ApplyCouponRequest.builder()
                .code("TEST1")
                .basket(BasketRequest.builder().value(BigDecimal.valueOf(-1)).build())
                .build();

            final ErrorResponse error = postAndReturn("/api/apply", request, 400, ErrorResponse.class);

            assertThat(error.getFieldErrors()).containsKey("basket.value");
        }
    }

    @Nested
    @DisplayName("POST /api/create")
    class CreateCoupon {

        @Test
        @DisplayName("Given a valid new coupon, when create, then it is persisted and retrievable")
        void givenValidNewCouponWhenCreateThenItIsPersistedAndRetrievable() throws Exception {
            final CreateCouponRequest request = CreateCouponRequest.builder()
                .code("NEW1")
                .discount(BigDecimal.valueOf(5))
                .minBasketValue(BigDecimal.TEN)
                .build();

            final CouponResponse created = postAndReturn("/api/create", request, 201, CouponResponse.class);

            assertThat(created.getCode()).isEqualTo("NEW1");
            assertThat(created.getDiscount()).isEqualByComparingTo(BigDecimal.valueOf(5));
            assertThat(created.getMinBasketValue()).isEqualByComparingTo(BigDecimal.TEN);

            final List<CouponResponse> found = getAndReturnList("/api/coupons?codes=NEW1");
            assertThat(found).extracting(CouponResponse::getCode).containsExactly("NEW1");
        }

        @Test
        @DisplayName("Given a code in lowercase, when create, then it is stored uppercased")
        void givenLowercaseCodeWhenCreateThenItIsStoredUppercased() throws Exception {
            final CreateCouponRequest request = CreateCouponRequest.builder()
                .code("new2")
                .discount(BigDecimal.ONE)
                .build();

            final CouponResponse created = postAndReturn("/api/create", request, 201, CouponResponse.class);

            assertThat(created.getCode()).isEqualTo("NEW2");
        }

        @Test
        @DisplayName("Given a code that already exists, when create, then 409 is returned")
        void givenExistingCodeWhenCreateThen409IsReturned() throws Exception {
            final CreateCouponRequest request = CreateCouponRequest.builder()
                .code("TEST1")
                .discount(BigDecimal.ONE)
                .build();

            final ErrorResponse error = postAndReturn("/api/create", request, 409, ErrorResponse.class);

            assertThat(error.getMessage()).isEqualTo("Coupon code already exists");
        }

        @Test
        @DisplayName("Given a missing discount, when create, then 400 is returned with a field error")
        void givenMissingDiscountWhenCreateThen400IsReturnedWithFieldError() throws Exception {
            final CreateCouponRequest request = CreateCouponRequest.builder()
                .code("NEW3")
                .discount(null)
                .build();

            final ErrorResponse error = postAndReturn("/api/create", request, 400, ErrorResponse.class);

            assertThat(error.getFieldErrors()).containsKey("discount");
        }

        @Test
        @DisplayName("Given a code longer than the database column, when create, then 400 is returned with a field error")
        void givenCodeLongerThanDatabaseColumnWhenCreateThen400IsReturnedWithFieldError() throws Exception {
            final CreateCouponRequest request = CreateCouponRequest.builder()
                .code("A".repeat(251))
                .discount(BigDecimal.ONE)
                .build();

            final ErrorResponse error = postAndReturn("/api/create", request, 400, ErrorResponse.class);

            assertThat(error.getFieldErrors()).containsKey("code");
        }

        @Test
        @DisplayName("Given a discount over 100, when create, then 400 is returned with a field error")
        void givenDiscountOver100WhenCreateThen400IsReturnedWithFieldError() throws Exception {
            final CreateCouponRequest request = CreateCouponRequest.builder()
                .code("NEW4")
                .discount(BigDecimal.valueOf(150))
                .build();

            final ErrorResponse error = postAndReturn("/api/create", request, 400, ErrorResponse.class);

            assertThat(error.getFieldErrors()).containsKey("discount");
        }
    }

    @Nested
    @DisplayName("GET /api/coupons")
    class GetCoupons {

        @Test
        @DisplayName("Given existing codes, when getCoupons, then all matching coupons are returned")
        void givenExistingCodesWhenGetCouponsThenAllMatchingCouponsAreReturned() throws Exception {
            final List<CouponResponse> result = getAndReturnList("/api/coupons?codes=TEST1&codes=TEST2");

            assertThat(result).extracting(CouponResponse::getCode).containsExactlyInAnyOrder("TEST1", "TEST2");
        }

        @Test
        @DisplayName("Given a mix of known and unknown codes, when getCoupons, then only known ones are returned")
        void givenMixOfKnownAndUnknownCodesWhenGetCouponsThenOnlyKnownOnesAreReturned() throws Exception {
            final List<CouponResponse> result = getAndReturnList("/api/coupons?codes=TEST1&codes=NOPE");

            assertThat(result).extracting(CouponResponse::getCode).containsExactly("TEST1");
        }

        @Test
        @DisplayName("Given a repeated code, when getCoupons, then it's returned only once")
        void givenRepeatedCodeWhenGetCouponsThenItIsReturnedOnlyOnce() throws Exception {
            final List<CouponResponse> result = getAndReturnList("/api/coupons?codes=TEST1&codes=TEST1");

            assertThat(result).extracting(CouponResponse::getCode).containsExactly("TEST1");
        }

        @Test
        @DisplayName("Given no codes are provided, when getCoupons, then every coupon is returned")
        void givenNoCodesProvidedWhenGetCouponsThenEveryCouponIsReturned() throws Exception {
            final List<CouponResponse> result = getAndReturnList("/api/coupons");

            assertThat(result).extracting(CouponResponse::getCode)
                .containsExactlyInAnyOrder("TEST1", "TEST2", "TEST3");
        }
    }

    @Nested
    @DisplayName("malformed requests")
    class MalformedRequests {

        @Test
        @DisplayName("Given an unparsable JSON body, when create, then 400 is returned")
        void givenUnparsableJsonBodyWhenCreateThen400IsReturned() throws Exception {
            mockMvc.perform(post("/api/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{not valid json"))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("code uniqueness at the database level")
    class CodeUniqueness {

        @Test
        @DisplayName("Given a code that already exists with different case, when saved directly through the repository, then the database rejects it")
        void givenCodeThatAlreadyExistsWithDifferentCaseWhenSavedDirectlyThenDatabaseRejectsIt() {
            // Bypasses CouponServiceImpl's uppercase normalization on purpose, to prove the
            // uniqueness invariant is enforced by the database itself, not only by that one write path.
            final CouponEntity duplicate = CouponEntity.builder()
                .code("test1")
                .discount(BigDecimal.ONE)
                .build();

            assertThatThrownBy(() -> couponRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
        }
    }

    private <T> T postAndReturn(final String url, final Object body, final int expectedStatus, final Class<T> responseType) throws Exception {
        final MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().is(expectedStatus))
            .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), responseType);
    }

    private List<CouponResponse> getAndReturnList(final String url) throws Exception {
        final MvcResult result = mockMvc.perform(get(url))
            .andExpect(status().isOk())
            .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(),
            objectMapper.getTypeFactory().constructCollectionType(List.class, CouponResponse.class));
    }
}
