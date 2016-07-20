package in.gov.bpm.shared.exception

import org.springframework.http.HttpStatus
import in.gov.bpm.shared.pojo.ApiErrorResponse

/**
 * Created by user-1 on 5/4/16.
 */
class InvalidValueException extends ApplicationException implements Serializable {
    String key;
    String invalidValueProvided;

    InvalidValueException(String key, String invalidValueProvided) {
        super(ApiErrorResponse.ApiErrorResponseCode.INVALID_DATA, "Invalid value $invalidValueProvided provided for field $key.", HttpStatus.BAD_REQUEST);
        this.key = key;
        this.invalidValueProvided = invalidValueProvided;
    }
    
}
