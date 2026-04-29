package schwarz.jobs.interview.coupon.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static schwarz.jobs.interview.coupon.common.util.MessageKey.APP_ERR_001;
import static schwarz.jobs.interview.coupon.common.util.MessageKey.APP_ERR_002;
import static schwarz.jobs.interview.coupon.common.util.MessageKey.BAS_COUAPP_FAILED;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import schwarz.jobs.interview.coupon.common.mapper.AppResponseDTOMapper;
import schwarz.jobs.interview.coupon.web.dto.AppResponseDto;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class AppExceptionHandler {

    // Mappers
    private final AppResponseDTOMapper responseDTOMapper;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppResponseDto<Void>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException violations) {

        final List<AppResponseDto.Error> validationErrors = violations.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> responseDTOMapper.mapViolation(error.getField(), error.getDefaultMessage()))
            .toList();

        log.info("Validation Failed, Violations :{}", validationErrors);

        return ResponseEntity.status(BAD_REQUEST)
            .body(responseDTOMapper.mapError(APP_ERR_002, validationErrors));
    }

    @ExceptionHandler(CouponApplicationException.class)
    public ResponseEntity<AppResponseDto<Void>> handleCouponApplicationException(final CouponApplicationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(BAD_REQUEST).body(responseDTOMapper.mapResponse(BAS_COUAPP_FAILED, ex.getCouponCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponseDto<Void>> handleException(final Exception ex) {
        log.error("Exception While processing request", ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(responseDTOMapper.mapError(APP_ERR_001, Collections.emptyList()));
    }
}
