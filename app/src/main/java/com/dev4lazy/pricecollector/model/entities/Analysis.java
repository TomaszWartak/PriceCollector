package com.dev4lazy.pricecollector.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dev4lazy.pricecollector.model.utils.DateConverter;

import java.util.Date;

@Entity(tableName = "analyzes" ) // w bazie zdalnej nie ma takiej tabeli
@TypeConverters({
        DateConverter.class
})
public class Analysis {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "creation_date")
    public Date creationDate;
    @ColumnInfo(name = "due_date")
    public Date dueDate;
    public Boolean finished;

}
