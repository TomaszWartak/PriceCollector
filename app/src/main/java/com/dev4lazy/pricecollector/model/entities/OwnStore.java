package com.dev4lazy.pricecollector.model.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dev4lazy.pricecollector.model.utils.DateConverter;
import com.dev4lazy.pricecollector.model.utils.StoreStructureType;
import com.dev4lazy.pricecollector.model.utils.StoreStructureTypeConverter;

@Entity(
    tableName = "own_stores", // w bazie zdalnej nie ma takiej tabeli - zob. [dbo].[DCT_Shop],
    inheritSuperIndices = true
)
public class OwnStore extends Store {
    // todo ??? czy w bazie zdalnej jest taka tabela
    public String ownNumber;
    public String sapOwnNumber;
    public StoreStructureType structureType;
}
