package com.fhga.yqfk;


import com.fhga.yqfk.service.StartTaskService;

import org.junit.jupiter.api.Test;

import org.quartz.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



import java.util.concurrent.TimeUnit;




@SpringBootTest
public class YqfkApplicationTests {

    @Autowired
    private Scheduler scheduler;

    @Test
    public void optiontest() throws SchedulerException, InterruptedException {

        // 2、创建JobDetail实例，并与PrintWordsJob类绑定(Job执行内容)
        JobDetail jobDetail = JobBuilder.newJob(StartTaskService.class)
                .withIdentity("job1", "group1").build();
        jobDetail.getJobDataMap().put("caseId", 1);

        // 3、构建Trigger实例,每隔1s执行一次
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "triggerGroup1")
                .startNow()//立即生效
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1)//每隔1s执行一次
                        .repeatForever()).build();//一直执行

        //4、执行
        scheduler.scheduleJob(jobDetail, trigger);
        System.out.println("--------scheduler start ! ------------");
        scheduler.start();

        //睡眠
        TimeUnit.MINUTES.sleep(1);
        //scheduler.shutdown();
        System.out.println("--------scheduler shutdown ! ------------");
        return;
    }
}
