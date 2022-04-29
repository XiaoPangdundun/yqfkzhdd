package com.fhga.yqfk.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fhga.yqfk.entity.Case;
import com.fhga.yqfk.entity.Staff;
import com.fhga.yqfk.entity.Task;
import com.fhga.yqfk.entity.Work;
import com.fhga.yqfk.mapper.CaseMapper;
import com.fhga.yqfk.mapper.StaffMapper;
import com.fhga.yqfk.mapper.TaskMapper;
import com.fhga.yqfk.mapper.WorkMapper;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StartTaskService implements Job {

    @Autowired
    private CaseMapper caseMapper;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("执行开始任务");
        JobDataMap jdMap = jobExecutionContext.getJobDetail().getJobDataMap();
        System.out.println(jdMap.getInt("taskId"));
        String jobname=jobExecutionContext.getJobDetail().getKey().getName();
        //DateFormat format1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        List<Work> workList=workMapper.selectList(new QueryWrapper<Work>().eq("task_id", jdMap.getInt("taskId")));

        for(Work work:workList){
            Staff staff = new Staff();
            staff.setStaffNo(work.getStaffNo());
            staff.setIsFight(true);
            staffMapper.update(staff,new QueryWrapper<Staff>().eq("Staff_No",work.getStaffNo()));
        }
    }

}
