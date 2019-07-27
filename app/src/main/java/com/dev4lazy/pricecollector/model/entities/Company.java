package com.dev4lazy.pricecollector.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "companies",
    foreignKeys = @ForeignKey(
        entity = Country.class,
        parentColumns = "id",
        childColumns = "country_id"
    )
)
public class Company {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "country_id")
    public int countryId;

    public String name;

}
