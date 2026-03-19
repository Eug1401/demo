package com.example.demo.logic;

import com.example.demo.dto.EsitDTO;
import com.example.demo.dto.PositiveEsitDTO;
import com.example.demo.dto.PostIncomingMessageDTO;
import com.example.demo.Entity.IncomingMessage;
import com.example.demo.mapper.IncomingMessageMapper;
import com.example.demo.repository.ObjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CheckServiceTest {

    // *
    //oggetto fittizio -> non viene iniettato, ma creato da Mockito appositamente per il test
    @Mock
    ObjectRepository objectRepository;

    @Mock
    IncomingMessageMapper incomingMessageMapper;

    //inietta gli oggetti definiti sopra. Se non fatto, l'iniezione non viene realizzata
    //checkService sarà a sua volta un oggetto Mock
    @InjectMocks
    CheckService checkService;

    //NOTA: bisogna iniettare tutti gli oggetti indispensabili per l'esecuzione del codice testato
    //esempio: se va testato un test che fa .save() quello utilizzerà la repository -> la repository va iniettata tramite @InjectMocks

    //NOTA: Mockito restituisce null su qualsiasi metodo che restituisce un oggetto
    //bisogna indicare tramite when(...).thenReturn(...) cosa fare in tali casistiche

    //TEST CASO POSITIVO
    @Test
    void checkMessageTest() {
        PostIncomingMessageDTO message = new PostIncomingMessageDTO("ciao");  //INPUT DEL METODO DA TESTARE
        PositiveEsitDTO esito= new PositiveEsitDTO("Messaggio salvato in memoria");   //OUTPUT ATTESO RISPETTO ALL'INPUT

        //In questo modo Mockito non tornerà null, ma saprà cosa fare
        IncomingMessage incomingMessage = new IncomingMessage(1,"ciao");
        Mockito.when(incomingMessageMapper.toIncomingMessage(message))    //quando verrà eseguita questa linea di codice
                .thenReturn(incomingMessage);  //fai questo

        Mockito.when(objectRepository.save(incomingMessage)).thenReturn(incomingMessage);

        EsitDTO obteined = checkService.addMessage(message);   //OUTPUT DEL METODO TA TESTARE RISPETTO ALL'INPUT PASSATO

        //OUTPUT DEL METODO ED OUTPUT ATTESO DEVONO COINCIDERE
        //Assertions.assertEquals(atteso, reale);
        //Assertions controlla se i due valori sono uguali
        Assertions.assertEquals(esito.getEsito(), obteined.getEsito());  //le asserzioni servono a verificare che il risultato sia quello atteso
    }

    //Junit -> framework per test in Java
    //trova i metodi di test, li esegue e controlla se i valori sono corretti
    //è il motore che esegue i test

    //mockito crea oggetti fittizi per simulare dipendenze



    //ALTRI ANNOTAZIONI UTILI PER I TEST: E' POSSIBILE DEFINIRE METODI MARCATI CON
    //@BeforeAll viene eseguito una sola volta prima di tutti i test della classe.
    //
    //@BeforeEach viene eseguito prima di ogni singolo test.



    //Potenzialmente è possibile anche testare l'eccezione lanciata dal metodo testato
    //In tal caso si deve progettare correttamente il test per rientrare in una casistica da eccezione e poi occorre usare assertThrows
}

