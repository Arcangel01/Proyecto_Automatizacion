Feature: Consultar vuelos
  Como usuario quiero consultar un vuelo con el menor precio

  Scenario: Buscar vuelos baratos
    Given Quiero seleccionar un vuelo barato
    When Comparar los valores
    Then Deberia tener de resultado si los valores son iguales