package in.gov.bpm.shared.pojo

/**
 * Created by user-1 on 6/4/16.
 */
class ApiErrorResponse {

    String error;
    Integer code;

    public static class ApiErrorResponseCode {
        public static final Integer MISSING_REQUIRED_PROPERTY = 1;
        public static final Integer INVALID_DATA = 2;
        public static final Integer RESOURCE_EXPIRED = 3;
        public static final Integer OPERATION_FAILED = 4;
        public static final Integer APPLICATION_AUTHORIZATION_ERROR = 5;
        public static final Integer DOCUMENT_AUTHORIZATION_ERROR = 6;
    }
}
