package com.fhga.yqfk.config;


import com.fhga.yqfk.entity.MyJobFactory;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Description: Quartz调度配置
 * <p/>
 * User: lishaohua
 * Date: 2017/11/14  10:27
 */
@Configuration //类似xml中的<beans>标签,一般和@bean注解一起使用来配置一个Bean,让Spring来管理它的生命周期
public class SchedulerConfig {
    /**
     * 配置文件路径
     */
    private String propertiesPath;
    //quartz.config.propertiesPath

    @Autowired
    private MyJobFactory myJobFactory;

    /**
     * 配置SchedulerFactoryBean
     *
     * @param dataSource 数据源
     * @return
     * @throws IOException
     */
    @Bean //将一个方法产生为Bean并交给Spring容器管理(@Bean只能用在方法上)
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) throws IOException {
        //Spring提供SchedulerFactoryBean为Scheduler提供配置信息,并被Spring容器管理其生命周期
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        //启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        // 延时启动(秒)
        factory.setStartupDelay(20);
        //设置quartz的配置文件
        factory.setQuartzProperties(quartzProperties());
        //设置自定义Job Factory，用于Spring管理Job bean
        factory.setJobFactory(myJobFactory);
        return factory;
    }

    /**
     * 加载Quartz配置
     *
     * @return
     * @throws IOException
     */
    @Bean
    public Properties quartzProperties() throws IOException {
        //使用Spring的PropertiesFactoryBean对属性配置文件进行管理
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        //注意：quartz的配置文件从指定系统目录中获取，而不是从classpath中获取
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        //propertiesFactoryBean.setLocation(new FileSystemResource(propertiesPath));


        //重要：保证其初始化
        propertiesFactoryBean.afterPropertiesSet();

        return propertiesFactoryBean.getObject();
    }

    /**
     * 初始化Quartz监听器,让Spring boot启动时初始化Quartz
     * --web工程中，一般在web.xml中设置如下：
     * <listener>
     * <listener-class>org.quartz.ee.servlet.QuartzInitializerListener</listener-class>
     * </listener>
     * Quartz就会随着web容器启动，加载调度任务
     *
     * @return
     */
    @Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();
    }

    //========get/set method============================
    public String getPropertiesPath() {
        return propertiesPath;
    }

    public void setPropertiesPath(String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }
}