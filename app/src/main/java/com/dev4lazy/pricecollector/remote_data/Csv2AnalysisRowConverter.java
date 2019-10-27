package com.dev4lazy.pricecollector.remote_data;

import com.dev4lazy.pricecollector.model.RemoteDataRepository;

import java.util.ArrayList;

// todo test:
// - uruchom testy
//  - co jeśli będzie pusty plik
//  - co jeśli będą śmieci w pliku
public class Csv2AnalysisRowConverter {

    private final String analysisRowsFileName = "dane-test1000v2.csv";

    private CsvReader analysisRowCsvReader = new CsvReader();

    private CsvDecoder csvDecoder = new CsvDecoder();

    private RemoteDataRepository remoteDataRepository;

    private ArrayList<String> fieldNamesList; //todo z tego coś nie bardzo korzystasz...

    private ArrayList<RemoteAnalysisRow> remoteAnalysisRowList;

    public Csv2AnalysisRowConverter() {
        analysisRowCsvReader.openReader( analysisRowsFileName );
        remoteDataRepository = RemoteDataRepository.getInstance();
        remoteDataRepository.askAnalysisRowsCount(1);
        //todo może jakiś warunek, że jak błędy to nie działamy dalej...
        // ? globalne zmienne do błędów
        remoteAnalysisRowList = new ArrayList<>();
    }

    public void closeFiles() {
        analysisRowCsvReader.closeReader();
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

    public void makeAnalisisRowList() {
        ArrayList<String> values;
        // "pusty odczyt" - wiersz nagłówków
        String csvLine = analysisRowCsvReader.readCsvLine();
        RemoteAnalysisRow remoteAnalysisRow;
        while ((csvLine= analysisRowCsvReader.readCsvLine())!=null) {
            values = csvDecoder.getValuesFromCsvLine(csvLine);
            remoteAnalysisRow = makeAnalisisRow(values);
            remoteAnalysisRowList.add(remoteAnalysisRow);
        }
    }

    public RemoteAnalysisRow makeAnalisisRow(ArrayList<String> values ) {
        // todo musibyć sprawdzanie typu po konwersji, bo mogą przyjść śmieci w pliku i appka się wysypie...
        // todo co jeśli będzie mniej danych
        // todo może zamiast warości 0..11 użyj stałych...
        // todo zastanów się co, jeśli zmieni się format dancyh na serwerze analizy konkurencji
        RemoteAnalysisRow remoteAnalysisRow = new RemoteAnalysisRow.AnalysisRowBuilder()
                .store( csvDecoder.getIntegerOrNullFromString( values.get(0)) )
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

    public ArrayList<RemoteAnalysisRow> getRemoteAnalysisRowList() {
        return remoteAnalysisRowList;
    }

    private void insertAnalysisRow( RemoteAnalysisRow remoteAnalysisRow) {
        remoteDataRepository.insertAnalysisRow(remoteAnalysisRow);
    }

}
