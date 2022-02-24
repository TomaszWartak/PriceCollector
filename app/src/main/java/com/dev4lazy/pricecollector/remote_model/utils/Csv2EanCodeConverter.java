package com.dev4lazy.pricecollector.remote_model.utils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dev4lazy.pricecollector.model.logic.RemoteDataRepository;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteEanCode;
import com.dev4lazy.pricecollector.view.utils.ProgressPresenter;

import java.util.ArrayList;
import java.util.List;

public class Csv2EanCodeConverter {

    private final String EAN_CODES_FILE_NAME = "casto-ean.csv";

    private CsvReader eanCodeCsvReader = new CsvReader();
    private CsvDecoder csvDecoder;
    private RemoteDataRepository remoteDataRepository;
    private ArrayList<RemoteEanCode> remoteEanCodeList;

    public Csv2EanCodeConverter() {
        csvDecoder = new CsvDecoder();
        openCsvReader();
        remoteEanCodeList = new ArrayList<>();
        remoteDataRepository = RemoteDataRepository.getInstance();
    }

    public void reset() {
        clearRemoteEanCodeList();
        if (eanCodeCsvReader!=null) {
            closeReader();
        }
        openCsvReader();
    }


    public void clearRemoteEanCodeList() {
        if (remoteEanCodeList==null) {
            remoteEanCodeList = new ArrayList<>();
        } else {
            remoteEanCodeList.clear();
        }
    }

    public void closeReader() {
        eanCodeCsvReader.closeReader();
        eanCodeCsvReader=null;
    }

    private void openCsvReader() {
        if (eanCodeCsvReader==null) {
            eanCodeCsvReader = new CsvReader();
        }
        eanCodeCsvReader.openReaderFromAssets(EAN_CODES_FILE_NAME);
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
