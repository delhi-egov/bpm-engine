package in.gov.bpm.engine.config

import com.mysql.jdbc.Driver
import in.gov.bpm.engine.impl.UserInfoInjector
import org.activiti.engine.*
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.activiti.engine.impl.history.HistoryLevel
import org.activiti.spring.ProcessEngineFactoryBean
import org.activiti.spring.SpringProcessEngineConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.web.client.RestTemplate

import javax.sql.DataSource

/**
 * Created by user-1 on 24/6/16.
 */
@Configuration
@PropertySources(@PropertySource("file:/egov.properties"))
@Import(value = [RestConfig, JacksonConfiguration])
@ComponentScan(basePackages = "in.gov.bpm.engine.impl")
@EnableAspectJAutoProxy
class ActivitiEngineConfiguration {

    @Value('${jdbc.url}')
    String jdbcUrl;

    @Value('${jdbc.username}')
    String jdbcUsername;

    @Value('${jdbc.password}')
    String jdbcPassword;

    @Value('${email.enable}')
    Boolean emailEnable;

    @Value('${email.host}')
    String emailHost;

    @Value('${email.port}')
    Integer emailPort;

    @Value('${email.username}')
    String emailUsername;

    @Value('${email.password}')
    String emailPassword;

    @Value('${email.useSSL}')
    Boolean emailUseSSL;

    @Value('${email.useTLS}')
    Boolean emailUseTLS;


    @Bean(name = "processEngineDataSource")
    public DataSource dataSource() {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(Driver);

        // Connection settings
        ds.setUrl(jdbcUrl);
        ds.setUsername(jdbcUsername);
        ds.setPassword(jdbcPassword)

        return ds;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(name="processEngineFactoryBean")
    public ProcessEngineFactoryBean processEngineFactoryBean() {
        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
        return factoryBean;
    }

    @Bean(name="processEngine")
    public ProcessEngine processEngine() {
        try {
            return processEngineFactoryBean().getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean(name="processEngineConfiguration")
    public ProcessEngineConfigurationImpl processEngineConfiguration() {
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDataSource(dataSource());
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        processEngineConfiguration.setTransactionManager(annotationDrivenTransactionManager());
        processEngineConfiguration.setJobExecutorActivate(false);
        processEngineConfiguration.setAsyncExecutorEnabled(true);
        processEngineConfiguration.setAsyncExecutorActivate(false);
        processEngineConfiguration.setHistoryLevel(HistoryLevel.FULL);
        if(emailEnable) {
            processEngineConfiguration.setMailServerHost(emailHost);
            processEngineConfiguration.setMailServerPort(emailPort);
            processEngineConfiguration.setMailServerUsername(emailUsername);
            processEngineConfiguration.setMailServerPassword(emailPassword);
            processEngineConfiguration.setMailServerUseSSL(emailUseSSL);
            processEngineConfiguration.setMailServerUseTLS(emailUseTLS);
        }
        return processEngineConfiguration;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate rest = new RestTemplate();
        return rest;
    }

    @Bean
    public UserInfoInjector userInfoInjector() {
        return new UserInfoInjector();
    }

    @Bean
    public RepositoryService repositoryService() {
        return processEngine().getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService() {
        return processEngine().getRuntimeService();
    }

    @Bean
    public TaskService taskService() {
        return processEngine().getTaskService();
    }

    @Bean
    public HistoryService historyService() {
        return processEngine().getHistoryService();
    }

    @Bean
    public FormService formService() {
        return processEngine().getFormService();
    }

    @Bean
    public IdentityService identityService() {
        return processEngine().getIdentityService();
    }

    @Bean
    public ManagementService managementService() {
        return processEngine().getManagementService();
    }

}
