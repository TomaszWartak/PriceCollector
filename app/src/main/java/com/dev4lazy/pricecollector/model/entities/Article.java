package com.dev4lazy.pricecollector.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
    public int id;
    public int remote_id; // klucz głowny w bazie zdalnej
    public String name;
    public String description;
    // todo ??? picture;
}
