package com.example.demo.logic;

import com.example.demo.DTO.*;
import com.example.demo.Entity.StatusObject;
import com.example.demo.Enums.Operazione;
import com.example.demo.feign.DemoClient;
import com.example.demo.mapper.StatusObjectMapper;
import com.example.demo.repository.StatusObjectRepository;
import feign.FeignException;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @CacheEvict(value = "StatusObjectCache", key = "'all'")
    public EsitDTO addStatusObject(PostStatusObjectDTO statusObjectDTO) {

        StatusObject SO = statusObjectMapper.toEntity(statusObjectDTO);


        //salvataggio su DB
        try {
            statusObjectRepository.save(SO);
        } catch (Exception e) {
            logger.error("Errore nell'aggiunta dell'elemento", e);
            throw new RuntimeException(e);
        }


        //invio notifica al feign client
        try {
            logger.info("Invio notifica http al feign client in corso... (EVENTO CREATED)");
            demoClient.sendNotify(new NotifyDTO(SO.getCodiceIdentificativo(), Operazione.CREATED, LocalDateTime.now()));  //invio notifica tramite http request al feign client
            logger.info("Ripresa attività del servizio demo dopo invio della notifica al feign client. (EVENTO CREATED)");
        } catch (FeignException e) {
            logger.error("Errore nella chiamata al feign", e);
        }


        //invio messaggio al listener
        logger.info("Invio messaggio al listener in corso... (EVENTO CREATED)");
        kafkaService.sendMessage(new NotifyDTO(SO.getCodiceIdentificativo(), Operazione.CREATED, LocalDateTime.now()));   //invio messaggio tramite kafka al listener

        logger.info("Ripresa attività del servizio demo dopo invio del messaggio kafka al listener. (EVENTO CREATED)");

        return new PositiveEsitDTO("Status object salvato");
    }


    @CacheEvict(value = "StatusObjectCache", key = "'all'")  //pulisce la cache se viene aggiornata la lista, in modo che verrà ricaricata dal db alla prossima get
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
                throw new RuntimeException(e);
            }

            //invio notifica al feign client
            try {
                logger.info("Invio notifica http al feign client in corso... (EVENTO UPDATE)");
                demoClient.sendNotify(new NotifyDTO(SO.getCodiceIdentificativo(), Operazione.UPDATE, LocalDateTime.now()));  //invio notifica tramite http request al feign client
                logger.info("Ripresa attività del servizio demo dopo invio della notifica al feign client. (EVENTO UPDATE)");
            }  catch (FeignException e) {
                logger.error("Errore nella chiamata al feign", e);
            }

            //invio messaggio al listener
            logger.info("Invio messaggio al listener in corso... (EVENTO UPDATE)");
            kafkaService.sendMessage(new NotifyDTO(SO.getCodiceIdentificativo(), Operazione.UPDATE, LocalDateTime.now()));  //invio messaggio tramite kafka al listener

            logger.info("Ripresa attività del servizio demo dopo invio del messaggio kafka al listener. (EVENTO UPDATE)");

            return new PositiveEsitDTO("Stato dell'oggetto: "+SO.getCodiceIdentificativo()+ " aggiornato");
        } else {
            return new NegativeEsitDTO("Risorsa non trovata");
        }
    }

    //viene utilizzata entry chiamata 'all' per salvare in Redis
    //funziona come una tabella hash
    //l'operazione di pulizia in caso di aggiornamento deve essere realizzata sulla stessa entry
    @Cacheable(value = "StatusObjectCache", key = "'all'") //in caso di get successive, salva in cache per velocizzare il recupero
    public List<GetStatusObjectDTO> getAllStatusObject() {
        return statusObjectRepository.findAll().stream()
                .map(statusObjectMapper::toGetStatusObject).toList();  //mappa elementi trovati nel findAll in elementi toGetStatusObject e costruisce una lista
    }


}
