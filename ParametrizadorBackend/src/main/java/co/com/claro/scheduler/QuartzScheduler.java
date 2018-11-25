/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.scheduler;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.EjecucionDAO;
import co.com.claro.ejb.dao.IEjecucionDAO;
import co.com.claro.ejb.dao.IWsTransformacionDAO;
import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.ejb.dao.ParametroDAO;
import co.com.claro.ejb.dao.WsTransformacionDAO;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.quartz.CronScheduleBuilder;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import org.quartz.DateBuilder;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

@Singleton
@Startup

public class QuartzScheduler {

    @EJB(beanName = "WsTransformacionDAO")
    protected IWsTransformacionDAO transformacionDAO;

    @EJB(beanName = "EjecucionDAO")
    protected IEjecucionDAO logEjecucionDAO;

    @EJB
    protected ConciliacionDAO conciliacionDAO;

    @EJB
    protected ParametroDAO parametroDAO;

    private int repeatCount = 3;
    //private CountDownLatch latch = new CountDownLatch(repeatCount + 1);

    @PostConstruct
    public void init() {
        System.out.println("Iniciando Quartz");
        fireJob();
    }

    public void QuartzScheduler() {
        //   fireJob();
    }

    public void fireJob() {
        try {
            //throws SchedulerException, InterruptedException {
            System.out.println("Se lanza firejob principal");
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
            Scheduler scheduler = schedFact.getScheduler();
            scheduler.start();

            JobBuilder jobBuilder = JobBuilder.newJob(ValidadorAgendamientoJob.class);
            JobDataMap data = new JobDataMap();
            data.put("transformacionDAO", transformacionDAO);
            data.put("logEjecucionDAO", logEjecucionDAO);
            data.put("conciliacionDAO", conciliacionDAO);
            data.put("parametroDAO", parametroDAO);

            JobDetail jobDetail = jobBuilder
                    .withIdentity("jobPrincipal", "group1")
                    .usingJobData(data)
                    .build();

            int _horaEjecucionJob = 0;

            try {
                String horaEjecucionJob = parametroDAO.findByParametro("SISTEMA", "horaEjecucionJob");
                _horaEjecucionJob = Integer.parseInt(horaEjecucionJob);
            } catch (Exception e) {
                _horaEjecucionJob = 0;
            }

            int _minutoEjecucionJob;
            try {
                String minutoEjecucionJob = parametroDAO.findByParametro("SISTEMA", "minutoEjecucionJob");
                _minutoEjecucionJob = Integer.parseInt(minutoEjecucionJob);
            } catch (Exception e) {
                _minutoEjecucionJob = 0;
            }

            System.out.println("A intentar corre a las " + _horaEjecucionJob + ":" + _minutoEjecucionJob);
            Date fechaActual = new Date();
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(fechaActual);   // assigns calendar to given date 
            int horaActual = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
            int minutoActual = calendar.get(Calendar.MINUTE);
            System.out.println("Hora actual: " + horaActual + "**" + "Minuto Actual:" + minutoActual);

            Trigger trigger;
            if (horaActual < _horaEjecucionJob) {
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trgPrincipal", "group1")
                        .startAt(DateBuilder.todayAt(_horaEjecucionJob, _minutoEjecucionJob, 00))
                        .withSchedule(
                                SimpleScheduleBuilder.simpleSchedule()
                                        .withIntervalInHours(24).repeatForever())
                        .build();
            } else if (horaActual == _horaEjecucionJob) {
                if (minutoActual < _minutoEjecucionJob) {
                    trigger = TriggerBuilder.newTrigger()
                            .withIdentity("trgPrincipal", "group1")
                            .startAt(DateBuilder.todayAt(_horaEjecucionJob, _minutoEjecucionJob, 00))
                            .withSchedule(
                                    SimpleScheduleBuilder.simpleSchedule()
                                            .withIntervalInHours(24).repeatForever())
                            .build();
                } else {
                  /*  Trigger _trigger = TriggerBuilder.newTrigger()
                            .withIdentity("trgPrincipal", "group1")
                            .startNow()
                            .build();
                    scheduler.scheduleJob(jobDetail, _trigger);*/

                    trigger = TriggerBuilder.newTrigger()
                            .withIdentity("trgPrincipal", "group1")
                            //.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(horaActual, minutoActual))
                          /*  .withSchedule(
                                    CronScheduleBuilder.dailyAtHourAndMinute(18, 59))*/
                            .startNow()
                            .build();
                }
            } else {
/*                Trigger _trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trgPrincipal", "group1")
                        .star
                        .startNow()
                        .build();
                scheduler.scheduleJob(jobDetail, _trigger);*/

                trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trgPrincipal", "group1")
                        //.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(horaActual, minutoActual))
                       /* .withSchedule(
                                CronScheduleBuilder.dailyAtHourAndMinute(18, 59)
                        )*/
                        .startNow()
                        .build();
            }

            // Tell quartz to schedule the job using our trigger
            scheduler.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void countDown() {
        //latch.countDown();
    }
}
