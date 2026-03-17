package com.example.demo.cucumber.steps;

import com.example.demo.DTO.EsitDTO;
import com.example.demo.DTO.NegativeEsitDTO;
import com.example.demo.DTO.PositiveEsitDTO;
import com.example.demo.DTO.PostIncomingMessageDTO;
import com.example.demo.Entity.IncomingMessage;
import com.example.demo.logic.CheckService;
import com.example.demo.repository.ObjectRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CheckObjectServiceSteps {

    @Autowired
    private ObjectRepository objectRepository;

    //Inietto servizio da testare (avrà già tutte le dipendenze interne configurate)
    @Autowired
    private CheckService checkService;


    //variabili per la condivisione dati tra step
    private PostIncomingMessageDTO inputMessage;
    private EsitDTO result;
    //in Given si prepara l'input
    //in When si esegue il metodo
    //nel Then si controlla il risultato

    @Given("ho un messaggio {string}")
    public void hoUnMessaggio(String messaggio) {
        inputMessage = new PostIncomingMessageDTO(messaggio);
    }




    @When("invoco il metodo checkMessage")
    public void invocoIlMetodoCheckMessage() {
        result = checkService.checkMessage(inputMessage);
    }

    @When("invoco il metodo addMessage per salvarlo su DB")
    public void invocoIlMetodoAddMessage() { result = checkService.addMessage(inputMessage); }





    @Then("ottengo un esito di tipo {string} con messaggio {string}")
    public void ottengoUnEsitoDiTipoConMessaggio(String tipoEsitoAtteso, String messaggioAtteso) {
        Assertions.assertEquals(messaggioAtteso, result.getMessage());  //controllo se il messaggio ottenuto è uguale a quello atteso


        if (tipoEsitoAtteso.equals("PositiveEsitDTO")) {  //se l'esito atteso è positivo
            Assertions.assertInstanceOf(PositiveEsitDTO.class, result);  //controllo se l'esitDTO ottenuto è istanta di PositiveEsitDTO
        } else if (tipoEsitoAtteso.equals("NegativeEsitDTO")) {
            Assertions.assertInstanceOf(NegativeEsitDTO.class, result);
        } else {
            Assertions.fail("Tipo esito non riconosciuto: " + tipoEsitoAtteso);
        }
    }

    @Then("il messaggio {string} risulta salvato nel database")
    public void verificaSalvataggio(String messaggioAtteso) {
        List<IncomingMessage> savedMessages = objectRepository.findAll();

        Assertions.assertEquals(1, savedMessages.size());   //LA INSERT DEVE AGGIUNGERE UNA SOLA RIGA
        Assertions.assertEquals(messaggioAtteso, savedMessages.get(0).getStringaDaControllare());  //CONTROLLO STRINGA SALVATA
    }
}
