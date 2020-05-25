package com.htecgroup.flightadvisor.controller.advice;

import com.htecgroup.flightadvisor.config.ErrorConstants;
import com.htecgroup.flightadvisor.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {

    private static final String TYPE_KEY = "errorType";
    private static final String FIELD_ERRORS_KEY = "fields";

    @Override
    public ResponseEntity<Problem> process(
            ResponseEntity<Problem> entity, @Nonnull NativeWebRequest request
    ) {
        return new ResponseEntity<>(entity.getBody(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, @Nonnull final NativeWebRequest request
    ) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors()
                .stream()
                .map(f -> new FieldErrorVM(f.getObjectName(), f.getField(), f.getDefaultMessage()))
                .collect(Collectors.toList());

        Problem problem = Problem.builder()
                .withTitle("Method argument not valid")
                .withStatus(defaultConstraintViolationStatus())
                .with(TYPE_KEY, ErrorConstants.VALIDATION_ERROR)
                .with(FIELD_ERRORS_KEY, fieldErrors)
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadCredentialsException(
            BadCredentialsException ex, NativeWebRequest request
    ) {
        Problem problem = Problem.builder()
                .withStatus(Status.UNAUTHORIZED)
                .with(TYPE_KEY, ErrorConstants.BAD_CREDENTIALS)
                .build();

        return create(ex, problem, request);
    }

    @ExceptionHandler({UniqueConstraintViolatedException.class})
    public ResponseEntity<Problem> handleUniqueConstraintViolatedException(
            UniqueConstraintViolatedException ex, NativeWebRequest request
    ) {
        Problem problem = Problem.builder()
                .withStatus(Status.CONFLICT)
                .with(TYPE_KEY, ErrorConstants.CONSTRAINT_VALIDATION)
                .withDetail(ex.getMessage())
                .build();

        return create(ex, problem, request);
    }

    @ExceptionHandler({NotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<Problem> handleNotFoundException(
            RuntimeException ex, NativeWebRequest request
    ) {
        Problem problem = Problem.builder()
                .withStatus(Status.NOT_FOUND)
                .with(TYPE_KEY, ErrorConstants.NOT_FOUND)
                .withDetail(ex.getMessage())
                .build();

        return create(ex, problem, request);
    }

    @ExceptionHandler({CSVParseException.class})
    public ResponseEntity<Problem> handleCsvParseException(
            RuntimeException ex, NativeWebRequest request
    ) {
        Problem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .with(TYPE_KEY, ErrorConstants.CSV_PARSE_FAILED)
                .withDetail(ex.getMessage())
                .build();

        return create(ex, problem, request);
    }

    @ExceptionHandler({StartJobException.class})
    public ResponseEntity<Problem> handleStartJobException(
            RuntimeException ex, NativeWebRequest request
    ) {
        Problem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .with(TYPE_KEY, ErrorConstants.CANNOT_START_JOB)
                .withDetail(ex.getMessage())
                .build();

        return create(ex, problem, request);
    }

    @ExceptionHandler({UploadFileException.class})
    public ResponseEntity<Problem> handleUploadFileException(
            RuntimeException ex, NativeWebRequest request
    ) {
        Problem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .with(TYPE_KEY, ErrorConstants.FILE_UPLOAD_FAILED)
                .withDetail(ex.getMessage())
                .build();

        return create(ex, problem, request);
    }

    @ExceptionHandler({UsernameAlreadyTakenException.class})
    public ResponseEntity<Problem> handleUsernameAlreadyTakenException(
            RuntimeException ex, NativeWebRequest request
    ) {
        Problem problem = Problem.builder()
                .withStatus(Status.CONFLICT)
                .with(TYPE_KEY, ErrorConstants.USERNAME_ALREADY_TAKEN)
                .withDetail(ex.getMessage())
                .build();

        return create(ex, problem, request);
    }
}
