package com.dev4lazy.pricecollector.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "families" ) // [dbo].[DCT_Family]
/*
    [FAM_Id] [int] NOT NULL,
	[FAM_Name] [nvarchar](100) NOT NULL,
 */
public class Family {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int remote_id; // klucz głowny w bazie zdalnej
    public String name;
}
