package in.gov.bpm.shared.exception

import in.gov.bpm.shared.pojo.ApiErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Created by user-1 on 5/4/16.
 */
@ControllerAdvice
class ApplicationExceptionHandlerAdvice {

    @ExceptionHandler(ApplicationException)
    @ResponseBody
    public ResponseEntity<ApiErrorResponse> handleException(ApplicationException e) {
        ApiErrorResponse response = new ApiErrorResponse(message: e.customMessage, code: e.errorCode);
        return new ResponseEntity<ApiErrorResponse>(response, e.httpStatus);
    }
}
