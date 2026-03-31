package com.example.demo.config;

import com.example.demo.schedulers.PrintJob;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

//classe di configurazione di Quartz
//inizializza i bean dei Job ed implementa i Trigger associati
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail printJobDetail() { //ritorna un oggetto JobDeatail (è l’oggetto che descrive un job, cioè che cosa deve essere eseguito)
        return newJob(PrintJob.class)   //la classe che deve essere eseguita à PrintJob
                .withIdentity("printJob", "demo")
                .storeDurably()  //il job deve essere mantenuto in memoria
                .build();
    }

    @Bean
    public Trigger printTrigger(JobDetail printJobDetail) {
        return newTrigger()
                .forJob(printJobDetail)  //associa il job al trigger
                .withIdentity("printTrigger", "demo")
                .startNow()  //inizia da ora
                .withSchedule(
                        simpleSchedule()  //schedulazione semplice ad intervalli regolari
                                .withIntervalInSeconds(30)  //ogni 15 secondi
                                .repeatForever()
                ).build();
    }
}
