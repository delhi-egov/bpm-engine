package in.gov.bpm.shared.exception

import in.gov.bpm.shared.pojo.ApiErrorResponse
import org.springframework.http.HttpStatus

/**
 * Created by user-1 on 5/4/16.
 */
class AccountExistsException extends ApplicationException implements Serializable {
    String invalidValueProvided;

    AccountExistsException(String invalidValueProvided) {
        super(ApiErrorResponse.ApiErrorResponseCode.INVALID_DATA, "Account with id $invalidValueProvided already exists.", HttpStatus.BAD_REQUEST);
        this.invalidValueProvided = invalidValueProvided;
    }
    
}
