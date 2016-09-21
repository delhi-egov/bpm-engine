package in.gov.bpm.engine.impl

import org.activiti.engine.IdentityService
import org.activiti.engine.RuntimeService
import org.activiti.engine.delegate.DelegateTask
import org.activiti.engine.delegate.TaskListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by vaibhav on 21/09/16.
 */
@Component
class AssigneeResolverTaskListener implements TaskListener {

    @Autowired
    IdentityService identityService;

    @Autowired
    RuntimeService runtimeService;

    @Override
    void notify(DelegateTask delegateTask) {
        String assignee = delegateTask.getAssignee();
        List<String> keys = identityService.getUserInfoKeys(assignee);
        Map<String, Object> userDetails = new HashMap<>();
        keys.each {
            userDetails[it] = identityService.getUserInfo(assignee, it);
        }
        runtimeService.setVariable(delegateTask.getProcessInstanceId(), 'previousAssignee', userDetails);
    }
}
