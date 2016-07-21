package in.gov.bpm.shared.exception

import org.springframework.http.HttpStatus
import in.gov.bpm.shared.pojo.ApiErrorResponse

/**
 * Created by vaibhav on 21/7/16.
 */
class TaskAuthorizationException extends ApplicationException implements Serializable {

    TaskAuthorizationException() {
        super(ApiErrorResponse.ApiErrorResponseCode.TASK_AUTHORIZATION_ERROR, "The task you are trying to access does not belong to you", HttpStatus.UNAUTHORIZED);
    }
}
