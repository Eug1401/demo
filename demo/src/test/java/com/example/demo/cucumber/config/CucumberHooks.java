package com.example.demo.cucumber.config;

import com.example.demo.repository.ObjectRepository;
import com.example.demo.repository.StatusObjectRepository;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class CucumberHooks { //mette a disposizione una serie di metodi automatici eseguiti prima/dopo i test

    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private StatusObjectRepository statusObjectRepository;

    @Before //In cucumber, un metodo annotato in questo modo è un Hooks
    public void cleanDatabase() {
        objectRepository.deleteAll();
        statusObjectRepository.deleteAll();
    }  //parte prima di ogni esecuzione
}
