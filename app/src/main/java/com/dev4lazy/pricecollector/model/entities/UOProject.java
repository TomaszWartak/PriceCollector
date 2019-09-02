package com.dev4lazy.pricecollector.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "uo_projects" )  // w bazie zdalnej nie ma takiej tabeli
public class UOProject {
    @PrimaryKey(autoGenerate = true)
    public int id;
    // todo ?? czy w bazie zdlanej jest taka tabela
    public int remote_id; // klucz głowny w bazie zdalnej - w tym przypadku w bazie zdalnej nie ma takiej tabeli
    public String name;

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
}
