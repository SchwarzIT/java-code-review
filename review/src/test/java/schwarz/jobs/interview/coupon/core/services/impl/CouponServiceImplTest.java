package schwarz.jobs.interview.coupon.core.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationInterceptor;

import javax.validation.ConstraintViolationException;

import schwarz.jobs.interview.coupon.core.exception.CouponNotFoundException;
import schwarz.jobs.interview.coupon.core.exception.MinBasketValueNotMetException;
import schwarz.jobs.interview.coupon.core.persistence.entity.CouponEntity;
import schwarz.jobs.interview.coupon.core.persistence.mapper.CouponDataBaseMapper;
import schwarz.jobs.interview.coupon.core.persistence.repository.CouponRepository;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.core.services.model.command.CreateCouponCommand;
import schwarz.jobs.interview.coupon.core.services.model.domain.BasketDomain;
import schwarz.jobs.interview.coupon.core.services.model.domain.CouponDomain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CouponServiceImpl")
class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponDataBaseMapper couponDataBaseMapper;

    private CouponServiceImpl couponService;

    @BeforeEach
    void setUp() {
        couponService = new CouponServiceImpl(couponRepository, couponDataBaseMapper);
    }

    @Nested
    @DisplayName("apply(basket, code)")
    class Apply {

        @ParameterizedTest(name = "minBasketValue={0}, basketValue={1}")
        @DisplayName("Given a basket that qualifies, when apply, then the discount is applied")
        @MethodSource("schwarz.jobs.interview.coupon.core.services.impl.CouponServiceImplTest#basketsThatQualify")
        void givenQualifyingBasketWhenApplyThenDiscountIsApplied(final BigDecimal minBasketValue, final BigDecimal basketValue) {
            final CouponEntity couponEntity = CouponEntity.builder()
                .id(1L).code("TEST1").discount(BigDecimal.TEN).minBasketValue(minBasketValue)
                .build();
            final CouponDomain couponDomain = CouponDomain.builder()
                .id(1L).code("TEST1").discount(BigDecimal.TEN).minBasketValue(minBasketValue)
                .build();
            final BasketDomain basket = BasketDomain.builder().value(basketValue).build();

            when(couponRepository.findByCodeIgnoreCase("TEST1")).thenReturn(Optional.of(couponEntity));
            when(couponDataBaseMapper.entityToDomain(couponEntity)).thenReturn(couponDomain);

            final BasketDomain result = couponService.apply(basket, "TEST1");

            assertThat(result.isApplicationSuccessful()).isTrue();
            assertThat(result.getAppliedDiscount()).isEqualTo(BigDecimal.TEN);
            assertThat(result.getFinalValue())
                .isEqualByComparingTo(basketValue.multiply(BigDecimal.valueOf(0.9)));
        }

        @Test
        @DisplayName("Given a basket below the coupon's minimum, when apply, then MinBasketValueNotMetException is thrown")
        void givenBasketBelowMinimumWhenApplyThenMinBasketValueNotMetExceptionIsThrown() {
            final CouponEntity couponEntity = CouponEntity.builder()
                .id(1L).code("TEST1").discount(BigDecimal.TEN).minBasketValue(BigDecimal.valueOf(50))
                .build();
            final CouponDomain couponDomain = CouponDomain.builder()
                .id(1L).code("TEST1").discount(BigDecimal.TEN).minBasketValue(BigDecimal.valueOf(50))
                .build();
            final BasketDomain basket = BasketDomain.builder().value(BigDecimal.TEN).build();

            when(couponRepository.findByCodeIgnoreCase("TEST1")).thenReturn(Optional.of(couponEntity));
            when(couponDataBaseMapper.entityToDomain(couponEntity)).thenReturn(couponDomain);

            assertThatThrownBy(() -> couponService.apply(basket, "TEST1"))
                .isInstanceOf(MinBasketValueNotMetException.class)
                .hasMessageContaining("TEST1");
        }

        @Test
        @DisplayName("Given an unknown coupon code, when apply, then CouponNotFoundException is thrown")
        void givenUnknownCouponCodeWhenApplyThenCouponNotFoundExceptionIsThrown() {
            final BasketDomain basket = BasketDomain.builder().value(BigDecimal.TEN).build();

            when(couponRepository.findByCodeIgnoreCase("NOPE")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> couponService.apply(basket, "NOPE"))
                .isInstanceOf(CouponNotFoundException.class)
                .hasMessageContaining("NOPE");
        }
    }

    @Nested
    @DisplayName("createCoupon(command)")
    class CreateCoupon {

        @Test
        @DisplayName("Given a valid command, when createCoupon, then the coupon is saved and returned")
        void givenValidCommandWhenCreateCouponThenCouponIsSavedAndReturned() {
            final CreateCouponCommand command = CreateCouponCommand.builder()
                .code("NEW1").discount(BigDecimal.valueOf(5)).minBasketValue(BigDecimal.TEN)
                .build();
            final CouponEntity entityToSave = CouponEntity.builder().code("NEW1").build();
            final CouponEntity savedEntity = CouponEntity.builder().id(1L).code("NEW1").build();
            final CouponDomain expected = CouponDomain.builder().id(1L).code("NEW1").build();

            when(couponDataBaseMapper.domainToEntity(any(CouponDomain.class))).thenReturn(entityToSave);
            when(couponRepository.save(entityToSave)).thenReturn(savedEntity);
            when(couponDataBaseMapper.entityToDomain(savedEntity)).thenReturn(expected);

            final CouponDomain result = couponService.createCoupon(command);

            assertThat(result).isEqualTo(expected);
            verify(couponRepository, times(1)).save(entityToSave);
        }

        @ParameterizedTest(name = "code=\"{0}\"")
        @DisplayName("Given a code in any case, when createCoupon, then it's uppercased before saving")
        @ValueSource(strings = {"new1", "NEW1", "New1", "nEw1"})
        void givenCodeInAnyCaseWhenCreateCouponThenCodeIsUppercasedBeforeSaving(final String inputCode) {
            final CreateCouponCommand command = CreateCouponCommand.builder()
                .code(inputCode).discount(BigDecimal.valueOf(5)).minBasketValue(BigDecimal.TEN)
                .build();
            final ArgumentCaptor<CouponDomain> domainCaptor = ArgumentCaptor.forClass(CouponDomain.class);

            when(couponDataBaseMapper.domainToEntity(domainCaptor.capture())).thenReturn(CouponEntity.builder().build());
            when(couponRepository.save(any())).thenReturn(CouponEntity.builder().build());
            when(couponDataBaseMapper.entityToDomain(any(CouponEntity.class))).thenReturn(CouponDomain.builder().build());

            couponService.createCoupon(command);

            assertThat(domainCaptor.getValue().getCode()).isEqualTo("NEW1");
        }

        @Test
        @DisplayName("Given a command without a minimum basket value, when createCoupon, then it's saved as null")
        void givenCommandWithoutMinBasketValueWhenCreateCouponThenCouponIsSavedWithNullMinBasketValue() {
            final CreateCouponCommand command = CreateCouponCommand.builder()
                .code("NEW1").discount(BigDecimal.valueOf(5)).minBasketValue(null)
                .build();
            final ArgumentCaptor<CouponDomain> domainCaptor = ArgumentCaptor.forClass(CouponDomain.class);

            when(couponDataBaseMapper.domainToEntity(domainCaptor.capture())).thenReturn(CouponEntity.builder().build());
            when(couponRepository.save(any())).thenReturn(CouponEntity.builder().build());
            when(couponDataBaseMapper.entityToDomain(any(CouponEntity.class))).thenReturn(CouponDomain.builder().build());

            couponService.createCoupon(command);

            assertThat(domainCaptor.getValue().getMinBasketValue()).isNull();
        }
    }

    @Nested
    @DisplayName("getCoupons(codes)")
    class GetCoupons {

        @Test
        @DisplayName("Given all codes exist, when getCoupons, then all matching coupons are returned")
        void givenAllCodesExistWhenGetCouponsThenAllCouponsAreReturned() {
            final CouponEntity entity1 = CouponEntity.builder().id(1L).code("TEST1").build();
            final CouponEntity entity2 = CouponEntity.builder().id(2L).code("TEST2").build();
            final List<CouponDomain> expected = List.of(
                CouponDomain.builder().id(1L).code("TEST1").build(),
                CouponDomain.builder().id(2L).code("TEST2").build());

            when(couponRepository.findByCodeIgnoreCaseIn(List.of("TEST1", "TEST2"))).thenReturn(List.of(entity1, entity2));
            when(couponDataBaseMapper.entityToDomain(List.of(entity1, entity2))).thenReturn(expected);

            final List<CouponDomain> result = couponService.getCoupons(List.of("TEST1", "TEST2"));

            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Given some codes don't exist, when getCoupons, then only the existing coupons are returned")
        void givenSomeCodesDontExistWhenGetCouponsThenOnlyExistingCouponsAreReturned() {
            final CouponEntity entity1 = CouponEntity.builder().id(1L).code("TEST1").build();

            when(couponRepository.findByCodeIgnoreCaseIn(List.of("TEST1", "NOPE"))).thenReturn(List.of(entity1));
            when(couponDataBaseMapper.entityToDomain(List.of(entity1)))
                .thenReturn(List.of(CouponDomain.builder().id(1L).code("TEST1").build()));

            final List<CouponDomain> result = couponService.getCoupons(List.of("TEST1", "NOPE"));

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Given no codes exist, when getCoupons, then an empty list is returned")
        void givenNoCodesExistWhenGetCouponsThenEmptyListIsReturned() {
            when(couponRepository.findByCodeIgnoreCaseIn(List.of("NOPE1", "NOPE2"))).thenReturn(List.of());
            when(couponDataBaseMapper.entityToDomain(List.<CouponEntity>of())).thenReturn(List.of());

            final List<CouponDomain> result = couponService.getCoupons(List.of("NOPE1", "NOPE2"));

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Given an empty codes list, when getCoupons, then every coupon is returned")
        void givenEmptyCodesListWhenGetCouponsThenEveryCouponIsReturned() {
            final CouponEntity entity1 = CouponEntity.builder().id(1L).code("TEST1").build();
            final CouponEntity entity2 = CouponEntity.builder().id(2L).code("TEST2").build();
            final List<CouponDomain> expected = List.of(
                CouponDomain.builder().id(1L).code("TEST1").build(),
                CouponDomain.builder().id(2L).code("TEST2").build());

            when(couponRepository.findAll()).thenReturn(List.of(entity1, entity2));
            when(couponDataBaseMapper.entityToDomain(List.of(entity1, entity2))).thenReturn(expected);

            final List<CouponDomain> result = couponService.getCoupons(List.of());

            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Given null codes, when getCoupons, then every coupon is returned")
        void givenNullCodesWhenGetCouponsThenEveryCouponIsReturned() {
            final CouponEntity entity1 = CouponEntity.builder().id(1L).code("TEST1").build();
            final List<CouponDomain> expected = List.of(CouponDomain.builder().id(1L).code("TEST1").build());

            when(couponRepository.findAll()).thenReturn(List.of(entity1));
            when(couponDataBaseMapper.entityToDomain(List.of(entity1))).thenReturn(expected);

            final List<CouponDomain> result = couponService.getCoupons(null);

            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("method validation (@Validated)")
    class Validation {

        private CouponService validatedCouponService;

        @BeforeEach
        void wrapWithValidationProxy() {
            final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
            validator.afterPropertiesSet();

            final ProxyFactory proxyFactory = new ProxyFactory(couponService);
            proxyFactory.addAdvice(new MethodValidationInterceptor((javax.validation.Validator) validator));
            validatedCouponService = (CouponService) proxyFactory.getProxy();
        }

        @Test
        @DisplayName("Given a negative basket value, when apply, then ConstraintViolationException is thrown")
        void givenNegativeBasketValueWhenApplyThenConstraintViolationExceptionIsThrown() {
            final BasketDomain basket = BasketDomain.builder().value(BigDecimal.valueOf(-1)).build();

            assertThatThrownBy(() -> validatedCouponService.apply(basket, "TEST1"))
                .isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Given a blank code, when apply, then ConstraintViolationException is thrown")
        void givenBlankCodeWhenApplyThenConstraintViolationExceptionIsThrown() {
            final BasketDomain basket = BasketDomain.builder().value(BigDecimal.TEN).build();

            assertThatThrownBy(() -> validatedCouponService.apply(basket, " "))
                .isInstanceOf(ConstraintViolationException.class);
        }
    }

    private static Stream<Arguments> basketsThatQualify() {
        return Stream.of(
            Arguments.of(BigDecimal.valueOf(50), BigDecimal.valueOf(100)),
            Arguments.of(BigDecimal.valueOf(50), BigDecimal.valueOf(50)),
            Arguments.of(null, BigDecimal.ZERO)
        );
    }
}
