package de.schwarz.jobs.interview.coupon.api.coupons.v1;

import de.schwarz.jobs.interview.coupon.api.coupons.CouponsApiConstants;
import de.schwarz.jobs.interview.coupon.api.coupons.v1.dto.WebBasket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = CouponsApiConstants.PATH_API_V1_ROOT)
public interface PostBasketDiscountApiV1 {

  @Operation(
      description = "Apply a discount to a basket by basket id and coupon code."
  )
  @PostMapping(value = CouponsApiConstants.PATH_BASKETS_COUPON_DISCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<WebBasket> applyDiscount(
      @Parameter(description = CouponsApiConstants.PATH_PARAM_BASKET_ID_DOC)
      @PathVariable(CouponsApiConstants.PATH_PARAM_BASKET_ID) Long basketid,
      @Parameter(description = CouponsApiConstants.PATH_PARAM_CODE_DOC)
      @PathVariable(CouponsApiConstants.PATH_PARAM_CODE) String code
  );

}
