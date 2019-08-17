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
}
