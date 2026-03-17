package com.example.demo.cucumber.runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

//Classe di avvio di cucumber

@Suite  //Una suite è una classe che serve ad avviare un insieme di test
@IncludeEngines("cucumber")  //utilizza motore cucumber
@SelectClasspathResource("features")  //i file .features vengono cercati nella cartella features di resources
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.example.demo.cucumber")  //la glue è il package dove cucumber deve cercare le step definitions (Given / When / Then)
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")  //pretty serve a migliorare il modo in cui un test viene visualizzato a schermo
public class CucumberTestRunner {

    //La classe è vuota perché tutta la configurazione è nelle annotazioni.
}
