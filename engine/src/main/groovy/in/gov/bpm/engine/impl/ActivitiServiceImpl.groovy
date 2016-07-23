package in.gov.bpm.engine.impl

import in.gov.bpm.engine.api.ActivitiService
import in.gov.bpm.shared.pojo.ApplicationProcess
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.runtime.ProcessInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import in.gov.bpm.shared.pojo.Task

import java.util.stream.Collectors

/**
 * Created by vaibhav on 21/7/16.
 */
@Component
class ActivitiServiceImpl implements ActivitiService {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Value('${bpm.username}')
    String bpmUsername;

    @Value('${bpm.password}')
    String bpmPassword;

    @Override
    ApplicationProcess startProcessInstanceByKey(String key, String businessKey, Map<String, Object> variables) {
        return getApiProcess(runtimeService.startProcessInstanceByKey(key, businessKey, variables));
    }

    @Override
    List<ApplicationProcess> findProcessInstancesByBusinessKey(String key) {
        return runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(key).list().stream().map({
            it -> getApiProcess(it);
        }).collect(Collectors.toList());
    }

    @Override
    List<ApplicationProcess> findProcessInstancesByDefinitionKey(String key) {
        return runtimeService.createProcessInstanceQuery().processDefinitionKey(key).list().stream().map({
            it -> getApiProcess(it);
        }).collect(Collectors.toList());
    }

    @Override
    List<String> getActiveActivities(String processInstanceId) {
        return runtimeService.getActiveActivityIds(processInstanceId);
    }

    @Override
    List<String> getActiveActivitiesByBusinessKey(String key) {
        List<ApplicationProcess> processInstanceList = findProcessInstancesByBusinessKey(key);
        if(processInstanceList != null && !processInstanceList.isEmpty()) {
            return getActiveActivities(processInstanceList[0].getProcessInstanceId());
        }
        return [];
    }

    @Override
    List<Task> getTasksForAssigneeByBusinessKey(String assignee, String key) {
        return taskService.createTaskQuery().taskAssignee(assignee).processInstanceBusinessKey(key).list().stream().map({
            it -> getApiTask(it);
        }).collect(Collectors.toList());
    }

    @Override
    List<Task> getTasksForPortalAssigneeByBusinessKey(String key) {
        return getTasksForAssigneeByBusinessKey(bpmUsername, key);
    }

    @Override
    Map<String, Object> getAllVariableForTask(String taskId) {
        return taskService.getVariables(taskId);
    }

    @Override
    Task completeTask(String taskId, Map<String, Object> variables) {
        return getApiTask(taskService.complete(taskId, variables));
    }

    @Override
    void setVariablesByBusinessKey(String key, Map<String, Object> variables) {
        List<ApplicationProcess> processInstanceList = findProcessInstancesByBusinessKey(key);
        if(processInstanceList != null && !processInstanceList.isEmpty()) {
            runtimeService.setVariables(processInstanceList[0].processInstanceId, variables);
        }
    }

    private static Task getApiTask(org.activiti.engine.task.Task task) {
        Task apiTask = null;
        if(task != null) {
            apiTask = new Task(
                    name: task.name,
                    id: task.id,
                    assignee: task.assignee
            );
        }
        return apiTask;
    }

    private static ApplicationProcess getApiProcess(ProcessInstance processInstance) {
        ApplicationProcess applicationProcess = null;
        if(processInstance != null) {
            applicationProcess = new ApplicationProcess(
                    processInstanceId: processInstance.processInstanceId,
                    businessKey: processInstance.businessKey
            );
        }
        return applicationProcess;
    }
}
