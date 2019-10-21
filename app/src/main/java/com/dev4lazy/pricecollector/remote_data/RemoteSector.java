package com.dev4lazy.pricecollector.remote_data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sectors" ) // [dbo].[DCT_Sector]
/*
    [SEC_Id] [int] NOT NULL,
	[SEC_Name] [nvarchar](50) NOT NULL,

 */
public class RemoteSector {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int remote_id; // klucz głowny w bazie zdalnej
    private String name;

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
