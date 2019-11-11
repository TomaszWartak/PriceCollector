package com.dev4lazy.pricecollector.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity( tableName = "departments_in_sector",
        foreignKeys = {
                @ForeignKey(
                        entity = Sector.class,
                        parentColumns = "id",
                        childColumns = "sector_id"
                ),
                @ForeignKey(
                        entity = Department.class,
                        parentColumns = "id",
                        childColumns = "department_id"
                )
        },
        indices = {
                @Index(
                        value = "sector_id"
                ),
                @Index(
                        value = "department_id"
                )
        }
)
public class DepartmentInSector {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "sector_id")
    private int sectorId;

    @ColumnInfo(name = "department_id")
    private int departmentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSectorId() {
        return sectorId;
    }

    public void setSectorId(int sectorId) {
        this.sectorId = sectorId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepartmentInSector)) return false;
        DepartmentInSector that = (DepartmentInSector) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
