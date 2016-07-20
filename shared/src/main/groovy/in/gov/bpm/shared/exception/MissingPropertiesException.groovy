package in.gov.bpm.shared.exception

import in.gov.bpm.shared.pojo.ApiErrorResponse
import org.springframework.http.HttpStatus

/**
 * Created by user-1 on 5/4/16.
 */
class MissingPropertiesException extends ApplicationException implements Serializable {

    List<String> missingProperties;

    MissingPropertiesException() {
        super(ApiErrorResponse.ApiErrorResponseCode.MISSING_REQUIRED_PROPERTY, '', HttpStatus.BAD_REQUEST);
    }

    MissingPropertiesException(List<String> missingProperties) {
        this();
        this.missingProperties = missingProperties;
        this.customMessage = "Following required properties are missing $missingProperties";
    }
}
