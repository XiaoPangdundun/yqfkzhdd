package com.fhga.yqfk.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class EndTaskService implements Job {


    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("执行结束任务");
        JobDataMap jdMap = jobExecutionContext.getJobDetail().getJobDataMap();
        System.out.println(jdMap.getInt("caseId"));
        String jobname=jobExecutionContext.getJobDetail().getKey().getName();

        //DateFormat format1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        List<Work> workList=workMapper.selectList(new QueryWrapper<Work>().eq("Case_Id", jdMap.getInt("caseId")));
        for(Work work:workList){
            Staff staff = new Staff();
            staff.setStaffNo(work.getStaffNo());
            staff.setIsFight(false);
            staffMapper.update(staff,new QueryWrapper<Staff>().eq("Staff_No",work.getStaffNo()));
        }
    }

}