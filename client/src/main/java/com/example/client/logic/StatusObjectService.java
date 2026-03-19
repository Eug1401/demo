package com.example.client.logic;

import com.example.client.entity.Notify;
import com.example.client.exception.SaveException;
import com.example.client.repository.NotifyRepository;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Getter
@Service
public class StatusObjectService {

    private final NotifyRepository notifyRepository;

    private final Logger logger = LoggerFactory.getLogger(StatusObjectService.class);

    StatusObjectService(NotifyRepository notifyRepository) {
        this.notifyRepository = notifyRepository;
    }

    public void saveNotify(Notify notify) {

        try {
            logger.info("Notifica ricevuta. Salvataggio in corso...");

            notifyRepository.save(notify);

            logger.info("Notifica salvata.");
        } catch (Exception e) {
            logger.error("Errore durante il salvataggio della notifica. ", e);
            throw new SaveException("Impossibile salvare la notifica", e);
        }
    }

}
