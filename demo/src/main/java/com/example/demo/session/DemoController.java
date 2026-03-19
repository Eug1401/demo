package com.example.demo.session;

import com.example.demo.dto.PostIncomingMessageDTO;
import com.example.demo.dto.EsitDTO;
import com.example.demo.logic.CheckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class DemoController {

    private final CheckService checkService;

    public DemoController(CheckService checkService) {
        this.checkService = checkService;
    }

    @GetMapping("/ciao")
    public String returnCiao() { return "ciao"; };


    @PostMapping("/check")
    public ResponseEntity<EsitDTO> checkRequest(@RequestBody PostIncomingMessageDTO requestObject) {
        return ResponseEntity.ok(checkService.checkMessage(requestObject));
    }

    @PostMapping("/add")
    public ResponseEntity<EsitDTO> saveMessage(@RequestBody PostIncomingMessageDTO requestObject) {
        return ResponseEntity.ok(checkService.addMessage(requestObject));
    }

    //API in minuscolo separate da trattino in caso -

    //non è necessario mettere nel path il metodo (DELETE) in caso di CRUD controller
    @DeleteMapping("/{id}")
    public ResponseEntity<EsitDTO> deleteElement(@PathVariable Long id) {
        EsitDTO esit= checkService.deleteById(id);

        return switch (esit.getEsito()) {
            case POSITIVO -> ResponseEntity.ok(esit);
            case NEGATIVO -> ResponseEntity                //deleteByID restituisce un caso NEGATIVO se non trova l'oggetto da eliminare
                    .status(HttpStatus.NOT_FOUND)
                    .body(esit);
        };
    }
}
