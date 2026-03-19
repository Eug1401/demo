package com.example.demo.logic;

import com.example.demo.dto.NotifyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    //il kafka template serve a strutturare il messaggio da inviare
    //  <Tipo della key, oggetto da inviare>
    private final KafkaTemplate<String, NotifyDTO> kafkaTemplate;

    private final Logger logger =   LoggerFactory.getLogger(KafkaService.class);

    public KafkaService(KafkaTemplate<String, NotifyDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    //metodo per l'invio del messaggio (conterrà la stateTable)
    public void sendMessage(NotifyDTO notifyDTO) {
        kafkaTemplate.send("event", "sendEvent", notifyDTO)
                .whenComplete((result, ex) -> {  //result contiene i dettagli del messaggio inviato con successo
                    if (ex == null) {              //se ex è nulla, tutto ok
                        logger.info("Messaggio Kafka inviato correttamente");
                    } else {                       //se viene lanciata eccezione, viene caricata in ex
                        logger.error("Errore durante l'invio del messaggio Kafka", ex);
                    }
                });
    }
}