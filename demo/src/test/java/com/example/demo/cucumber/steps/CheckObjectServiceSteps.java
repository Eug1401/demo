package com.example.demo.cucumber.steps;

import com.example.demo.DTO.EsitDTO;
import com.example.demo.DTO.NegativeEsitDTO;
import com.example.demo.DTO.PositiveEsitDTO;
import com.example.demo.DTO.PostIncomingMessageDTO;
import com.example.demo.Entity.IncomingMessage;
import com.example.demo.logic.CheckService;
import com.example.demo.mapper.IncomingMessageMapper;
import com.example.demo.repository.ObjectRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CheckObjectServiceSteps {
    @Mock
    private ObjectRepository objectRepository;

    @Mock
    private IncomingMessageMapper incomingMessageMapper;

    //Inietto servizio da testare (avrà già tutte le dipendenze interne configurate)
    @InjectMocks
    private CheckService checkService;


    //variabili per la condivisione dati tra step
    private PostIncomingMessageDTO inputMessage;
    private EsitDTO result;
    //in Given si prepara l'input
    //in When si esegue il metodo
    //nel Then si controlla il risultato

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    //-----------------------STEPS

    @Given("ho un messaggio {string}")
    public void hoUnMessaggio(String messaggio) {
        inputMessage = new PostIncomingMessageDTO(messaggio);
    }




    @When("invoco il metodo checkMessage")
    public void invocoIlMetodoCheckMessage() {
        IncomingMessage IM = new IncomingMessage(1, inputMessage.getStringaDaControllare());
        Mockito.when(incomingMessageMapper.toIncomingMessage(inputMessage)).thenReturn(IM);

        result = checkService.checkMessage(inputMessage);
    }


    @When("invoco il metodo addMessage per salvarlo su DB")
    public void invocoIlMetodoAddMessage() {
        Mockito.when(objectRepository.save(Mockito.any())).thenReturn(result);

        result = checkService.addMessage(inputMessage);

        Mockito.verify(objectRepository, Mockito.times(1)).save(Mockito.any());
    }





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
}
