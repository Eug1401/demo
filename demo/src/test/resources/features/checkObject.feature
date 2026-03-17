Feature: checkObjectService Test
  Questa feature testa il metodo checkMessage

  Scenario Outline: controllo di un messaggio
    Given ho un messaggio "<messaggio>"
    When invoco il metodo checkMessage
    Then ottengo un esito di tipo "<tipoEsito>" con messaggio "<messaggioAtteso>"

    Examples:
      | messaggio | tipoEsito        | messaggioAtteso                                          |
      | ciao      | PositiveEsitDTO  | Il messaggio inviato risulta corretto                    |
      | hello     | NegativeEsitDTO  | Il messaggio inviato non coincide con quello nel sistema |


    Scenario Outline: salvataggio corretto di un messaggio
      Given ho un messaggio "<messaggio>"
      When invoco il metodo addMessage per salvarlo su DB
      Then ottengo un esito di tipo "PositiveEsitDTO" con messaggio "Messaggio salvato in memoria"
      And il messaggio "<messaggio>" risulta salvato nel database

      Examples:
      | messaggio |
      | ciao      |
      | palla     |
      | pollo     |