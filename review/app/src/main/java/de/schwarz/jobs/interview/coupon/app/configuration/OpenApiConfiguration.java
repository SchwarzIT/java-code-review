package de.schwarz.jobs.interview.coupon.app.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

  @Bean
  OpenAPI openApi() {
    return new OpenAPI()
        .info(new Info()
            .title("Coupon API")
            .description("Query and create coupons.")
            .version("v1"));
  }
}

