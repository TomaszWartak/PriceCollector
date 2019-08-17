package com.dev4lazy.pricecollector.model.utils;

import androidx.room.TypeConverter;

public class StoreStructureTypeConverter {
    @TypeConverter
    public StoreStructureType int2NoteKind(int kind) {
        return StoreStructureType.values()[kind];
    }

    @TypeConverter
    public int noteKind2Int(StoreStructureType kind) {
        return kind.ordinal();
    }
}
