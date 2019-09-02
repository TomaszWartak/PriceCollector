package com.dev4lazy.pricecollector.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "countries" ) // w bazie zdalnej nie ma takiej tabeli
public class Country {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int remote_id; // klucz głowny w bazie zdalnej - w tym przypadku w bazie zdalnej nie ma takiej tabeli
    private String name;
    private String englishName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRemote_id() {
        return remote_id;
    }

    public void setRemote_id(int remote_id) {
        this.remote_id = remote_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String name) {
        this.englishName = name;
    }
}
