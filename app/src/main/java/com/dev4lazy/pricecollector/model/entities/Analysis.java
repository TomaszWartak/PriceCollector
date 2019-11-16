package com.dev4lazy.pricecollector.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dev4lazy.pricecollector.model.utils.DateConverter;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "analyzes" ) // w bazie zdalnej nie ma takiej tabeli
@TypeConverters({
        DateConverter.class
})
public class Analysis {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "creation_date")
    private Date creationDate;
    @ColumnInfo(name = "due_date")
    private Date dueDate;
    @ColumnInfo(name = "finish_date")
    private Date finishDate;
    @ColumnInfo(name = "confirmation_date")
    private Date confirmationDate;
    private Boolean finished;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Analysis)) return false;
        Analysis analysis = (Analysis) o;
        return id == analysis.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
