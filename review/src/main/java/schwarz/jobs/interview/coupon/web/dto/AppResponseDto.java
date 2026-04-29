package schwarz.jobs.interview.coupon.web.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record AppResponseDto<T>(String code,
                                String message,
                                T data,
                                List<Error> errors) {

    @Builder
    public record Error(String field, String message) { }
}
