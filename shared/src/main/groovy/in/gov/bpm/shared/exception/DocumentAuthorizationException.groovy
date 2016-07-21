package in.gov.bpm.shared.exception

import org.springframework.http.HttpStatus
import in.gov.bpm.shared.pojo.ApiErrorResponse

/**
 * Created by vaibhav on 21/7/16.
 */
class DocumentAuthorizationException extends ApplicationException implements Serializable {

    DocumentAuthorizationException() {
        super(ApiErrorResponse.ApiErrorResponseCode.DOCUMENT_AUTHORIZATION_ERROR, "The document you are trying to access does not belong to you", HttpStatus.UNAUTHORIZED);

    }
}
