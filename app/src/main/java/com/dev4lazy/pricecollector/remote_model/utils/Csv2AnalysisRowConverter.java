package com.dev4lazy.pricecollector.remote_model.utils;

import com.dev4lazy.pricecollector.model.logic.RemoteDataRepository;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.view.utils.ProgressPresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class Csv2AnalysisRowConverter {

    private final String ANALYSIS_ROWS_FILE_NAME = "dane-test1000v2.csv";

    private CsvReader analysisRowCsvReader;
    private CsvDecoder csvDecoder;
    private RemoteDataRepository remoteDataRepository;
    private ArrayList<String> fieldNamesList; //todo ok: z tego coś nie bardzo korzystasz...
    private ArrayList<RemoteAnalysisRow> remoteAnalysisRowList;

    public Csv2AnalysisRowConverter() {
        csvDecoder = new CsvDecoder();
        openCsvReader();
        remoteAnalysisRowList = new ArrayList<>();
        remoteDataRepository = RemoteDataRepository.getInstance();
    }

    public void reset() {
        clearRemoteAnalysisRowList();
        if (analysisRowCsvReader!=null) {
            closeReader();
        }
        openCsvReader();
    }

    public void clearRemoteAnalysisRowList() {
        if (remoteAnalysisRowList==null) {
            remoteAnalysisRowList = new ArrayList<>();
        } else {
            remoteAnalysisRowList.clear();
        }
    }

    public void closeReader() {
        analysisRowCsvReader.closeReader();
        analysisRowCsvReader=null;
    }

    private void openCsvReader() {
        if (analysisRowCsvReader==null) {
            analysisRowCsvReader = new CsvReader();
        }
        analysisRowCsvReader.openReaderFromAssets(ANALYSIS_ROWS_FILE_NAME);
    }

    private void createFieldList() {
        fieldNamesList = new ArrayList<>();
        fieldNamesList.add("store");
        fieldNamesList.add("article_code");
        fieldNamesList.add("article_name");
        fieldNamesList.add("article_store_price");
        fieldNamesList.add("article_ref_price");
        fieldNamesList.add("article_new_price");
        fieldNamesList.add("article_new_margin_percent");
        fieldNamesList.add("article_lm_price");
        fieldNamesList.add("article_obi_price");
        fieldNamesList.add("article_bricoman_price");
        fieldNamesList.add("article_local_competitor1_price");
        fieldNamesList.add("article_local_competitor2_price");
    }

    public List<RemoteAnalysisRow> makeAnalysisRowList(int analysisNr, int analysisId) {
        ArrayList<String> values;
        // "pusty odczyt" - wiersz nagłówków
        String csvLine = analysisRowCsvReader.readCsvLine();
        RemoteAnalysisRow remoteAnalysisRow;
        int rowCounter = 0;
        while ((csvLine= analysisRowCsvReader.readCsvLine())!=null) {
            values = csvDecoder.getValuesFromCsvLine(csvLine);
            remoteAnalysisRow = makeAnalysisRow(values);
            remoteAnalysisRow.setAnalysisId(analysisId);
            remoteAnalysisRowList.add(remoteAnalysisRow);
            rowCounter++;
            // TODO ok: Na potrzeby testu, dla pierwszej analizy pobierane jest tylko 1000 wierszy
            if ((rowCounter==1000) && (analysisNr==0)) {
                break;
            }
        }
        return remoteAnalysisRowList;
    }

    public RemoteAnalysisRow makeAnalysisRow(ArrayList<String> values ) {
        RemoteAnalysisRow remoteAnalysisRow = new RemoteAnalysisRow.AnalysisRowBuilder()
                .store( csvDecoder.getIntegerOrNullFromString( values.get(0)) )
                .analysisId( 0 ) // 0 - bo zostanie wpisany później
                .articleCode( csvDecoder.getIntegerOrNullFromString( values.get(1)) )
                .articleName( values.get(2) )
                .articleStorePrice( csvDecoder.getDoubleOrNullFromString(values.get(3)) )
                .articleRefPrice( csvDecoder.getDoubleOrNullFromString(values.get(4)) )
                .articleNewPrice( csvDecoder.getDoubleOrNullFromString(values.get(5)) )
                .articleNewMarginPercent( csvDecoder.getDoubleOrNullFromString(values.get(6)) )
                .articleLmPrice( csvDecoder.getDoubleOrNullFromString(values.get(7)) )
                .artlcleObiPrice( csvDecoder.getDoubleOrNullFromString(values.get(8)) )
                .artlcleBricomanPrice( csvDecoder.getDoubleOrNullFromString(values.get(9)) )
                .articleLocalCompetitor1Price( csvDecoder.getDoubleOrNullFromString(values.get(10)) )
                .articleLocalCompetitor2Price( csvDecoder.getDoubleOrNullFromString(values.get(11)) )
                .department( values.get(12) )
                .sector( values.get(13) )
                .build();
        return remoteAnalysisRow;
    }

    public void insertAllAnalysisRows() {
        for (RemoteAnalysisRow remoteAnalysisRow : getRemoteAnalysisRowList()) {
            insertAnalysisRow(remoteAnalysisRow);
        }
    }

    public void insertAllAnalysisRows(
            MutableLiveData<Long> result,
            ProgressPresenter progressPresenter ) {
        remoteDataRepository.insertRemoteAnalysisRows( getRemoteAnalysisRowList(), result, progressPresenter );
    }

    public ArrayList<RemoteAnalysisRow> getRemoteAnalysisRowList() {
        return remoteAnalysisRowList;
    }

    private void insertAnalysisRow( RemoteAnalysisRow remoteAnalysisRow) {
        remoteDataRepository.insertRemoteAnalysisRow(remoteAnalysisRow);
    }

}
