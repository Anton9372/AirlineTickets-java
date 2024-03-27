package airline.tickets.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(final ResourceNotFoundException exception,
                                                                        final WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resources not found: " + exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorDetails> handleBadRequestException(final BadRequestException exception,
                                                                        final WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Resources not found: " + exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDetails> handleGlobalException(final Exception exception, final WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Resources not found: " + exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

/*
@ExceptionHandler({HttpClientErrorException.class})
    public ResponseEntity<ErrorDetails> handleHttpClientErrorException(final HttpClientErrorException exception,
                                                                       final WebRequest request) {
        ErrorDetails errorMessage = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Client error: " + exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception, final WebRequest request) {
        ErrorDetails errorMessage = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid resources input: " + exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpServerErrorException.class})
    public ResponseEntity<ErrorDetails> handleHttpServerErrorException(final Exception exception,
                                                                       final WebRequest request) {
        ErrorDetails errorMessage = new ErrorDetails(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Server error: " + exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
 */
