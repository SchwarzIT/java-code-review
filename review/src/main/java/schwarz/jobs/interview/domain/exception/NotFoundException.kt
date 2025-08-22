package schwarz.jobs.interview.domain.exception

import org.springframework.http.HttpStatus

class NotFoundException(
    errorCode: HttpStatus,
    message: String,
) : CustomException(errorCode = errorCode, message = message)