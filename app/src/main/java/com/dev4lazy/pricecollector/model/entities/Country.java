package com.dev4lazy.pricecollector.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "countries" )
public class Country {
    @PrimaryKey
    public int id;
    public String name;
}
