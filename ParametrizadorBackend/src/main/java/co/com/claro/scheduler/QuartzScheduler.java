/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.scheduler;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.IEjecucionDAO;
import co.com.claro.ejb.dao.IWsTransformacionDAO;
import co.com.claro.ejb.dao.ParametroDAO;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.quartz.CronScheduleBuilder;
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
                String horaEjecucionJob = parametroDAO.findByParametro("SISTEMA", "V_horaEjecucionJob");
                _horaEjecucionJob = Integer.parseInt(horaEjecucionJob);
            } catch (Exception e) {
                _horaEjecucionJob = 0;
            }

            int _minutoEjecucionJob;
            try {
                String minutoEjecucionJob = parametroDAO.findByParametro("SISTEMA", "V_minutoEjecucionJob");
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

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trgPrincipal", "group1")
                    //.startAt(DateBuilder.todayAt(13, 42, 00))
                   .startNow()
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(_horaEjecucionJob, _minutoEjecucionJob))
                    //.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(20, 40))
                    .build();

            // Tell quartz to schedule the job using our trigger
            scheduler.scheduleJob(jobDetail, trigger);
            System.out.println("scheduler programado");
            /*if (horaActual < _horaEjecucionJob) {

            } else if (horaActual == _horaEjecucionJob) {
                if (minutoActual < _minutoEjecucionJob) {

                } else {

                    SchedulerFactory schedFact2 = new org.quartz.impl.StdSchedulerFactory();
                    Scheduler scheduler2 = schedFact2.getScheduler();
                    scheduler2.start();

                    JobBuilder jobBuilder2 = JobBuilder.newJob(ValidadorAgendamientoJob.class);
                    JobDataMap data2 = new JobDataMap();
                    data2.put("transformacionDAO", transformacionDAO);
                    data2.put("logEjecucionDAO", logEjecucionDAO);
                    data2.put("conciliacionDAO", conciliacionDAO);
                    data2.put("parametroDAO", parametroDAO);

                    JobDetail jobDetail2 = jobBuilder2
                            .withIdentity("jobPrincipal_", "group1")
                            .usingJobData(data2)
                            .build();

                    Trigger _trigger = TriggerBuilder.newTrigger()
                            .withIdentity("trgPrincipal_", "group1")
                            .startNow()
                            .build();

                    scheduler2.scheduleJob(jobDetail2, _trigger);
                }
            } else {
                SchedulerFactory schedFact2 = new org.quartz.impl.StdSchedulerFactory();
                Scheduler scheduler2 = schedFact2.getScheduler();
                scheduler2.start();

                JobBuilder jobBuilder2 = JobBuilder.newJob(ValidadorAgendamientoJob.class);
                JobDataMap data2 = new JobDataMap();
                data2.put("transformacionDAO", transformacionDAO);
                data2.put("logEjecucionDAO", logEjecucionDAO);
                data2.put("conciliacionDAO", conciliacionDAO);
                data2.put("parametroDAO", parametroDAO);

                JobDetail jobDetail2 = jobBuilder2
                        .withIdentity("jobPrincipal_", "group1")
                        .usingJobData(data2)
                        .build();

                Trigger _trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trgPrincipal_", "group1")
                        //.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(horaActual, minutoActual))
              
                        .startNow()
                        .build();

                scheduler2.scheduleJob(jobDetail2, _trigger);
            }*/

        } catch (SchedulerException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void countDown() {
        //latch.countDown();
    }
}
