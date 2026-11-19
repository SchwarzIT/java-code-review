package de.schwarz.jobs.interview.coupon.api.coupons.v1;

import de.schwarz.jobs.interview.coupon.api.coupons.CouponsApiConstants;
import de.schwarz.jobs.interview.coupon.api.coupons.v1.dto.WebCoupon;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = CouponsApiConstants.PATH_API_V1_ROOT)
public interface PostCouponApiV1 {

  @Operation(
      description = "Create a coupon."
  )
  @PostMapping(value = CouponsApiConstants.PATH_COUPONS, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Void> createCoupon(
      @Valid @RequestBody WebCoupon webCoupon
  );

}
