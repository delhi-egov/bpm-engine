package in.gov.bpm.frontend.controller

import com.fasterxml.jackson.databind.ObjectMapper
import in.gov.bpm.db.entity.Application
import in.gov.bpm.db.entity.Document
import in.gov.bpm.db.entity.Form
import in.gov.bpm.frontend.impl.UserDetails
import in.gov.bpm.frontend.pojo.AttachFormRequest
import in.gov.bpm.frontend.pojo.ChangeStageRequest
import in.gov.bpm.frontend.pojo.CompleteApplicationRequest
import in.gov.bpm.frontend.pojo.CompleteTaskRequest
import in.gov.bpm.frontend.pojo.CreateApplicationRequest
import in.gov.bpm.service.application.api.ApplicationService
import in.gov.bpm.shared.exception.InvalidValueException
import in.gov.bpm.shared.exception.OperationFailedException
import in.gov.bpm.shared.pojo.Task
import in.gov.bpm.frontend.pojo.AttachDocumentRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

/**
 * Created by vaibhav on 21/7/16.
 */
@RestController
@RequestMapping('/application')
class ApplicationController {

    @Autowired
    ApplicationService applicationService;

    @Autowired
    ObjectMapper objectMapper;

    @Value('${storage.path}')
    String storagePath;

    @RequestMapping(value = '/create', method = RequestMethod.POST)
    Application create(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CreateApplicationRequest request) {
        return applicationService.createApplication(userDetails.getUser(), request.type);
    }

    @RequestMapping(value = '/changeStage', method = RequestMethod.POST)
    Application changeStage(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ChangeStageRequest request) {
        return applicationService.updateStageAndStatusWithUserAuthorization(userDetails.getUser(), request.applicationId, request.stage, null);
    }

    @RequestMapping(value = '/attachForm', method = RequestMethod.POST)
    Form attachForm(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AttachFormRequest request) {
        return applicationService.attachFormToApplicationWithUserAuthorization(userDetails.getUser(), request.applicationId, request.type, objectMapper.writeValueAsString(request.form));
    }

    @RequestMapping(value = '/attachDocument', method = RequestMethod.POST, consumes = ["multipart/form-data"])
    Document attachDocument(@AuthenticationPrincipal UserDetails userDetails, @RequestPart('form') String form, @RequestPart('file') MultipartFile file) {
        AttachDocumentRequest request = objectMapper.readValue(form, AttachDocumentRequest);
        if(!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                //String name = file.getOriginalFilename() + '.' + userDetails.getPrincipal() + '.' + new Date();
                String name = (new Date()).toString() + '-' + userDetails.getPrincipal() + '-' + file.getOriginalFilename();

                // Create the file on server
                File serverFile = new File(storagePath + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                return applicationService.attachDocumentToApplicationWithUserAuthorization(userDetails.getUser(), request.applicationId, request.type, name);
            }
            catch (Exception e) {
                throw new OperationFailedException(e.message);
            }
        }
        else {
            throw new InvalidValueException('file', 'empty');
        }
    }

    @RequestMapping(value = '/complete', method = RequestMethod.POST)
    Application complete(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CompleteApplicationRequest request) {
        return applicationService.completeApplicationWithUserAuthorization(userDetails.getUser(), request.applicationId);
    }

    @RequestMapping(value = '/{applicationId}/status', method = RequestMethod.GET)
    List<String> getStatus(@AuthenticationPrincipal UserDetails userDetails, @PathVariable('applicationId') Long applicationId) {
        return applicationService.getApplicationStatusWithUserAuthorization(userDetails.getUser(), applicationId);
    }

    @RequestMapping(value = '/{applicationId}/tasks', method = RequestMethod.GET)
    List<Task> getTasks(@AuthenticationPrincipal UserDetails userDetails, @PathVariable('applicationId') Long applicationId) {
        return applicationService.getTasksByApplicationForPortalWithUserAuthorization(userDetails.getUser(), applicationId);
    }

    @RequestMapping(value = '/{applicationId}/task/{taskId}/variables', method = RequestMethod.GET)
    List<Map<String, Object>> getTaskVariables(@AuthenticationPrincipal UserDetails userDetails, @PathVariable('applicationId') Long applicationId, @PathVariable('taskId') String taskId) {
        Map<String, Object> variables = applicationService.getVariablesForTaskAndApplicationWithUserAuthorization(userDetails.getUser(), applicationId, taskId);
        List<Map<String, Object>> variableList = [];
        variables.entrySet().each {
            Map<String, Object> variable = ['name': it.key, 'value': it.value];
            variableList.add(variable);
        }
        return variableList;
    }

    @RequestMapping(value = '/task/complete', method = RequestMethod.POST)
    Task completeTask(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CompleteTaskRequest request) {
        Map<String, Object> variableMap = [:];
        request.variables.each {
            variableMap.put(it.name, it.value);
        }
        return applicationService.completeTaskWithUserAuthorization(userDetails.getUser(), request.applicationId, request.taskId, variableMap);
    }
}
