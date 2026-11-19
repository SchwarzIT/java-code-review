package de.schwarz.jobs.interview.coupon.app.adapter.in.web;

import de.schwarz.jobs.interview.coupon.api.coupons.CouponsApiConstants;
import de.schwarz.jobs.interview.coupon.api.coupons.v1.PostBasketApiV1;
import de.schwarz.jobs.interview.coupon.api.coupons.v1.dto.WebCreateBasket;
import de.schwarz.jobs.interview.coupon.app.domain.model.Basket;
import de.schwarz.jobs.interview.coupon.app.domain.port.in.PostBasketInPort;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAuthority('SCOPE_coupon_admin')")
@RequiredArgsConstructor
public class PostBasketApiV1Controller implements PostBasketApiV1 {

  private final PostBasketInPort postBasketInPort;

  @Override
  public ResponseEntity<Void> createBasket(WebCreateBasket webCreateBasket) {
    Basket basket = postBasketInPort.createBasket(
        Basket.builder()
            .amount(webCreateBasket.getAmount())
            .build()
    );
    return ResponseEntity
        .created(URI.create(CouponsApiConstants.PATH_API_V1_ROOT + CouponsApiConstants.PATH_BASKETS + "/" + basket.getId()))
        .build();
  }
}
