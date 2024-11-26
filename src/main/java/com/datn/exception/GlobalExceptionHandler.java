
package com.datn.exception;

import com.datn.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception) {
        log.error("Exception: ", exception);
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getFieldError();

        String enumKey = fieldError.getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;

        try {
            errorCode = ErrorCode.valueOf(enumKey);

            // Sử dụng try-catch để kiểm tra unwrap an toàn
            for (ObjectError error : exception.getBindingResult().getAllErrors()) {
                try {
                    var constraintViolation = error.unwrap(ConstraintViolation.class);
                    attributes = constraintViolation.getConstraintDescriptor().getAttributes();
                    log.info("Constraint attributes: {}", attributes);
                    break; // Dừng lại khi lấy được thông tin constraint đầu tiên
                } catch (ClassCastException e) {
                    log.warn("Error cannot be unwrapped to ConstraintViolation: {}", e.getMessage());
                }
            }

        } catch (IllegalArgumentException e) {
            log.warn("No matching enum value found for key: {}", enumKey, e);
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage()
        );

        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.getOrDefault(MIN_ATTRIBUTE, ""));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
    // them exrption nay
    // xem exception nay lien quan gi toi methodValidation
    @ExceptionHandler(value = ConstraintViolationException.class)
    ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        String errorMessage = "Validation error occurred";
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String enumKey = violation.getMessage(); // Lấy mã lỗi từ message của constraint

            try {
                // Ánh xạ lỗi từ enum
                errorCode = ErrorCode.valueOf(enumKey);
                attributes = violation.getConstraintDescriptor().getAttributes();
                errorMessage = mapAttribute(errorCode.getMessage(), attributes);
                log.info("Constraint attributes: {}", attributes);

                break; // Dừng lại khi lấy được thông tin constraint đầu tiên
            } catch (IllegalArgumentException e) {
                log.warn("No matching enum value found for key: {}", enumKey, e);
            }
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorMessage);

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

}


//@ControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
//    private static final String MIN_ATTRIBUTE = "min";
//
//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
//        log.error("Exception: ", exception);
//        ApiResponse apiResponse = new ApiResponse();
//
//        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
//        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
//
//        return ResponseEntity.badRequest().body(apiResponse);
//    }
//
//    @ExceptionHandler(value = AppException.class)
//    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
//        ErrorCode errorCode = exception.getErrorCode();
//        ApiResponse apiResponse = new ApiResponse();
//
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(errorCode.getMessage());
//
//        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
//    }
//
//    @ExceptionHandler(value = AccessDeniedException.class)
//    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
//        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
//
//        return ResponseEntity.status(errorCode.getStatusCode())
//                .body(ApiResponse.builder()
//                        .code(errorCode.getCode())
//                        .message(errorCode.getMessage())
//                        .build());
//    }
//
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
//
//        FieldError fieldError = exception.getFieldError();
//        if (fieldError == null) {
//            return null;
//        }
//
//        String enumKey = fieldError.getDefaultMessage();
//        ErrorCode errorCode = ErrorCode.INVALID_KEY;
//        Map<String, Object> attributes = null;
//
//        try {
//            errorCode = ErrorCode.valueOf(enumKey);
//
//            var constraintViolation = exception.getBindingResult()
//                    .getAllErrors()
//                    .get(0)  // get first error in list
//                    .unwrap(ConstraintViolation.class);
//
//            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
//            log.info(attributes.toString());
//        } catch (IllegalArgumentException e) {
//            log.warn("No matching enum value found for key: {}", enumKey, e);
//        } catch (ClassCastException e) {
//            log.warn("Cannot unwrap ConstraintViolation: {}", e.getMessage());
//        }
//
//
//        ApiResponse apiResponse = new ApiResponse();
//
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(
//                Objects.nonNull(attributes)
//                        ? mapAttribute(errorCode.getMessage(), attributes)
//                        : errorCode.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//    }
//
//    private String mapAttribute(String message, Map<String, Object> attributes) {
//        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
//
//        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
//    }
//}
