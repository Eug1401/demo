package com.example.demo.feign;

import com.example.demo.dto.NotifyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


//nell'url va specificato il nome dell'altro servizio nel container (demo -> nel container è client-app)
@FeignClient(name="demo-client", url="http://client-app:8081")
//feign client -> strategia per poter effettuare chiamate HTTP verso altre applicazioni in modo semplice
//anzichè gestire manualmente gli header, il corpo, il body, la response e la request, si realizza un metodo Java che verrà convertito in chiamata direttamente da Spring
public interface DemoClient {

    //l'interfaccia sarà implementata da Spring che realizzerà tutto dietro le quinte

    @PostMapping("/notify-controller/add")
    void sendNotify(@RequestBody NotifyDTO notifyDTO);  //imposto body

    //la conversione da Json (in arrivo dopo la chiamata) a DTO sarà realizzata da Spring
}

//In Spring, i feign client vengono utilizzati per implementare un tipo di comunicazione sincrona