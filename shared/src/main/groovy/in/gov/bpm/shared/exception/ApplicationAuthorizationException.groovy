package in.gov.bpm.shared.exception

import org.springframework.http.HttpStatus
import in.gov.bpm.shared.pojo.ApiErrorResponse

/**
 * Created by vaibhav on 21/7/16.
 */
class ApplicationAuthorizationException extends ApplicationException implements Serializable {

    ApplicationAuthorizationException() {
        super(ApiErrorResponse.ApiErrorResponseCode.APPLICATION_AUTHORIZATION_ERROR, "The application you are trying to access does not belong to you", HttpStatus.UNAUTHORIZED);
    }
}
