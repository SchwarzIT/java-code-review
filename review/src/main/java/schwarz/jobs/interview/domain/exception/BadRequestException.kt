package schwarz.jobs.interview.domain.exception

import org.springframework.http.HttpStatus

class BadRequestException(
    errorCode: HttpStatus,
    message: String,
) : CustomException(errorCode = errorCode, message = message)