package br.com.itau.challenge.entrypoint.http.handler

import br.com.itau.challenge.entrypoint.http.handler.response.ErrorResponse
import br.com.itau.challenge.exception.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(ClientNotFoundException::class)
    fun clientNotFoundException(ex: ClientNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                ex.message
            )
        )
    }

    @ExceptionHandler(AccountDeactivateException::class)
    fun clientDeactivateException(ex: AccountDeactivateException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            ErrorResponse(
                ex.message
            )
        )
    }

    @ExceptionHandler(InsufficientBalanceException::class)
    fun insufficientBalanceException(ex: InsufficientBalanceException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(
            ErrorResponse(
                ex.message
            )
        )
    }

    @ExceptionHandler(DailyLimitExceededException::class)
    fun dailyLimitExceededException(ex: DailyLimitExceededException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(
            ErrorResponse(
                ex.message
            )
        )
    }

    @ExceptionHandler(NotFoundException::class)
    fun hotFoundException(ex: NotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                ex.message
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun genericException(ex: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
            ErrorResponse(
                "Erro genérico"
            )
        )
    }

    @ExceptionHandler(BadRequestException::class)
    fun badGatewayException(ex: BadRequestException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
            ErrorResponse(
                ex.message
            )
        )
    }
}