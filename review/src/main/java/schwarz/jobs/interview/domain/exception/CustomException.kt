package schwarz.jobs.interview.domain.exception

import org.springframework.http.HttpStatus

open class CustomException(
    val errorCode: HttpStatus,
    message: String,
) : RuntimeException(message)
