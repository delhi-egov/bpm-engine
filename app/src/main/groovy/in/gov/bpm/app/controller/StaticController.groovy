package in.gov.bpm.app.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by vaibhav on 23/7/16.
 */
@RestController
@RequestMapping('/file')
class StaticController {

    @Autowired
    ResourceLoader resourceLoader;

    @Value('${storage.path}')
    String storagePath;

    @RequestMapping(value = '/{filename}', method = RequestMethod.GET)
    public ResponseEntity<?> getFile(@PathVariable String filename) {
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + storagePath + File.separator + filename));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
