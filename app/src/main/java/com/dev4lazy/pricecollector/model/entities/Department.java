package com.dev4lazy.pricecollector.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "departments" ) // [dbo].[DCT_Department]
/*
    [DEP_Id] [int] NOT NULL,
	[DEP_Name] [nvarchar](50) NOT NULL,

 */
public class Department {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int remote_id; // klucz głowny w bazie zdalnej
    public String symbol;
    public String name;

}
