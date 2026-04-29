package schwarz.jobs.interview.coupon.util;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.client.RestTestClient;
import schwarz.jobs.interview.coupon.common.util.Paths;
import schwarz.jobs.interview.coupon.web.dto.AppResponseDto;
import schwarz.jobs.interview.coupon.web.dto.ApplyDiscountRequestDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponDTO;
import schwarz.jobs.interview.coupon.web.dto.CouponFilterDTO;
import schwarz.jobs.interview.coupon.web.dto.CreateCouponDTO;

@Component
public class RequestHelper {

    @Autowired
    private RestTestClient restTestClient;

    public RestTestClient.ResponseSpec createCoupon(final CreateCouponDTO createCouponDTO) {
        return restTestClient.post()
            .uri(Paths.COUPON_CREATE)
            .body(createCouponDTO)
            .accept(MediaType.APPLICATION_JSON)
            .exchange();
    }

    public List<CouponDTO> filterCoupons(final Set<String> codes) {
        final CouponFilterDTO couponFilterDTO = CouponFilterDTO.builder()
            .codes(codes)
            .build();

        return filterCoupons(couponFilterDTO)
            .expectStatus().isEqualTo(200)
            .returnResult(new ParameterizedTypeReference<List<CouponDTO>>() {})
            .getResponseBody();
    }

    public AppResponseDto<Void> filterCouponsUnsuccessfully(final Set<String> codes) {
        final CouponFilterDTO couponFilterDTO = CouponFilterDTO.builder()
            .codes(codes)
            .build();

        return filterCoupons(couponFilterDTO)
            .expectStatus().isEqualTo(400)
            .returnResult(AppResponseDto.class)
            .getResponseBody();
    }

    public RestTestClient.ResponseSpec applyCouponToBasket(final ApplyDiscountRequestDTO applyDiscountRequestDTO) {
        return restTestClient.post()
            .uri(Paths.BASKET_APPLY_COUPON)
            .body(applyDiscountRequestDTO)
            .accept(MediaType.APPLICATION_JSON)
            .exchange();
    }

    private RestTestClient.ResponseSpec filterCoupons(final CouponFilterDTO couponFilterDTO) {
        return restTestClient.post()
            .uri(Paths.COUPON_FILTER)
            .body(couponFilterDTO)
            .accept(MediaType.APPLICATION_JSON)
            .exchange();
    }
}
