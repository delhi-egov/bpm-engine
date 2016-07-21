package in.gov.bpm.frontend.pojo

/**
 * Created by vaibhav on 21/7/16.
 */
class CompleteTaskRequest {
    Long applicationId;
    String taskId;
    List<Map<String, Object>> variables;
}
