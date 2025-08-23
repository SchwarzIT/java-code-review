package schwarz.jobs.interview.infrastructure.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApplicationRequestDTO {

    @NotBlank
    private String code;

    @NotNull
    private BasketRequestDTO basket;

    public String getCode() {
        return code;
    }
    public BasketRequestDTO getBasket() {
        return basket;
    }
}
