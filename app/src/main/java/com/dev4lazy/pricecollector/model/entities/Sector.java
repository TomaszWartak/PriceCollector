package com.dev4lazy.pricecollector.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sectors" ) // [dbo].[DCT_Sector]
/*
    [SEC_Id] [int] NOT NULL,
	[SEC_Name] [nvarchar](50) NOT NULL,

 */
public class Sector {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int remote_id; // klucz głowny w bazie zdalnej
    public String name;

}
