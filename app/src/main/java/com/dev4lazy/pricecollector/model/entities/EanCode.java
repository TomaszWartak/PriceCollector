package com.dev4lazy.pricecollector.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "ean_codes", // w bazie zdalnej nie ma takiej tabeli
    foreignKeys = {
        @ForeignKey(
            entity = Article.class,
            parentColumns = "id",
            childColumns = "article_id"
        )
    },
    indices = {
        @Index(value = "article_id")
    }
)
public class EanCode {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int remote_id; // klucz głowny w bazie zdalnej - w tym przypadku w bazie zdalnej nie ma takiej tabeli
    public String value;
    @ColumnInfo(name = "article_id")
    public int articleId;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }
}
