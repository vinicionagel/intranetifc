/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import com.bean.MigracaoBean;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author vinicio
 */

public class JobScheduler implements ServletContextListener{
    /**
     * Método referente a migração automatizada.
     */
    private void executeAction() {
        try {
            JobDetail job = JobBuilder.newJob(MigracaoBean.class)
                    .withIdentity("testJob")
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger().startAt(DateBuilder.dateOf(23, 59, 59)).withSchedule(
                    SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInHours(12)
                    .repeatForever())
                    .build();

            SchedulerFactory schFactory = new StdSchedulerFactory();
            Scheduler sch = schFactory.getScheduler();
            sch.start();
            sch.scheduleJob(job, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        executeAction();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
