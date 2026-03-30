package com.example.demo.schedulers;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//funge da controller per quartz
//interagisce con gli altri bean spring per realizzare operazioni più o meno complesse
public class PrintJob implements Job {

    Logger log = LoggerFactory.getLogger(PrintJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String nome = context.getMergedJobDataMap().getString("nome");
        log.info("La classe "+PrintJob.class.getName()+ " sta stampando: "+ nome);
    }
}
