package schwarz.jobs.interview.coupon.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI couponApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Coupon API")
                        .description("Coupon reservation service")
                        .version("1.0.0"));
    }
}

