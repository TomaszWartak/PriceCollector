package com.dev4lazy.pricecollector.remote_model.enities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "families" ) // [dbo].[DCT_Family]
/*
    [FAM_Id] [int] NOT NULL,
	[FAM_Name] [nvarchar](100) NOT NULL,
 */
public class RemoteFamily {

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
