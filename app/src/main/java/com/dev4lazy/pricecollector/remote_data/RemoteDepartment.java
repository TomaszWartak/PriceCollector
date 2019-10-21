package com.dev4lazy.pricecollector.remote_data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "departments" ) // [dbo].[DCT_Department]
/*
    [DEP_Id] [int] NOT NULL,
	[DEP_Name] [nvarchar](50) NOT NULL,

 */
public class RemoteDepartment {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int remote_id; // klucz głowny w bazie zdalnej
    private String symbol;
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
