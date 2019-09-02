package com.dev4lazy.pricecollector.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dev4lazy.pricecollector.model.utils.DateConverter;

import java.util.Date;

@Entity(tableName = "analyzes" ) // w bazie zdalnej nie ma takiej tabeli
@TypeConverters({
        DateConverter.class
})
public class Analysis {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "creation_date")
    public Date creationDate;
    @ColumnInfo(name = "due_date")
    public Date dueDate;
    @ColumnInfo(name = "finish_date")
    public Date finishDate;
    @ColumnInfo(name = "confirmation_date")
    public Date confirmationDate;
    public Boolean finished;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public Boolean isFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
