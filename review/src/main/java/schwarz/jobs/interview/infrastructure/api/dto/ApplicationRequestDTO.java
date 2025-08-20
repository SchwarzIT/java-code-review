package schwarz.jobs.interview.infrastructure.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationRequestDTO {

    @NotBlank
    private String code;

    @NotNull
    private Basket basket;

}
