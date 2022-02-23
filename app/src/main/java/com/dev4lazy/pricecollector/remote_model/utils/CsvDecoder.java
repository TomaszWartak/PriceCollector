package com.dev4lazy.pricecollector.remote_model.utils;

import java.util.ArrayList;

import static com.dev4lazy.pricecollector.remote_model.utils.CsvUtils.CSV_SEPARATOR;


public class CsvDecoder {

    public ArrayList<String> getValuesFromCsvLine(String csvLine ) {
        ArrayList<String> values = new ArrayList<>();
        if (!csvLine.isEmpty()) {
            /*
             Jeżeli w linii csv po ostatnim separatorze jest pusty napis (np. ";"),
             to nie zostanie on dodany do values.
             Wynika to z tego, że jeśli cutCsvValueFromCsvLine() usunie ostatni separator
             (jako efekt uboczny) z csvLineStringBuilder, to csvLineStringBuilder
             przyjmie wartość "" i pętla odczytu wartości z csvLineStringBuilder
             zostanie zakończona.
             Dlatego, zamiast tego ostaniego pustego napisu dodaję spację (np. "; ").
             Spacja w cutCsvValueFromCsvLine() zosytanie zamieniona na pusty napis,
             który dzięki temu zostanie dodany do values.
            */
            if (lastCharIsSeparator(csvLine)) {
                csvLine=csvLine+" ";
            }
            StringBuilder csvLineStringBuilder = new StringBuilder(csvLine);
            String value;
            while (csvLineStringBuilder.length()>0) {
                value = cutCsvValueFromCsvLine(csvLineStringBuilder);
                if (value!=null)
                    values.add(value);
            }
        }
        return values;
    }

    private boolean lastCharIsSeparator(String csvLine) {
        if (csvLine.isEmpty()) {
            return false;
        } else {
            return (csvLine.charAt(csvLine.length()-1)== CSV_SEPARATOR.charAt(0));
        }
    }

    /**
        Działanie: wycina wartość z początku linii csv
        Argumenty: csvLine - linia csv
        Rezultat: wartość wyciętą z początku linii csv
        Efekty uboczne: csvLine jest pomniejszoną o wyciętą wartość
     */
    public String cutCsvValueFromCsvLine( StringBuilder csvLine ) {
        if (!csvLine.toString().isEmpty()) {
            StringBuilder result = new StringBuilder();
            int delimiterPosition = csvLine.indexOf(CSV_SEPARATOR);
            if (delimiterPosition > -1) {
                result.append(csvLine.substring(0, delimiterPosition).trim());
                result.toString().replace(CSV_SEPARATOR, "");
                csvLine.delete(0, delimiterPosition + 1);
            } else {
                // W csvLine została ostatnia wartość
                result.append(csvLine.toString().trim());
                csvLine.delete(0,csvLine.length());
            }
            return result.toString();
        } else {
            return "";
        }
    }

    public Integer getIntegerOrNullFromString(String value) {
        if (value.isEmpty()) {
            return null;
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                return null;
            }
        }
    }

    public Double getDoubleOrNullFromString(String value) {
        if (value.isEmpty()) {
            return null;
        } else {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                return null;
            }
        }
    }

}
