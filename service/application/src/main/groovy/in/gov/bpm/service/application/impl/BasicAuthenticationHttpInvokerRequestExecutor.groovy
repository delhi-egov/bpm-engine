package in.gov.bpm.service.application.impl

import org.apache.commons.httpclient.methods.PostMethod
import org.apache.http.client.methods.HttpPost
import org.springframework.beans.factory.annotation.Value
import org.springframework.remoting.httpinvoker.HttpComponentsHttpInvokerRequestExecutor
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration
import org.springframework.stereotype.Component
import org.apache.commons.codec.binary.Base64

import javax.annotation.PostConstruct

/**
 * Created by vaibhav on 21/7/16.
 */
@Component
class BasicAuthenticationHttpInvokerRequestExecutor extends HttpComponentsHttpInvokerRequestExecutor {

    @Value('${bpm.username}')
    String bpmUsername;

    @Value('${bpm.password}')
    String bpmPassword;

    String token;

    @Override
    protected HttpPost createHttpPost(HttpInvokerClientConfiguration config) throws IOException {
        HttpPost httpPost = super.createHttpPost(config);
        httpPost.setHeader("Authorization", "Basic " + token);
        return httpPost;
    }

    @PostConstruct
    public void createToken() {
        String base64 = bpmUsername + ":" + bpmPassword;
        token = new String(Base64.encodeBase64(base64.getBytes()));
    }
}
