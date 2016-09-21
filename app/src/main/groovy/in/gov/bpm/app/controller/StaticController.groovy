package in.gov.bpm.app.controller

import org.apache.http.entity.ContentType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
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
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add('Content-Type' ,resolveContentTypeByFileExtension(filename));
            ResponseEntity responseEntity = new ResponseEntity(resourceLoader.getResource("file:" + storagePath + File.separator + filename), headers, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private static String resolveContentTypeByFileExtension(String document) {
        if(document.endsWith('jpg') || document.endsWith('jpeg')) {
            return "image/jpeg";
        }
        if(document.endsWith('png')) {
            return "image/png";
        }
        if(document.endsWith('svg')) {
            return "image/svg+xml";
        }
        if(document.endsWith('gif')) {
            return "image/gif";
        }
        else if(document.endsWith('pdf')) {
            return "application/pdf";
        }
        else if(document.endsWith('txt')) {
            return "text/plain";
        }
        else if(document.endsWith('rtf')) {
            return "application/rtf";
        }
        else if(document.endsWith('doc')) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }
        else if(document.endsWith('docx')) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }
        else if(document.endsWith('ppt')) {
            return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        }
        else if(document.endsWith('pptx')) {
            return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        }
        else if(document.endsWith('html')) {
            return "text/html";
        }else if(document.endsWith('xls')) {
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        }
        else if(document.endsWith('xlsx')) {
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        }
        else if(document.endsWith('csv')) {
            return "text/csv";
        }
        else {
            return '';
        }
    }
}
