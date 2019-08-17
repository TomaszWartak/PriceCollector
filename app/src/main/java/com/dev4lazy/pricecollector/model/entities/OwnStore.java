package com.dev4lazy.pricecollector.model.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.TypeConverters;

import com.dev4lazy.pricecollector.model.utils.DateConverter;
import com.dev4lazy.pricecollector.model.utils.StoreStructureType;
import com.dev4lazy.pricecollector.model.utils.StoreStructureTypeConverter;

@Entity(
    tableName = "own_stores", // w bazie zdalnej nie ma takiej tabeli - zob. [dbo].[DCT_Shop],
    foreignKeys = @ForeignKey(
            entity = Company.class,
            parentColumns = "id",
            childColumns = "company_id"
    )
)
@TypeConverters({
        DateConverter.class,
        StoreStructureTypeConverter.class
})
public class OwnStore extends Store {
    // todo ??? czy w bazie zdalnej jest taka tabela
    public int remote_id; // klucz głowny w bazie zdalnej - w tym przypadku w bazie zdalnej nie ma takiej tabeli

    public String ownNumber;
    public String sapOwnNumber;
    public StoreStructureType structureType;
}
