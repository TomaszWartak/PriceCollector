package com.dev4lazy.pricecollector.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "articles" ) // [dbo].[DCT_Article]
/*
    [SKU_Id] [int] NOT NULL,
	[SKU_Name] [nvarchar](100) NULL,
	[SKU_SaleUnit] [nvarchar](50) NULL,
	[FAM_Id] [int] NOT NULL,
	[DEP_Id] [int] NOT NULL,
	[SEC_Id] [int] NOT NULL,
	[SKU_PMMPrice] [money] NULL,
	[SKU_PMMPriceDate] [date] NOT NULL,
	[SKU_Status] [nvarchar](50) NULL,
	[SKU_ShopCost] [money] NULL,
	[SKU_ShopCostDate] [date] NULL,
	[SKU_Vat] [money] NULL,
	[SKU_Ean] [nvarchar](50) NULL,

 */
public class Article {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int remote_id; // klucz głowny w bazie zdalnej; tutaj casto...
    private String name;
    private String description;
    // todo ??? picture;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return id == article.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
