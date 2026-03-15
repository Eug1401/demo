package com.example.demo.logic;

import com.example.demo.DTO.NegativeEsitDTO;
import com.example.demo.DTO.PositiveEsitDTO;
import com.example.demo.DTO.PostIncomingMessageDTO;
import com.example.demo.Entity.IncomingMessage;
import com.example.demo.DTO.EsitDTO;
import com.example.demo.Enums.MessaggioDaControllare;
import com.example.demo.mapper.IncomingMessageMapper;
import com.example.demo.repository.ObjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CheckService {

    private final ObjectRepository objectRepository;

    private final IncomingMessageMapper incomingMessageMapper;

    private final Logger logger = LoggerFactory.getLogger(CheckService.class);

    @Autowired
    public CheckService(ObjectRepository objectRepository, IncomingMessageMapper incomingMessageMapper) {
        this.objectRepository = objectRepository;
        this.incomingMessageMapper = incomingMessageMapper;
    }

    public EsitDTO checkMessage(PostIncomingMessageDTO IR)
    {
        IncomingMessage IM = incomingMessageMapper.toIncomingMessage(IR);

        if(IM.getStringaDaControllare().equals(MessaggioDaControllare.CIAO.getValore())) return new PositiveEsitDTO("Il messaggio inviato risulta corretto");

        else return new NegativeEsitDTO("Il messaggio inviato non coincide con quello nel sistema");

    }


    public EsitDTO addMessage(PostIncomingMessageDTO postIncomingMessageDTO) {

        IncomingMessage IM = incomingMessageMapper.toIncomingMessage(postIncomingMessageDTO);
        try {
            objectRepository.save(IM);
        } catch (Exception e) {
            logger.error("Errore nell'aggiunta dell'elemento", e);
            throw new RuntimeException(e);
        }
        return new PositiveEsitDTO("Messaggio salvato in memoria");
    }


    public EsitDTO deleteById (Long id) {

        Optional<IncomingMessage> MDC = objectRepository.findById(id);

        if(MDC.isPresent()) {

            try {
                objectRepository.deleteById(id);
            } catch (Exception e) {
                logger.error("Errore nell'eliminazione dell'elemento", e);
                throw new RuntimeException(e);
            }
            return new PositiveEsitDTO("Elemento "+id+ " rimosso dalla memoria");
        }

        else {
            return new NegativeEsitDTO("Risorsa non trovata");
        }
    }

}
