package in.gov.bpm.app.config

import com.mysql.jdbc.Driver
import org.activiti.engine.FormService
import org.activiti.engine.HistoryService
import org.activiti.engine.IdentityService
import org.activiti.engine.ManagementService
import org.activiti.engine.ProcessEngine
import org.activiti.engine.ProcessEngineConfiguration
import org.activiti.engine.RepositoryService
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.activiti.engine.impl.history.HistoryLevel
import org.activiti.spring.ProcessEngineFactoryBean
import org.activiti.spring.SpringProcessEngineConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.jdbc.datasource.SimpleDriverDataSource

import javax.annotation.PostConstruct;
import javax.sql.DataSource

/**
 * Created by user-1 on 24/6/16.
 */
@Configuration
class ActivitiEngineConfiguration {

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(Driver);

        // Connection settings
        ds.setUrl("jdbc:mysql://localhost:3306/activiti");
        ds.setUsername("root");
        ds.setPassword("password")

        return ds;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean(name="processEngineFactoryBean")
    public ProcessEngineFactoryBean processEngineFactoryBean() {
        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
        return factoryBean;
    }

    @Bean(name="processEngine")
    public ProcessEngine processEngine() {
        // Safe to call the getObject() on the @Bean annotated processEngineFactoryBean(), will be
        // the fully initialized object instanced from the factory and will NOT be created more than once
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
        return processEngineConfiguration;
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
