package in.gov.bpm.frontend.controller

import in.gov.bpm.db.entity.Application
import in.gov.bpm.frontend.impl.UserDetails
import in.gov.bpm.frontend.pojo.NotificationRequest
import in.gov.bpm.service.application.api.ApplicationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Created by vaibhav on 21/7/16.
 */
@RestController
@RequestMapping('/notification')
class NotificationController {

    @Autowired
    ApplicationService applicationService;

    @RequestMapping(value = '/', method = RequestMethod.POST)
    Application notify(@RequestBody NotificationRequest notificationRequest) {
        return applicationService.updateStatus(Long.valueOf(notificationRequest.businessKey), 'FINISHED');
    }
}
