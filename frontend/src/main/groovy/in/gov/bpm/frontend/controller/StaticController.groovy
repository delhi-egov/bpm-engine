package in.gov.bpm.frontend.controller

import in.gov.bpm.frontend.impl.UserDetails
import in.gov.bpm.service.application.api.ApplicationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.http.ResponseEntity
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by vaibhav on 21/7/16.
 */
@RestController
@RequestMapping('/file')
class StaticController {

    @Autowired
    ApplicationService applicationService;

    @Autowired
    ResourceLoader resourceLoader;

    @Value('${storage.path}')
    String storagePath;

    @RequestMapping('/{filename}')
    public ResponseEntity<?> getFile(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String filename) {
        applicationService.checkFileBelongsToUser(userDetails.getUser(), filename);
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + storagePath + File.separator + filename));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
