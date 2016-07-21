package in.gov.bpm.engine.impl

import org.activiti.engine.IdentityService
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

/**
 * Created by vaibhav on 21/7/16.
 */
@Aspect
class UserInfoInjector {

    @Autowired
    IdentityService identityService;

    @Value('${bpm.username}')
    String bpmUsername;

    @Value('${bpm.password}')
    String bpmPassword;

    @Pointcut("execution(* in.gov.bpm.engine.impl.ActivitiServiceImpl.*(..))")
    public void allMethods() {

    }

    @Before("allMethods()")
    public void inject() {
        identityService.setAuthenticatedUserId(bpmUsername);
    }
}
