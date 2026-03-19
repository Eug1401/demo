package com.example.demo;

import com.example.demo.dto.PostIncomingMessageDTO;
import com.example.demo.Entity.IncomingMessage;
import com.example.demo.repository.ObjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//docker deve essere attivo -> il DB viene lanciato dentro un container Docker temporaneo
@SpringBootTest  //“Avvia tutto il contesto dell’applicazione, quasi come se stessi accendendo davvero il progetto.”
@AutoConfigureMockMvc      //fa creare a spring il bean MockMvc
@Testcontainers  //Abilitazione dei Testcontainers
@ActiveProfiles("test") //usiamo properties di test (vi viene definito un DB di test)
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;  //Serve per simulare chiamate HTTP senza avviare un server vero su porta 8080. Serve per richieste finte

	@Autowired
	private ObjectRepository objectRepository;

	@Autowired
	private ObjectMapper objectMapper;  //Serve per convertire un oggetto Java in JSON.


	@BeforeEach
	public void setup() {
		objectRepository.deleteAll();  //indipendenza tra test. Pulizia database
	}

	//TEST CASO POSITIVO
	@Test
	void saveMessageIntegrationTest() throws Exception {
		PostIncomingMessageDTO postIncomingMessageDTO = new PostIncomingMessageDTO("ciao");

		mockMvc.perform(MockMvcRequestBuilders.post("/message/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(postIncomingMessageDTO))  //conversione dto in json
						.accept(MediaType.APPLICATION_JSON))  //voglio ricevere un json
				.andExpect(status().isOk())  //lo stato deve essere 200
				.andExpect(MockMvcResultMatchers.jsonPath("$.message")  //se tutto ok, ricevo un PositiveEsitDTO -> ha un message
						.value("Messaggio salvato in memoria"));  //lo controllo


		//infine, verifica sul DB
		List<IncomingMessage> savedMessages = objectRepository.findAll();
		Assertions.assertEquals(1, savedMessages.size());
		Assertions.assertEquals("ciao", savedMessages.get(0).getStringaDaControllare());
	}
}
