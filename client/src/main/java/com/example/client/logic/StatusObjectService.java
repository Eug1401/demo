package com.example.client.logic;

import com.example.client.entity.Notify;
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

            // Simula un'elaborazione lenta per osservare il comportamento sincrono/asincro
            Thread.sleep(5000);

            notifyRepository.save(notify);

            logger.info("Notifica salvata.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrotto durante l'attesa simulata", e);
            throw new RuntimeException("Operazione interrotta", e);
        }catch (Exception e) {
            logger.error("Errore durante il salvataggio della notifica", e);
            throw new RuntimeException("Impossibile salvare la notifica", e);
        }
    }
}
