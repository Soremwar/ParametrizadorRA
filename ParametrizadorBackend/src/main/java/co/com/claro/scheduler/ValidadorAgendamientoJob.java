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
import co.com.claro.model.entity.WsTransformacion;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

/**
 *
 * @author ronal
 */
@Stateless
public class ValidadorAgendamientoJob implements Job {

    @EJB(beanName = "WsTransformacionDAO")
    protected IWsTransformacionDAO transformacionDAO;

    @EJB(beanName = "EjecucionDAO")
    protected IEjecucionDAO logEjecucionDAO;

    @EJB
    protected ConciliacionDAO conciliacionDAO;

    @EJB
    protected ParametroDAO parametroDAO;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
       System.out.println("HERE in execution");
            JobDetail _jobDetail = jec.getJobDetail();
            IWsTransformacionDAO transformacionDAO = (IWsTransformacionDAO) _jobDetail.getJobDataMap().get("transformacionDAO");
            IEjecucionDAO logEjecucionDAO = (IEjecucionDAO) _jobDetail.getJobDataMap().get("logEjecucionDAO");
            ConciliacionDAO conciliacionDAO = (ConciliacionDAO) _jobDetail.getJobDataMap().get("conciliacionDAO");
            ParametroDAO parametroDAO = (ParametroDAO) _jobDetail.getJobDataMap().get("parametroDAO");
            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.HOUR_OF_DAY, 23);
            c2.set(Calendar.MINUTE, 59);
            c2.set(Calendar.SECOND, 59);

            List<WsTransformacion> aAgendarHoy = transformacionDAO.findByFechaAgendamiento(c1.getTime(), c2.getTime());
            String tempo1 = "Cantidad::" + aAgendarHoy.size();
            System.out.println(tempo1);

            System.out.println("Se lanza job SECUNDARIO");
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
            Scheduler scheduler = schedFact.getScheduler();
            scheduler.start();

            aAgendarHoy.forEach(a -> {
                try {
                    System.out.println(a.getFechaAgendamiento() + " ** " + a.getNombreWs());
                    JobBuilder jobBuilder = JobBuilder.newJob(EjecucionProgramacion.class);
                    JobDataMap data = new JobDataMap();
                    data.put("wsTransformacion", a);
                    data.put("logEjecucionDAO", logEjecucionDAO);
                    data.put("conciliacion", a.getConciliacion());
                    data.put("conciliacionDAO", conciliacionDAO);
                    data.put("parametroDAO", parametroDAO);

                    // Por cada conciliación que encuentre, programar la ejecución
                    JobDetail jobDetail = jobBuilder
                            .withIdentity("job" + a.getNombreWs(), "group2")
                            .usingJobData(data)
                            .build();

                    Trigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity("trg" + a.getNombreWs(), "group2")
                            .startAt(a.getFechaAgendamiento())
                            .build();

                    // Tell quartz to schedule the job using our trigger
                    scheduler.scheduleJob(jobDetail, trigger);
                } catch (SchedulerException ex) {
                    Logger.getLogger(ValidadorAgendamientoJob.class.getName()).log(Level.SEVERE, null, ex);
                }

            });
        } catch (SchedulerException ex) {
            Logger.getLogger(ValidadorAgendamientoJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
