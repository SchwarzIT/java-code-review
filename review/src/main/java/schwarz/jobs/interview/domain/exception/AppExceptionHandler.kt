package schwarz.jobs.interview.domain.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.net.URI

private const val BASE_ERROR_URL = "/errors/"
private val camelCaseRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
private const val KEBAB_CASE_REPLACEMENT = "-$0"

private const val EX_ERROR_CODE = "errorCode"
private const val EX_EXTRA_DATA = "data"

@ControllerAdvice
class AppExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(
        ex: NotFoundException,
        request: HttpServletRequest,
    ): ProblemDetail = createProblemDetail(exception = ex, request = request, status = HttpStatus.NOT_FOUND)

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(
        ex: BadRequestException,
        request: HttpServletRequest,
    ): ProblemDetail = createProblemDetail(exception = ex, request = request, status = HttpStatus.BAD_REQUEST)

    private fun createProblemDetail(
        exception: CustomException,
        request: HttpServletRequest,
        status: HttpStatus,
    ): ProblemDetail = ProblemDetail.forStatusAndDetail(status, exception.message).apply {
        type = URI.create(BASE_ERROR_URL + camelCaseToKebabCase(exception.javaClass.simpleName))
        instance = URI.create(request.requestURI)
        title = exception.errorCode.name
        properties = mapOf(EX_ERROR_CODE to exception.errorCode, EX_EXTRA_DATA to exception.message)
    }

    private fun camelCaseToKebabCase(camelCaseString: String): String =
        camelCaseString.replace(camelCaseRegex, KEBAB_CASE_REPLACEMENT).lowercase()
}