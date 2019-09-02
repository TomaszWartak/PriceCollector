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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(int remoteId) {
        this.remoteId = remoteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
