package in.gov.bpm.service.application.impl

import in.gov.bpm.db.entity.Application
import in.gov.bpm.db.entity.Document
import in.gov.bpm.db.entity.Form
import in.gov.bpm.db.entity.User
import in.gov.bpm.db.repository.ApplicationRepository
import in.gov.bpm.db.repository.DocumentRepository
import in.gov.bpm.db.repository.FormRepository
import in.gov.bpm.engine.api.ActivitiService
import in.gov.bpm.service.application.api.ApplicationService
import in.gov.bpm.shared.exception.ApplicationAuthorizationException
import in.gov.bpm.shared.pojo.Task
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.persistence.EntityManager
import java.util.stream.Collectors

/**
 * Created by vaibhav on 21/7/16.
 */
@Component
class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    ActivitiService activitiService;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    FormRepository formRepository;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    EntityManager entityManager;

    @Override
    Application createApplication(User user, String type) {
        Application application = new Application(
                user: user,
                type: type,
                submissionStage: 'NEW',
                executionStatus: 'NOT_SUBMITTED'
        );

        return detachAndReturn(applicationRepository.save(application));
    }

    @Override
    List<Application> getApplicationsForUser(User user) {
        return detachAndReturn(applicationRepository.findByUser_Id(user.id));
    }

    @Override
    Application findApplicationById(Long applicationId) {
        return detachAndReturn(applicationRepository.findOne(applicationId));
    }

    @Override
    Application updateStageAndStatusWithUserAuthorization(User user, Long applicationId, String stage, String status) {
        Application application = checkApplicationBelongsToUser(user, applicationId);
        if(stage != null) {
            application.submissionStage = stage;
        }
        if(status != null) {
            application.executionStatus = status;
        }
        return detachAndReturn(applicationRepository.save(application));
    }

    @Override
    Application updateStatus(Long applicationId, String status) {
        Application application = findApplicationById(applicationId);
        if(application != null) {
            application.executionStatus = status;
            return detachAndReturn(applicationRepository.save(application));
        }
        return application;
    }

    @Override
    Form attachFormToApplicationWithUserAuthorization(User user, Long applicationId, String type, String data) {
        Application application = checkApplicationBelongsToUser(user, applicationId);
        Form form = formRepository.findByTypeAndApplication_Id(type, applicationId);
        if(form != null) {
            form.data = data;
        }
        else {
            form = new Form(
                    application: application,
                    type: type,
                    data: data
            )
        }
        return detachAndReturn(formRepository.save(form));
    }

    @Override
    Document attachDocumentToApplicationWithUserAuthorization(User user, Long applicationId, String type, String path) {
        Application application = checkApplicationBelongsToUser(user, applicationId);
        Document form = documentRepository.findByTypeAndApplication_Id(type, applicationId);
        if(form != null) {
            form.path = path;
        }
        else {
            form = new Document(
                    application: application,
                    type: type,
                    path: path
            )
        }
        return detachAndReturn(documentRepository.save(form));
    }

    @Override
    Application completeApplicationWithUserAuthorization(User user, Long applicationId) {
        Application application = checkApplicationBelongsToUser(user, applicationId);
        List<Form> formList = formRepository.findByApplication_Id(applicationId);
        List<Document> documentList = documentRepository.findByApplication_Id(applicationId);
        Map<String, String> formMap = new HashMap<>();
        Map<String, String> documentMap = new HashMap<>();
        formList.each {
            formMap.put(it.type, it.data);
        }
        documentList.each {
            documentMap.put(it.type, it.path);
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put('user', user);
        variables.put('forms', formMap);
        variables.put('documents', documentMap);
        activitiService.startProcessInstanceByKey(application.type, application.id.toString(), variables);
        return updateStageAndStatusWithUserAuthorization(user, applicationId, 'COMPLETE', 'PROGRESS');
    }

    @Override
    List<String> getApplicationStatusWithUserAuthorization(User user, Long applicationId) {
        Application application = checkApplicationBelongsToUser(user, applicationId);
        return activitiService.getActiveActivitiesByBusinessKey(applicationId.toString());
    }

    @Override
    List<Task> getTasksByApplicationForPortalWithUserAuthorization(User user, Long applicationId) {
        Application application = checkApplicationBelongsToUser(user, applicationId);
        List<org.activiti.engine.task.Task> taskList = activitiService.getTasksForPortalAssigneeByBusinessKey(applicationId.toString());
        return taskList.stream().map({ it ->
            Task task = new Task(
                    name: it.name,
                    id: it.id
            )
        }).collect(Collectors.toList());
    }

    @Override
    Task getTaskOfTypeByApplicationForPortalWithUserAuthorization(User user, Long applicationId, String type) {
        List<Task> taskList = getTasksByApplicationForPortalWithUserAuthorization(user, applicationId);
        taskList.each {
            if(it.name == type) {
                return it;
            }
        }
        return null;
    }

    @Override
    Map<String, Object> getVariablesForTaskAndApplicationWithUserAuthorization(User user, Long applicationId, String taskId) {
        checkTaskBelongsToUserAndApplication(user, applicationId, taskId);
        return activitiService.getAllVariableForTask(taskId);
    }

    @Override
    Task completeTaskWithUserAuthorization(User user, Long applicationId, String taskId, Map<String, Object> variables) {
        checkTaskBelongsToUserAndApplication(user, applicationId, taskId);
        org.activiti.engine.task.Task task = activitiService.completeTask(taskId, variables);
        return new Task(
                id: task.id,
                name: task.name
        );
    }


    private Application detachAndReturn(Application application) {
        if(application != null) {
            entityManager.detach(application);
        }
        return application;
    }

    private List<Application> detachAndReturn(List<Application> applicationList) {
        if(applicationList != null) {
            applicationList.each {
                entityManager.detach(it);
            }
        }
        return applicationList;
    }

    private Form detachAndReturn(Form application) {
        if(application != null) {
            entityManager.detach(application);
        }
        return application;
    }

    /*private List<Form> detachAndReturn(List<Form> applicationList) {
        if(applicationList != null) {
            applicationList.each {
                entityManager.detach(it);
            }
        }
        return applicationList;
    }*/

    private Document detachAndReturn(Document application) {
        if(application != null) {
            entityManager.detach(application);
        }
        return application;
    }

    /*private List<Document> detachAndReturn(List<Document> applicationList) {
        if(applicationList != null) {
            applicationList.each {
                entityManager.detach(it);
            }
        }
        return applicationList;
    }*/

    private Application checkApplicationBelongsToUser(User user, Long applicationId) {
        Application application = applicationRepository.findOne(applicationId);
        if(application == null || application.getUser().getId() != user.getId()) {
            throw new ApplicationAuthorizationException();
        }
        return detachAndReturn(application);
    }

    private void checkTaskBelongsToUserAndApplication(User user, Long applicationId, String taskId) {
        Application application = checkApplicationBelongsToUser(user, applicationId);
        List<org.activiti.engine.task.Task> taskList = activitiService.getTasksForPortalAssigneeByBusinessKey(applicationId.toString());
        Boolean taskBelongsToApplication = false;
        taskList.each {
            if(it.id == taskId) {
                taskBelongsToApplication = true;
            }
        }
        if(!taskBelongsToApplication) {
            throw new ApplicationAuthorizationException();
        }
    }

}
