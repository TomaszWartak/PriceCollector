package com.dev4lazy.pricecollector.csv2pojo;

import android.content.Context;

import java.util.ArrayList;

// todo test:
// - uruchom testy
//  - co jeśli będzie pusty plik
//  - co jeśli będą śmieci w pliku
public class Csv2AnalysisRowConverter {

    private CsvReader csvReader = new CsvReader();
    private CsvDecoder csvDecoder = new CsvDecoder();

    private AnalysisRowRepository analysisRowRepository;

    private ArrayList<String> fieldNamesList; //todo z tego coś nie bardzo korzystasz...

    private ArrayList<AnalysisRow> analysisRowList;

    public Csv2AnalysisRowConverter(String csvFileName, Context context) {
        csvReader.openReader( csvFileName );
        analysisRowRepository = AnalysisRowRepository.getInstance(AnalyzesDatabase.getInstance(context));
        // todo test
        //Integer rowsCount = analysisRowRepository.getAnalysisRowsCount().getValue();
        analysisRowRepository.askAnalysisRowsCount(1);
        analysisRowRepository.clearDatabase();
        // todo test
        //rowsCount = analysisRowRepository.getAnalysisRowsCount().getValue();
        analysisRowRepository.askAnalysisRowsCount(2);
        //todo może jakiś warunek, że jak błędy to nie działamy dalej...
        //todo może jakiś warunek, że jak błędy to nie działamy dalej...
        // ? globalne zmienne do błędów
        analysisRowList = new ArrayList<>();
    }

    public void closeFiles() {
        csvReader.closeReader();
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
        String csvLine = csvReader.readCsvLine();
        AnalysisRow analysisRow;
        while ((csvLine=csvReader.readCsvLine())!=null) {
            values = csvDecoder.getValuesFromCsvLine(csvLine);
            analysisRow = makeAnalisisRow(values);
            analysisRowList.add(analysisRow);
        }
    }

    public AnalysisRow makeAnalisisRow(ArrayList<String> values ) {
        // todo musibyć sprawdzanie typu po konwersji, bo mogą przyjść śmieci w pliku i appka się wysypie...
        // todo co jeśli będzie mniej danych
        // todo może zamiast warości 0..11 użyj stałych...
        // todo zastanów się co, jeśli zmieni się format dancyh na serwerze analizy konkurencji
        AnalysisRow analysisRow = new AnalysisRow.AnalysisRowBuilder()
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
                .build();
        return analysisRow;
    }

    public void insertAllAnalysisRows() {
        for (AnalysisRow analysisRow : getAnalysisRowList()) {
            insertAnalysisRow(analysisRow);
        }
    }

    public ArrayList<AnalysisRow> getAnalysisRowList() {
        return analysisRowList;
    }

    private void insertAnalysisRow( AnalysisRow analysisRow) {
        analysisRowRepository.insertAnalysisRow(analysisRow);
    }

}
