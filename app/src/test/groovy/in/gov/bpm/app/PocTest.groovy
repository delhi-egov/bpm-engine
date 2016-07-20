package in.gov.bpm.app


import org.activiti.engine.RepositoryService
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.task.Task
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import in.gov.bpm.engine.config.ActivitiEngineConfiguration

/**
 * Created by user-1 on 26/6/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = [ActivitiEngineConfiguration])
class PocTest {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    RepositoryService repositoryService;

    @Test
    public void passTest() {
        /*repositoryService.createDeployment()
                .addClasspathResource("labor.bpmn20.xml")
                .deploy();*/

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process");

        //Scrutiny
        Task task = taskService.createTaskQuery().singleResult();
        runtimeService.setVariable(processInstance.getId(), "hasDiscrepancy", false);
        taskService.claim(task.getId(), "fozzie");
        taskService.complete(task.getId());

        //Report submission
        task = taskService.createTaskQuery().singleResult();
        taskService.claim(task.getId(), "fozzie");
        taskService.complete(task.getId());

        //Decision
        task = taskService.createTaskQuery().singleResult();
        runtimeService.setVariable(processInstance.getId(), "accepted", true);
        taskService.claim(task.getId(), "gonzo");
        taskService.complete(task.getId());
    }
}
