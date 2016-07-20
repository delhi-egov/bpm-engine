package in.gov.bpm.engine.impl

import in.gov.bpm.engine.api.ActivitiService
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.task.Task
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by vaibhav on 21/7/16.
 */
@Component
class ActivitiServiceImpl implements ActivitiService {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Override
    ProcessInstance startProcessInstanceByKey(String key, Map<String, Object> variables) {
        return runtimeService.startProcessInstanceByKey(key, variables);
    }

    @Override
    List<ProcessInstance> findProcessInstancesByBusinessKey(String key) {
        return runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(key).list();
    }

    @Override
    List<ProcessInstance> findProcessInstancesByDefinitionKey(String key) {
        return runtimeService.createProcessInstanceQuery().processDefinitionKey(key).list();
    }

    @Override
    List<String> getActiveActivities(String processInstanceId) {
        return runtimeService.getActiveActivityIds(processInstanceId);
    }

    @Override
    List<String> getActiveActivitiesByBusinessKey(String key) {
        List<ProcessInstance> processInstanceList = findProcessInstancesByBusinessKey(key);
        if(processInstanceList != null && !processInstanceList.isEmpty()) {
            return getActiveActivities(processInstanceList[0].getId());
        }
        return [];
    }

    @Override
    List<Task> getTasksForAssigneeByBusinessKey(String assignee, String key) {
        return taskService.createTaskQuery().taskAssignee(assignee).processInstanceBusinessKey(key).list();
    }

    @Override
    Map<String, Object> getAllVariableForTask(String taskId) {
        return taskService.getVariables(taskId);
    }

    @Override
    Task completeTask(String taskId, Map<String, Object> variables) {
        return taskService.complete(taskId, variables);
    }

}
