package com.example.demo.session;

import com.example.demo.dto.EsitDTO;
import com.example.demo.dto.NegativeEsitDTO;
import com.example.demo.exception.DeleteException;
import com.example.demo.exception.SaveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SaveException.class)
    public ResponseEntity<EsitDTO> handleSaveException(SaveException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new NegativeEsitDTO("Errore nel salvataggio su DB: "+ e));
    }

    @ExceptionHandler(DeleteException.class)
    public ResponseEntity<EsitDTO> handleDeleteException(DeleteException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new NegativeEsitDTO("Errore nell'eliminazione dell'oggetto dal DB:"+ e));
    }

}
