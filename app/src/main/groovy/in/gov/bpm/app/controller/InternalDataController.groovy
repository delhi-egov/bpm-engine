package in.gov.bpm.app.controller

import com.fasterxml.jackson.databind.ObjectMapper
import in.gov.bpm.app.pojo.AttachDocumentRequest
import in.gov.bpm.app.pojo.AttachFormRequest
import in.gov.bpm.app.pojo.InternalDocumentVariable
import in.gov.bpm.app.pojo.InternalFormVariable
import in.gov.bpm.app.pojo.User
import in.gov.bpm.shared.exception.InvalidValueException
import in.gov.bpm.shared.exception.OperationFailedException
import org.activiti.engine.IdentityService
import org.activiti.engine.RuntimeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

import java.util.stream.Collectors

/**
 * Created by vaibhav on 3/8/16.
 */
@RestController
@RequestMapping('/data')
class InternalDataController {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    IdentityService identityService;

    @Autowired
    ObjectMapper objectMapper;

    @Value('${storage.path}')
    String storagePath;

    @RequestMapping(value = '/attachForm', method = RequestMethod.POST)
    Boolean attachForm(@AuthenticationPrincipal UsernamePasswordAuthenticationToken userDetails, @RequestBody AttachFormRequest request) {
        Map<String, Object> internalForms = (Map<String, Object>) runtimeService.getVariable(request.processInstanceId, 'internalForms');
        if(internalForms == null) {
            internalForms = new HashMap<>();
        }
        InternalFormVariable formVariable = new InternalFormVariable(
                data: request.data,
                user: getApiUserForAuthToken(userDetails),
                addedAt: new Date()
        );
        internalForms.put(request.type, formVariable);
        runtimeService.setVariable(request.processInstanceId, 'internalForms', internalForms);
        return true;
    }

    @RequestMapping(value = '/attachDocument', method = RequestMethod.POST, consumes = ["multipart/form-data"])
    Boolean attachDocument(@AuthenticationPrincipal UsernamePasswordAuthenticationToken userDetails, @RequestParam('form') String form, @RequestParam('file') MultipartFile file) {
        AttachDocumentRequest request = objectMapper.readValue(form, AttachDocumentRequest);
        if(!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                String name = (new Date()).getTime() + '-' + userDetails.getPrincipal() + '-' + file.getOriginalFilename();

                // Create the file on server
                File serverFile = new File(storagePath + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                Map<String, Object> internalDocuments = (Map<String, Object>) runtimeService.getVariable(request.processInstanceId, 'internalDocuments');
                if(internalDocuments == null) {
                    internalDocuments = new HashMap<>();
                }
                InternalDocumentVariable documentVariable = new InternalDocumentVariable(
                        path: name,
                        user: getApiUserForAuthToken(userDetails),
                        addedAt: new Date()
                );
                internalDocuments.put(request.type, documentVariable);
                runtimeService.setVariable(request.processInstanceId, 'internalDocuments', internalDocuments);
                return true;

            }
            catch (Exception e) {
                throw new OperationFailedException(e.message);
            }
        }
        else {
            throw new InvalidValueException('file', 'empty');
        }
    }

    private User getApiUserForAuthToken(UsernamePasswordAuthenticationToken userDetails) {
        org.activiti.engine.identity.User user = identityService.createUserQuery().userId(userDetails.principal.toString()).singleResult();
        User apiUser = new User(
                firstName: user.firstName,
                lastName: user.lastName,
                username: user.id,
                email: user.email
        );

        List<String> groups = identityService.createGroupQuery().groupMember(user.id).list().stream().map({group -> group.id}).collect(Collectors.toList());
        apiUser.groups = groups;
        return apiUser;
    }
}
