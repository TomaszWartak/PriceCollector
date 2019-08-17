package com.dev4lazy.pricecollector.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "companies", // w bazie zdalnej nie ma takiej tabeli
    foreignKeys = {
        @ForeignKey(
            entity = Country.class,
            parentColumns = "id",
            childColumns = "country_id"
        )
    },
    indices = {
        @Index(
            value = "country_id"
        )
    }
)
public class Company {
    @PrimaryKey(autoGenerate = true)
    public int id;
    // todo?? czy jest taka tabela w bazie zdalnej
    public int remoteId; // klucz głowny w bazie zdalnej - w tym przypadku w bazie zdalnej nie ma takiej tabeli
    public String name;
    @ColumnInfo(name = "country_id")
    public int countryId;
}
