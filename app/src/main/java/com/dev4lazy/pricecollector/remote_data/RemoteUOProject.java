package com.dev4lazy.pricecollector.remote_data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "uo_projects" )  // w bazie zdalnej nie ma takiej tabeli
public class RemoteUOProject {

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
