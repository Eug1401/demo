package com.example.demo.schedulers;

import com.example.demo.dto.GetStatusObjectDTO;
import com.example.demo.logic.StatusObjectService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

//funge da controller per quartz
//interagisce con gli altri bean spring per realizzare operazioni più o meno complesse
public class PrintJob implements Job {

    StatusObjectService statusObjectService;

    PrintJob (StatusObjectService statusObjectService) {
        this.statusObjectService = statusObjectService;
    }

    Logger log = LoggerFactory.getLogger(PrintJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Il PrintJob sta effettuando un controllo di integrità sul DB...");
        boolean check = true;

        List<GetStatusObjectDTO> L = statusObjectService.getAllStatusObject();

        for(GetStatusObjectDTO x : L) {

            if(x.getNome() == null) {
                check = false;
                log.error("Controllo di integrità fallito, almeno un oggetto ha nome null");
                break;
            }

        }

        if(check) log.info("Il controllo di integrità è andato a buon fine");

        log.info("Il PrintJob ha terminato le operazioni di controllo.");
    }
}
