package in.gov.bpm.engine.api

import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.task.Task

/**
 * Created by vaibhav on 21/7/16.
 */
interface ActivitiService {
    ProcessInstance startProcessInstanceByKey(String key, String businessKey, Map<String, Object> variables);
    List<ProcessInstance> findProcessInstancesByBusinessKey(String key);
    List<ProcessInstance> findProcessInstancesByDefinitionKey(String key);
    List<String> getActiveActivities(String processInstanceId);
    List<String> getActiveActivitiesByBusinessKey(String key);
    List<Task> getTasksForAssigneeByBusinessKey(String assignee, String key);
    List<Task> getTasksForPortalAssigneeByBusinessKey(String key);
    Map<String, Object> getAllVariableForTask(String taskId);
    Task completeTask(String taskId, Map<String, Object> variables);
}
