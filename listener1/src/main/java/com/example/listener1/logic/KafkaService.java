package com.example.listener1.logic;

import com.example.listener1.dto.NotifyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);

    //mi iscrivo al topic 'MyTopic' e rimango in attesa di messaggio
    //mi aspetto di ricevere una StateTable
    //definisco il gruppo del listener1 : "group1"

    @KafkaListener(topics = "event", groupId = "group1")
    public void listen(NotifyDTO notifyDTO) {

            log.info("Evento ricevuto, elaborazione in corso...");

            log.info("Operazione completata, dati ricevuti: {}", notifyDTO);
    }
    //solitamente questi metodi ritornano void e non altri oggetti
    //il loro obiettivo è la manipolazione dei dati ricevuti
    //le informazioni così manipolate possono essere poi inoltrate ad altri microservizi (catena di eventi)
}
