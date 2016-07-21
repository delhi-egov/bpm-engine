package in.gov.bpm.service.application.api

import in.gov.bpm.db.entity.Application
import in.gov.bpm.db.entity.Document
import in.gov.bpm.db.entity.Form
import in.gov.bpm.db.entity.User
import in.gov.bpm.shared.pojo.Task

/**
 * Created by vaibhav on 21/7/16.
 */
interface ApplicationService {
    Application createApplication(User user, String type);
    List<Application> getApplicationsForUser(User user);
    Application findApplicationById(Long applicationId);
    Application updateStageAndStatusWithUserAuthorization(User user, Long applicationId, String stage, String status);
    Application updateStatus(Long applicationId, String status);
    Form attachFormToApplicationWithUserAuthorization(User user, Long applicationId, String type, String data);
    Document attachDocumentToApplicationWithUserAuthorization(User user, Long applicationId, String type, String path);
    Application completeApplicationWithUserAuthorization(User user, Long applicationId);
    List<String> getApplicationStatusWithUserAuthorization(User user, Long applicationId);
    List<Task> getTasksByApplicationForPortalWithUserAuthorization(User user, Long applicationId);
    Task getTaskOfTypeByApplicationForPortalWithUserAuthorization(User user, Long applicationId, String type);
    Map<String, Object> getVariablesForTaskAndApplicationWithUserAuthorization(User user, Long applicationId, String taskId);
    Task completeTaskWithUserAuthorization(User user, Long applicationId, String taskId, Map<String, Object> variables);
}
