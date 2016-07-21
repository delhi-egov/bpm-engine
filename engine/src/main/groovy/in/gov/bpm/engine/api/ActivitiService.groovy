package in.gov.bpm.engine.api

import in.gov.bpm.shared.pojo.ApplicationProcess
import in.gov.bpm.shared.pojo.Task

/**
 * Created by vaibhav on 21/7/16.
 */
interface ActivitiService {
    ApplicationProcess startProcessInstanceByKey(String key, String businessKey, Map<String, Object> variables);
    List<ApplicationProcess> findProcessInstancesByBusinessKey(String key);
    List<ApplicationProcess> findProcessInstancesByDefinitionKey(String key);
    List<String> getActiveActivities(String processInstanceId);
    List<String> getActiveActivitiesByBusinessKey(String key);
    List<Task> getTasksForAssigneeByBusinessKey(String assignee, String key);
    List<Task> getTasksForPortalAssigneeByBusinessKey(String key);
    Map<String, Object> getAllVariableForTask(String taskId);
    Task completeTask(String taskId, Map<String, Object> variables);
}
