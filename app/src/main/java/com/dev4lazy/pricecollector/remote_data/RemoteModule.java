package com.dev4lazy.pricecollector.remote_data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "modules" ) // może [dbo].[DCT_ModuleToSector]
/*
    [MOD_Id] [int] NOT NULL,
	[SEC_Id] [int] NOT NULL,

 */
public class RemoteModule {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
