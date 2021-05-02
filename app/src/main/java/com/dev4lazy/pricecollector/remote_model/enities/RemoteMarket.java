package com.dev4lazy.pricecollector.remote_model.enities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "markets" ) // w bazie zdalnej nie ma takiej tabeli
public class RemoteMarket {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
