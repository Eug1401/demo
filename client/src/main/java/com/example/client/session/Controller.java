package com.example.client.session;

import com.example.client.entity.Notify;
import com.example.client.logic.StatusObjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notify-controller")
public class Controller {

    private final StatusObjectService statusObjectService;


    public Controller(StatusObjectService statusObjectService) {
        this.statusObjectService = statusObjectService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addNotify(@RequestBody Notify notify) {
        statusObjectService.saveNotify(notify);
        return ResponseEntity.ok().build();
    }

}
