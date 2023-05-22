package org.reckful.archive.twitter.server.configuration

import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import io.swagger.v3.oas.annotations.media.Content
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import kotlin.IllegalArgumentException as KotlinIllegalArgumentException
import java.lang.IllegalArgumentException as JavaIllegalArgumentException

data class Error(val errorMessage: String?)

@RestControllerAdvice
class ControllerErrorHandler {

    @ResponseBody
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ApiResponse(
        responseCode = "422",
        description = "An illegal argument",
        content = [Content(/* intentionally blank to override DTO showing */)]
    )
    @ExceptionHandler(value = [KotlinIllegalArgumentException::class, JavaIllegalArgumentException::class])
    fun handleIllegalArgumentException(exception: Exception): Error {
        return Error(exception.message)
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
    @ExceptionHandler(value = [NotImplementedError::class])
    @ApiResponse(
        responseCode = "501",
        description = "A specific operation has not been implemented",
        content = [Content(/* intentionally blank to override DTO showing */)]
    )
    fun handleNotImplementedError(error: NotImplementedError): Error {
        return Error(error.message)
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(
        responseCode = "500",
        description = "An internal server error",
        content = [Content(/* intentionally blank to override DTO showing */)]
    )
    @ExceptionHandler(value = [Exception::class])
    fun handleIllegalStateException(exception: Exception): Error {
        return Error(exception.message)
    }
}
