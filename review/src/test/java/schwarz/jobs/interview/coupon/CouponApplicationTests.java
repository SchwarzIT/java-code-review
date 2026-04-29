package schwarz.jobs.interview.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.experimental.Delegate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import schwarz.jobs.interview.coupon.common.service.MessageService;
import schwarz.jobs.interview.coupon.core.services.BasketService;
import schwarz.jobs.interview.coupon.core.services.CouponService;
import schwarz.jobs.interview.coupon.util.RequestHelper;

@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CouponApplicationTests {

	@Autowired protected CouponService couponService;

	@Autowired protected BasketService basketService;

	@Autowired protected MessageService messageService;

	@Autowired @Delegate protected RequestHelper requestHelper;

	@Test
	void contextLoads() {
		assertThat(couponService).isNotNull();
		assertThat(basketService).isNotNull();
	}
}
