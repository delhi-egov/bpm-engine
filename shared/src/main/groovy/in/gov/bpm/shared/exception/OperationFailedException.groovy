package in.gov.bpm.shared.exception

import org.springframework.http.HttpStatus
import in.gov.bpm.shared.pojo.ApiErrorResponse

/**
 * Created by user-1 on 14/4/16.
 */
class OperationFailedException extends ApplicationException implements Serializable {

    OperationFailedException(String customMessage) {
        super(ApiErrorResponse.ApiErrorResponseCode.OPERATION_FAILED, customMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
