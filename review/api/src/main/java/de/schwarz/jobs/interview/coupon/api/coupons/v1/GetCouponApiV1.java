package de.schwarz.jobs.interview.coupon.api.coupons.v1;

import de.schwarz.jobs.interview.coupon.api.coupons.CouponsApiConstants;
import de.schwarz.jobs.interview.coupon.api.coupons.v1.dto.WebCoupon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = CouponsApiConstants.PATH_API_V1_ROOT)
public interface GetCouponApiV1 {

  @Operation(
      description = "Get coupon by code."
  )
  @GetMapping(value = CouponsApiConstants.PATH_COUPONS_BY_CODE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<WebCoupon> getCoupon(
      @Parameter(description = CouponsApiConstants.PATH_PARAM_CODE_DOC)
      @PathVariable(CouponsApiConstants.PATH_PARAM_CODE) String code
  );


  @Operation(
      description = "Get all coupons by code."
  )
  @GetMapping(value = CouponsApiConstants.PATH_COUPONS, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<List<WebCoupon>> getCoupons(
      @Parameter(description = CouponsApiConstants.PATH_PARAM_CODES_DOC)
      @RequestParam List<String> codes
  );

}
