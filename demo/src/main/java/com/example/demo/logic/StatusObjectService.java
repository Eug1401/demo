package com.example.demo.logic;

import com.example.demo.dto.*;
import com.example.demo.Entity.StatusObject;
import com.example.demo.Enums.Operazione;
import com.example.demo.exception.SaveException;
import com.example.demo.feign.DemoClient;
import com.example.demo.mapper.StatusObjectMapper;
import com.example.demo.repository.StatusObjectRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StatusObjectService {

    private final Logger logger = LoggerFactory.getLogger(StatusObjectService.class);

    private final StatusObjectRepository statusObjectRepository;

    private final StatusObjectMapper statusObjectMapper;

    private final DemoClient demoClient;

    private final  KafkaService kafkaService;


    @Autowired
    StatusObjectService(StatusObjectRepository statusObjectRepository, StatusObjectMapper statusObjectMapper, DemoClient demoClient, KafkaService kafkaService) {
        this.statusObjectRepository = statusObjectRepository;
        this.statusObjectMapper = statusObjectMapper;
        this.demoClient = demoClient;
        this.kafkaService = kafkaService;
    }

    //NOTA: eventuali modifiche e messaggi inviati al client o al listener non influenzano la scrittura su DB
    //eventuali fallimenti nelle due operazioni descritte, non porteranno quindi ad una rollback
    //tale implementazione ha infatti come unico scopo il test delle meccaniche feign client e kafka message


    public EsitDTO addStatusObject(PostStatusObjectDTO statusObjectDTO) {

        StatusObject SO = statusObjectMapper.toEntity(statusObjectDTO);


        if(!statusObjectRepository.existsById(SO.getCodiceIdentificativo()))
        {

            //salvataggio su DB
            try {
                statusObjectRepository.save(SO);
            } catch (Exception e) {
                logger.error("Errore nell'aggiunta dell'elemento", e);
                throw new SaveException("Errore nell'aggiunta dell'elemento", e);
            }

        }
        else {

            logger.info("Errore nell'inserimento, chiave primaria già presente, riprovare");
            return new NegativeEsitDTO("Chiave primaria già presente nel DB, riprovare");

        }


        NotifyDTO notify = new NotifyDTO(SO.getCodiceIdentificativo(), Operazione.CREATED, LocalDateTime.now());

        //invio notifica al feign client
        try {
            logger.info("Invio notifica http al feign client in corso... (EVENTO CREATED)");
            demoClient.sendNotify(notify);  //invio notifica tramite http request al feign client
            logger.info("Ripresa attività del servizio demo dopo invio della notifica al feign client. (EVENTO CREATED)");
        } catch (FeignException e) {
            logger.error("Errore nella chiamata al feign", e);
        }


        //invio messaggio al listener
        logger.info("Invio messaggio al listener in corso... (EVENTO CREATED)");
        kafkaService.sendMessage(notify);   //invio messaggio tramite kafka al listener

        logger.info("Ripresa attività del servizio demo dopo invio del messaggio kafka al listener. (EVENTO CREATED)");

        return new PositiveEsitDTO("Status object salvato");
    }


    public EsitDTO modifyStatusObject (PutStatusObjectDTO PSO) {

        Optional<StatusObject> statusObject = statusObjectRepository.findById(PSO.getCodiceIdentificativo());
        if(statusObject.isPresent()) {

            StatusObject SO = statusObject.get();

            statusObjectMapper.updateStatusObjectFromPutStatusObjectDTO(PSO, SO);


            //salvataggio modifica su DB
            try {
                statusObjectRepository.save(SO);
            } catch (Exception e) {
                logger.error("Errore nell'aggiornamento dell'elemento", e);
                throw new SaveException("Errore nell'aggiornamento dell'elemento", e);
            }

            NotifyDTO notify = new NotifyDTO(SO.getCodiceIdentificativo(), Operazione.UPDATE, LocalDateTime.now());

            //invio notifica al feign client
            try {
                logger.info("Invio notifica http al feign client in corso... (EVENTO UPDATE)");
                demoClient.sendNotify(notify);  //invio notifica tramite http request al feign client
                logger.info("Ripresa attività del servizio demo dopo invio della notifica al feign client. (EVENTO UPDATE)");
            }  catch (FeignException e) {
                logger.error("Errore nella chiamata al feign", e);
            }

            //invio messaggio al listener
            logger.info("Invio messaggio al listener in corso... (EVENTO UPDATE)");
            kafkaService.sendMessage(notify);  //invio messaggio tramite kafka al listener

            logger.info("Ripresa attività del servizio demo dopo invio del messaggio kafka al listener. (EVENTO UPDATE)");

            return new PositiveEsitDTO("Stato dell'oggetto: "+SO.getCodiceIdentificativo()+ " aggiornato");
        } else {
            return new NegativeEsitDTO("Risorsa non trovata");
        }
    }


    public List<GetStatusObjectDTO> getAllStatusObject() {
        return statusObjectRepository.findAll().stream()
                .map(statusObjectMapper::toGetStatusObject).toList();  //mappa elementi trovati nel findAll in elementi toGetStatusObject e costruisce una lista
    }
}
