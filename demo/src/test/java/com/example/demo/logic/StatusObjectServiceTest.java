package com.example.demo.logic;

import com.example.demo.dto.EsitDTO;
import com.example.demo.dto.NotifyDTO;
import com.example.demo.dto.PositiveEsitDTO;
import com.example.demo.dto.PostStatusObjectDTO;
import com.example.demo.Entity.StatusObject;
import com.example.demo.Enums.Stato;
import com.example.demo.feign.DemoClient;
import com.example.demo.mapper.StatusObjectMapper;
import com.example.demo.repository.StatusObjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class StatusObjectServiceTest {

    @Mock
    StatusObjectRepository statusObjectRepository;

    @Mock
    StatusObjectMapper statusObjectMapper;

    @Mock
    DemoClient demoClient;

    @Mock
    KafkaService kafkaService;

    @InjectMocks
    StatusObjectService statusObjectService;

    //TEST CASO POSITIVO
    @Test
    void addStatusObjectTest() {
        //PRIMA PARTE: COSTRUZIONE INPUT E RISPOSTA ATTESA
        PostStatusObjectDTO postStatusObjectDTO = new PostStatusObjectDTO("codice1","test1");
        PositiveEsitDTO expectedResponse = new PositiveEsitDTO("Status object salvato");


        //SECONDA PARTE: GESTIONE DEI METODI CHE RESTITUISCONO UN OGGETTO NON NULL
        StatusObject SO = new StatusObject("codice1", "test1", Stato.DA_AGGIORNARE);
        Mockito.when(statusObjectMapper.toEntity(postStatusObjectDTO)).thenReturn(SO);

        Mockito.when(statusObjectRepository.save(SO)).thenReturn(SO);


        //TERZA PARTE: CHIAMATA DEL METODO DA TESTARE
        EsitDTO response = statusObjectService.addStatusObject(postStatusObjectDTO);



        //QUARTA PARTE: VERIFICA LA CHIAMATA AI METODI CHE RESTITUISCONO NULL
        Mockito.verify(demoClient, times(1)).sendNotify(Mockito.any(NotifyDTO.class));  //verifica che il metodo sia stato chiamato esattamente 1 volta

        Mockito.verify(kafkaService, times(1)).sendMessage(Mockito.any(NotifyDTO.class));  //Mockito.any -> serve quando l'oggetto da passare al metodo viene costruito già nel metodo da testare (o comunque quando non è importante l'oggetto da utilizzare)



        //QUINTA PARTE: VERIFICA DELLA CORRETTEZZA DELLA RISPOSTA OTTENUTA
        Assertions.assertEquals(expectedResponse.getEsito(), response.getEsito());
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
    }
}
