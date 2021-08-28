package com.dev4lazy.pricecollector.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "competitor_slots" )
public class AnalysisCompetitorSlot {
    @Ignore
    public static final int NONE = -1;
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "slot_nr")
    private int slotNr;
    @ColumnInfo(name = "company_id")
    private int companyId;
    @ColumnInfo(name = "other_store_id")
    private int otherStoreId;
    @ColumnInfo(name = "company_name")
    private String companyName;
    @ColumnInfo(name = "other_store_name")
    private String storeName;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     * Ustawia dane pustego slotu
     * @param slotNr nr kolejny slotu (1..5)
     * @param companyId id bazodanowy firmy
     * @param companyName nazwa firmy
     */
    public void initialize( int slotNr, int companyId, String companyName) {
        setSlotNr(slotNr);
        setOtherStoreId( NONE );
        setCompanyId( companyId );
        setCompanyName( companyName );
        setStoreName( "" );
    }

    /**
     * Czyści slot dla danego Konkurenta
     */

    public void reset( ) {
        initialize( getSlotNr(), getCompanyId(), getCompanyName() );
    }
}
