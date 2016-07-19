package in.gov.bpm.app.impl

import org.activiti.engine.RuntimeService
import org.activiti.engine.delegate.event.ActivitiEntityEvent
import org.activiti.engine.delegate.event.ActivitiEvent
import org.activiti.engine.delegate.event.ActivitiEventListener
import org.activiti.engine.delegate.event.ActivitiEventType
import org.activiti.engine.runtime.ProcessInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.apache.commons.codec.binary.Base64
import in.gov.bpm.app.pojo.NotifyEvent

import javax.annotation.PostConstruct


/**
 * Created by user-1 on 27/6/16.
 */
class RemoteNotifierEventListener implements ActivitiEventListener {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RuntimeService runtimeService;

    String url;
    String username;
    String password;

    HttpHeaders headers;

    RemoteNotifierEventListener(String url, String username, String password, RestTemplate restTemplate, RuntimeService runtimeService) {
        this.url = url
        this.username = username
        this.password = password
        this.runtimeService = runtimeService
        this.restTemplate = restTemplate

        buildBasicRequestHeader();
    }

    @Override
    void onEvent(ActivitiEvent event) {
        if(event.getType() == ActivitiEventType.PROCESS_COMPLETED) {
            HttpEntity httpEntity = new HttpEntity<>(buildEvent(event), headers);
            restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        }
    }

    @Override
    boolean isFailOnException() {
        return false;
    }

    @PostConstruct
    public void buildBasicRequestHeader() {
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + base64Creds);
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    private NotifyEvent buildEvent(ActivitiEvent event) {
        NotifyEvent notifyEvent = new NotifyEvent(type: event.getType(), executionId: event.getExecutionId(), processInstanceId: event.getProcessInstanceId(), processDefinitionId: event.getProcessDefinitionId());
        /*if(event instanceof ActivitiEntityEvent) {
            notifyEvent.entity = event.getEntity();
        }*/
        notifyEvent.businessKey = getBusinessKeyFromProcessInstanceId(event.getProcessInstanceId());
        return notifyEvent;
    }

    private String getBusinessKeyFromProcessInstanceId(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        return processInstance.getBusinessKey();
    }
}
