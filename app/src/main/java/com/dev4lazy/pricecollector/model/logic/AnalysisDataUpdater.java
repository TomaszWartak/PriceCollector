package com.dev4lazy.pricecollector.model.logic;

/*

AnalysisDataUpdater.
Służy do aktualizacji danych związanych z analizą konkurencji.
- po zalogowaniu sprawdza, czy na serwerze danych są jakieś dane do pobrania
- jeśli tak, sygnalizuje UI, że istnieje konieczność aktualizacji
- Jeśli użytkownik podejmie decyzję o aktualizacji, aktualizuje dane przez repozytorium
 */
public class AnalysisDataUpdater {

    // todo - jak sprawdzić na serwerze danych, czy są dane do aktualizacji, bez pobierania tych danych?
    // Chyba tylko jeśli na serwerze będzie dana informująca o tym
    // W innym przypadku trzeba chyba pobrać dane...

    // True - jeśli na serwerze danych są dane do pobrania.
    private boolean newAnalysisDataReadyToDownlad = false;

    /*
    Sprawdza, czy na serwerze danych są dane do pobrania.
    Efekt uboczny: Ustawia wartość newAnalisisDataReadyToDownlad na true, jesli są;
     */
    public void checkNewAnalysisDataToDownload() {
        //todo !!
        // z preferncji pobranie daty ostatniej aktualizacji
        // sprawdzenie jaka jest data najnowszych danych na serwerze danych
        // Jeśli są nowsze dane - ustawienie wartości newAnalisisDataReadyToDownlad
        // Jeśli jest nowe badanie,to trzeba zawiesić info na ekranie głównym
        // Podobnie jeśli zostanie pobrana informacjae badanie jest zakończone
    }

    public boolean areNewAnalysisDataReadyToDownlad() {
        return newAnalysisDataReadyToDownlad;
    }

    /*
    Pobiera dane nowsze niż data ostatniej aktualizacji
    todo trzeba przepytać każdą tabelę i pobrać dane nowsze niż ostatnia aktualizacja
    todo Jeśli użytkownik modyfikował lokalne dane po aktualizacji, to trzeba rozwiązać konflikty
    todo Kolekcja wszystkich DAO/RDAO; Każde DAO/RDAO relizuje interfejs z metodą oddającą datę kiedy dana była aktualizowana
    todo Może lpiej polegać na zapyatniu SQL, które zwróci dane zmodyfikowane po ostatniej aktualizacji...
    todo Metoda musi być zabezpieczona przed sytaucją, że pobieramy aktualne dane, zapisujemy je do lokalnej bazy
    todo i zrywa połączenie. Dobrze byłoby jakoś kontrolować, co zostało zaktualizowane i odtworzyć mniej więcej od miejsca przerwania
    todo !!! Zbyt odbiegasz od realiów. W istniejącej bazie nie ma mechnizmu pamięatnia daty aktualizacji każdej danej
     */
    public void DownloadNewAnalysisData() {

    }

    /*
    Pobiera wszsykie dane (refresh - reload), bez względu na to czy były już pobrane, czy nie
     */
    public void DownloadAllAnalysisData() {

    }

    /*
    Przepisuje dane lokalne na serwer zdalny
    todo E... To chyba on-line powinno być robione?
    todo Może nie on-line, bo trzeba się zastanowić, jak ma być nowa cena
    todo ! powinien podpowiadać nową cenę i sygnalizować odchylenie od ceny konurenta - lista wadliwych cen...
     */
    public void UploadAnalysisData() {

    }
}
