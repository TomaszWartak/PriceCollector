package com.dev4lazy.pricecollector.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "ean_codes", // w bazie zdalnej nie ma takiej tabeli
    foreignKeys = @ForeignKey(
        entity = Article.class,
        parentColumns = "id",
        childColumns = "article_id"
    )
)
public class EanCode {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int remote_id; // klucz głowny w bazie zdalnej - w tym przypadku w bazie zdalnej nie ma takiej tabeli
    public String value;
    @ColumnInfo(name = "article_id")
    public int articleId;
}
