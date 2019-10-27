package com.dev4lazy.pricecollector.remote_data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "ean_codes", // w bazie zdalnej nie ma takiej tabeli
    foreignKeys = {
        @ForeignKey(
            //entity = Article.class,
            entity = RemoteAnalysisRow.class,
            parentColumns = "articleCode", // kod casto
            childColumns = "article_id"
        )
    },
    indices = {
        @Index(value = "article_id")
    }
)
public class RemoteEanCode {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int remote_id; // klucz głowny w bazie zdalnej - w tym przypadku w bazie zdalnej nie ma takiej tabeli
    private String value;
    @ColumnInfo(name = "article_id")
    private int articleId; // tutaj casto, jako article_id

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

    public static class EanCodeBuilder {

        private RemoteEanCode remoteEanCode = new RemoteEanCode();

        public EanCodeBuilder id (Integer id) {
            remoteEanCode.id = id;
            return this;
        }

        public EanCodeBuilder remote_id (Integer remote_id) {
            remoteEanCode.remote_id = remote_id;
            return this;
        }

        public EanCodeBuilder value (String value) {
            remoteEanCode.value = value;
            return this;
        }

        public EanCodeBuilder articleId (Integer articleId) {
            remoteEanCode.articleId = articleId;
            return this;
        }

        public RemoteEanCode build() {
            return remoteEanCode;
        }
    }
    
}
