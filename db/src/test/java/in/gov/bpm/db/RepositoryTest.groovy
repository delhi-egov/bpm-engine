package in.gov.bpm.db

import in.gov.bpm.db.config.DbConfig
import in.gov.bpm.db.entity.Application
import in.gov.bpm.db.entity.User
import in.gov.bpm.db.repository.ApplicationRepository
import in.gov.bpm.db.repository.UserRepository
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import javax.persistence.EntityManager

/**
 * Created by vaibhav on 20/7/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DbConfig)
class RepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void testBasic() {
        User user = new User(firstName: 'Vaibhav',
        lastName: 'Sinha',
        phone: 9686860429,
        email: 'vaibhavsinh@gmail.com',
        password: 'password');

        userRepository.save(user);

        User queried = userRepository.findByPhone(9686860429);
        Assert.assertEquals(user.email, queried.email);
    }

    @Test
    void testDetach() {
        User queried = userRepository.findByPhone(9686860429);
        Assert.assertEquals('vaibhavsinh@gmail.com', queried.email);
        entityManager.detach(queried);
    }

    @Test
    void testQueryOnFk() {
        Application application = new Application(type: 'test4', user: userRepository.findOne(1l));
        applicationRepository.save(application);
        List<Application> applicationList = applicationRepository.findByUser_Id(1l);
        println('ApplicationList is ')
        print(applicationList);
    }
}
