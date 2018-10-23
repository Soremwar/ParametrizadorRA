/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.scheduler;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

/**
 *
 * @author andresbedoya
 */
public class MyJob implements org.quartz.Job {

      public MyJob() {
      }

      public void execute(JobExecutionContext context) throws JobExecutionException {
          System.err.println("Hello World!  MyJob is executing.");
      }

  } 
