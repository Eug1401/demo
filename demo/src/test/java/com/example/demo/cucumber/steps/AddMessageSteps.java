package com.example.demo.cucumber.steps;

import com.example.demo.DTO.EsitDTO;
import com.example.demo.DTO.PostIncomingMessageDTO;
import com.example.demo.Entity.IncomingMessage;
import com.example.demo.Enums.Esito;
import com.example.demo.cucumber.runner.CucumberTestRunner;
import com.example.demo.repository.ObjectRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AddMessageSteps {

    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PostIncomingMessageDTO inputMessage;

    private ResultActions responseMessage; //RISPOSTA HTTP


    @Given("ho una stringa {string} da salvare")
    public void ho_una_stringa_da_salvare(String messaggio) throws Exception {
        inputMessage = new PostIncomingMessageDTO(messaggio);
    }

    @When("invio una POST a {string}")
    public void invio_una_POST_a(String endpoint) throws Exception {
        responseMessage = mockMvc.perform(MockMvcRequestBuilders.post("/message"+endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputMessage))  //conversione dto in json
                            .accept(MediaType.APPLICATION_JSON));  //voglio ricevere un json
    }

    @Then("la risposta HTTP ha codice {int}")
    public void la_risposta_HTTP_ha_codice(int codice) throws Exception {
        responseMessage.andExpect(status().is(codice));
    }

    @Then("ottengo un esito di tipo {esito} con messaggio {string}")
    public void ottengo_un_esito_di_tipo_con_messaggio(Esito esito, String message)throws Exception {
        responseMessage
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(message))
                .andExpect(MockMvcResultMatchers.jsonPath("$.esito")
                        .value(esito.getValore()));
    }

    @Then("nel database esiste un messaggio con stringa {string}")
    public void nel_database_esiste_un_messaggio_con_stringa(String messaggio) throws Exception {
        List<IncomingMessage> L = objectRepository.findAll();

        Assertions.assertEquals(1, L.size());
        Assertions.assertEquals(messaggio, L.get(0).getStringaDaControllare());
    }
}
