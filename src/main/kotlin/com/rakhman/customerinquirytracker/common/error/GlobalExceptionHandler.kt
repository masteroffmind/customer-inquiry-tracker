package com.rakhman.customerinquirytracker.common.error

import com.rakhman.customerinquirytracker.inquiry.InquiryNotFoundException
import com.rakhman.customerinquirytracker.inquiry.InvalidStatusTransitionException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException


/**
 * аннотация для глобальной обработки исключений и общих логик контроллеров в REST API,
 * объединяющая @ControllerAdvice и @ResponseBody
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(InquiryNotFoundException::class)
    fun handleInquiryNotFoundException(exception: InquiryNotFoundException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            exception.message ?: "Inquiry not found"
        )
    }
    /**
     * Когда сервис бросает InquiryNotFoundExcpetion
     * Spring находит метод с аннотацией ExceptionHandler и передает ему исключение
     * в свою очередь метод создает ProblemDetail, а Spring отправляет его клиенту со статусом 404
     */

    @ExceptionHandler(InvalidStatusTransitionException::class)
    fun handleInvalidStatusTransitionException(exception: InvalidStatusTransitionException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT,
            exception.message ?: "Invalid status transition"
        )
    }
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(exception: MethodArgumentNotValidException): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Request validation failed"
        )
        problem.title = "Validation failed"

        val errors = exception.bindingResult.fieldErrors.map { error ->
            mapOf(
                "field" to error.field,
                "message" to error.defaultMessage)

        }
        problem.setProperty("errors", errors)

        return problem
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatchException(exception: MethodArgumentTypeMismatchException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Invalid value for parameter '${exception.name}'"
            /**
             * exception.name - будет содержать имя параметра, который не удалось преобразовать
             */
        )
    }
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleUnreadableBody(exception: HttpMessageNotReadableException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Request body is missing or contains invalid JSON or field values"
        )
    }
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(exception: Exception): ProblemDetail {
        if (exception is ErrorResponse) {
            /**
             * Если полученное исключение реализует ErrorResponse, то возьми из него готовый ProblemDetail
             */
            return exception.body
        }
        logger.error("Unexpected request processing error", exception)
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred"
        )
    }

}
