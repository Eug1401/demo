Feature: Salvataggio di un messaggio

  Scenario Outline: Salvataggio corretto di un messaggio
    Given ho una stringa "<messaggio>" da salvare
    When invio una POST a "/add"
    Then la risposta HTTP ha codice 200
    And ottengo un esito di tipo POSITIVO con messaggio "Messaggio salvato in memoria"
    And nel database esiste un messaggio con stringa "<messaggio>"

    Examples:
    | messaggio |
    | ciao      |
    | prova     |
    | test      |