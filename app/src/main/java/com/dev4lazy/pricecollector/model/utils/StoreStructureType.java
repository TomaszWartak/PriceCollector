package com.dev4lazy.pricecollector.model.utils;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.R;

public enum StoreStructureType {

    BY_SECTORS( AppHandle.getHandle().getString(R.string.sectoral_structure), true),
    BY_MARKETS ( AppHandle.getHandle().getString(R.string.structure_by_markets), false),
    BY_DEPARTMENTS ( AppHandle.getHandle().getString(R.string.department_structure), true);

    private String description;
    private Boolean active;

    StoreStructureType(String description, Boolean active) {
        this.description = description;
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isActive() { return active; }

    // public final ordinal()- zwraca wartość int informującą o miejscu deklaracji danej stałej w wyliczeniu
}
