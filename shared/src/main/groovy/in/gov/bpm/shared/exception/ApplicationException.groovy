package in.gov.bpm.shared.exception

import org.springframework.http.HttpStatus

/**
 * Created by vaibhav on 20/7/16.
 */
class ApplicationException extends RuntimeException implements Serializable {
    Integer errorCode;
    String customMessage;
    HttpStatus httpStatus;

    ApplicationException(Integer errorCode, String customMessage, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.customMessage = customMessage;
        this.httpStatus = httpStatus;
    }
}
