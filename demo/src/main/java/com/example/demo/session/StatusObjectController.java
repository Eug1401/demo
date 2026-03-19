package com.example.demo.session;

import com.example.demo.dto.EsitDTO;
import com.example.demo.dto.GetStatusObjectDTO;
import com.example.demo.dto.PostStatusObjectDTO;
import com.example.demo.dto.PutStatusObjectDTO;
import com.example.demo.logic.StatusObjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/status-object")
public class StatusObjectController {

    private final StatusObjectService statusObjectService;

    StatusObjectController(StatusObjectService statusObjectService) {
        this.statusObjectService = statusObjectService;
    }

    @PostMapping("/add")
    public ResponseEntity<EsitDTO> addStatusObject(@RequestBody PostStatusObjectDTO statusObject){
        return ResponseEntity.ok(statusObjectService.addStatusObject(statusObject));
    }

    @PutMapping("/put")
    public ResponseEntity<EsitDTO> putStatusObject(@RequestBody PutStatusObjectDTO statusObjectDTO) {
        EsitDTO esit = statusObjectService.modifyStatusObject(statusObjectDTO);

        return switch (esit.getEsito()) {
            case POSITIVO -> ResponseEntity.ok(esit);
            case NEGATIVO ->
                    ResponseEntity                //.modifyStatusObject() restituisce un caso NEGATIVO se non trova l'oggetto da aggiornare
                            .status(HttpStatus.NOT_FOUND)
                            .body(esit);
        };
    }

    @GetMapping("/getAllStatusObject")
    public ResponseEntity<List<GetStatusObjectDTO>> getAll() {
        return ResponseEntity.ok(statusObjectService.getAllStatusObject());
    }
}
