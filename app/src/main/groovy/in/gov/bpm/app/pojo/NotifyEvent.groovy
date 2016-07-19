package in.gov.bpm.app.pojo

import org.activiti.engine.delegate.event.ActivitiEventType

/**
 * Created by user-1 on 27/6/16.
 */
class NotifyEvent {
    ActivitiEventType type;
    String executionId;
    String processInstanceId;
    String processDefinitionId;
    //Object entity;
    String businessKey;
}
