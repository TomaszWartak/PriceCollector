package com.dev4lazy.pricecollector.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "stores", // [dbo].[DCT_Shop], ale tam jest prosta struktura:
        /*
        [SHO_Id] [int] NOT NULL,
	    [SHO_Name] [nvarchar](100) NOT NULL,
	    [REG_Id] [int] NULL,
	    [SHO_IsSector] [bit]
        */
    foreignKeys = @ForeignKey(
        entity = Company.class,
        parentColumns = "id",
        childColumns = "company_id"
    ),
    indices = {
        @Index(value = "company_id")
    }
)
public class Store {
    @PrimaryKey(autoGenerate = true)
    public int id;
    // todo ?? czy w bazie zdalnej jest taka tabela - zob entity OwnStore
    public int remoteId; // klucz głowny w bazie zdalnej - w tym przypadku w bazie zdalnej nie ma takiej tabeli
    @ColumnInfo(name = "company_id")
    public int companyId;
    public String city;
    public String street;
    public String zipCode;
}
