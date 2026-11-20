package de.schwarz.jobs.interview.coupon.app.adapter.in.web;

import de.schwarz.jobs.interview.coupon.api.coupons.v1.PostBasketDiscountApiV1;
import de.schwarz.jobs.interview.coupon.api.coupons.v1.dto.WebBasket;
import de.schwarz.jobs.interview.coupon.app.adapter.in.web.mapper.WebBasketMapper;
import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;
import de.schwarz.jobs.interview.coupon.app.domain.port.in.PostBasketDiscountInPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAuthority('SCOPE_coupon_admin')")
@RequiredArgsConstructor
public class PostBasketDiscountApiV1Controller implements PostBasketDiscountApiV1 {

  private final PostBasketDiscountInPort postBasketDiscountInPort;
  private final WebBasketMapper webBasketMapper;

  @Override
  public ResponseEntity<WebBasket> applyDiscount(Long basketid, String code) {
    Basket basket = postBasketDiscountInPort.applyDiscount(basketid, code);
    return ResponseEntity.ok(webBasketMapper.mapToWeb(basket));
  }
}
