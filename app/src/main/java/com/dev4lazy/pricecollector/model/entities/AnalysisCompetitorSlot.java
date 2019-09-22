package com.dev4lazy.pricecollector.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "competitor_slots" )
public class AnalysisCompetitorSlot {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "slot_nr")
    private int slotNr;
    @ColumnInfo(name = "company_id")
    private int companyId;
    @ColumnInfo(name = "other_store_id")
    private int otherStoreId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSlotNr() {
        return slotNr;
    }

    public void setSlotNr(int slotNr) {
        this.slotNr = slotNr;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getOtherStoreId() {
        return otherStoreId;
    }

    public void setOtherStoreId(int otherStoreId) {
        this.otherStoreId = otherStoreId;
    }
}
