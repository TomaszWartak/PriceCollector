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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(int remoteId) {
        this.remoteId = remoteId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
