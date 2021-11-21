package com.dev4lazy.pricecollector.remote_model.utils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dev4lazy.pricecollector.model.logic.RemoteDataRepository;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteEanCode;
import com.dev4lazy.pricecollector.view.utils.ProgressPresenter;

import java.util.ArrayList;
import java.util.List;

// todo test:
// - uruchom testy
//  - co jeśli będzie pusty plik
//  - co jeśli będą śmieci w pliku
public class Csv2EanCodeConverter {

    private final String eanCodesFileName = "casto-ean.csv";

    private CsvReader eanCodeCsvReader = new CsvReader();

    private CsvDecoder csvDecoder = new CsvDecoder();

    private RemoteDataRepository remoteDataRepository;

    private ArrayList<String> fieldNamesList; //todo z tego coś nie bardzo korzystasz...

    private ArrayList<RemoteEanCode> remoteEanCodeList;

    public Csv2EanCodeConverter() {
        eanCodeCsvReader.openReader( eanCodesFileName );
        remoteDataRepository = RemoteDataRepository.getInstance();

        // todo to niepotrzebne raczej...
        MutableLiveData<Integer> result = new MutableLiveData<>();
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onChanged(Integer ownCountryId) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                result.removeObserver(this); // this = observer...
            }
        };
        result.observeForever(observer);
        remoteDataRepository.askRemoteEanCodesNumberOf(result);

        //todo może jakiś warunek, że jak błędy to nie działamy dalej...
        // ? globalne zmienne do błędów
        remoteEanCodeList = new ArrayList<>();
    }

    public void closeFiles() {
        eanCodeCsvReader.closeReader();
    }
    
    public List<RemoteEanCode> makeEanCodeList() {
        ArrayList<String> values;
        // "pusty odczyt" - wiersz nagłówków
        String csvLine = eanCodeCsvReader.readCsvLine();
        RemoteEanCode remoteEanCode;
        while ((csvLine= eanCodeCsvReader.readCsvLine())!=null) {
            values = csvDecoder.getValuesFromCsvLine(csvLine);
            remoteEanCode = makeEanCode(values);
            remoteEanCodeList.add(remoteEanCode);
        }
        return remoteEanCodeList;
    }

    public RemoteEanCode makeEanCode(ArrayList<String> values ) {
        // todo musibyć sprawdzanie typu po konwersji, bo mogą przyjść śmieci w pliku i appka się wysypie...
        // todo co jeśli będzie mniej danych
        // todo może zamiast warości 0..11 użyj stałych...
        // todo zastanów się co, jeśli zmieni się format dancyh na serwerze analizy konkurencji
        RemoteEanCode remoteEanCode = new RemoteEanCode.EanCodeBuilder()
                .articleId( csvDecoder.getIntegerOrNullFromString( values.get(0)) )
                .value( values.get(1) )
                .build();
        return remoteEanCode;
    }

    public void insertAllEanCodes() {
        for (RemoteEanCode remoteEanCode : getRemoteEanCodeList()) {
            insertEanCode(remoteEanCode);
        }
    }

    public void insertAllEanCodes(
            MutableLiveData<Long> result,
            ProgressPresenter progressPresenter ) {
        remoteDataRepository.insertRemoteEanCodes( getRemoteEanCodeList(), result, progressPresenter );
    }

    public ArrayList<RemoteEanCode> getRemoteEanCodeList() {
        return remoteEanCodeList;
    }

    private void insertEanCode( RemoteEanCode remoteEanCode) {
        remoteDataRepository.insertRemoteEanCode(remoteEanCode);
    }

}
