package com.example.demo.schedulers;

import com.example.demo.Entity.StatusObject;
import com.example.demo.mapper.StatusObjectMapper;
import com.example.demo.repository.StatusObjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusObjectScheduler {
    private final StatusObjectRepository statusObjectRepository;

    private final StatusObjectMapper statusObjectMapper;

    Logger logger = LoggerFactory.getLogger(StatusObjectScheduler.class);

    @Autowired
    StatusObjectScheduler(StatusObjectRepository statusObjectRepository, StatusObjectMapper statusObjectMapper) {
        this.statusObjectRepository = statusObjectRepository;
        this.statusObjectMapper = statusObjectMapper;
    }

    @Scheduled(
            fixedDelayString = "${scheduler.delay}"
    )
    //fixedRate = 10000 -> ogni 10 secondi
    //se il metodo richiede 10 secondi per essere eseguito ed il fixedRate è a 8s, si ha overlay
    //per impedire tale scenario si usa fixedDelay, che invece impedisce overlay ed esegue nuovamente il metodo solo quando termina il precedente
    //mediante initialDelay è possibile specificare dopo quanto avviare lo scheduling dall'avvio dell'applicazione
    public void consonantStatusObjectScheduler() {
        System.out.println("Ricerca degli StatusObject che iniziano per consonante in corso...");
        List<StatusObject> objects = statusObjectRepository.findByNomeStartingWithConsonant();

        if (objects.isEmpty()) { logger.info("Al momento non sono presenti oggetti che iniziano con consonante nel DB"); }


        else {
            objects
                    .forEach(x -> {
                        logger.info("NOME: {}", statusObjectMapper.toResponseStatusObject(x).getNome());  //logger
                    });
        }
    }

    //cron expressions permettono di essere più precisi. In particolare permette di specificare esatti istanti temporali.
    //cron = "* * * * *"
    //schema : second (0-59) - minute (0-59) - hour (0-23) - day of month (1-31) - month (1-12) - day of week (0-7) (sunday=0 or 7)

    //schemi speciali: * * * * * significa ogni secondo
    //ne esistono altri, vedere documentazione (CronMaker su google)
}
