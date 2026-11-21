package com.rakhman.customerinquirytracker.common.error

import com.rakhman.customerinquirytracker.inquiry.InquiryNotFoundException
import com.rakhman.customerinquirytracker.inquiry.InvalidStatusTransitionException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


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


}
