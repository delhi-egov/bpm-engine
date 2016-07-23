package in.gov.bpm.shared.exception

import org.springframework.http.HttpStatus
import in.gov.bpm.shared.pojo.ApiErrorResponse


/**
 * Created by vaibhav on 23/7/16.
 */
class InvalidStateException extends ApplicationException implements Serializable {

    InvalidStateException(String message) {
        super(ApiErrorResponse.ApiErrorResponseCode.INVALID_STATE_ERROR, message, HttpStatus.BAD_REQUEST)
    }
}
