package com.dev4lazy.pricecollector.model.utils;

import com.dev4lazy.pricecollector.R;

public enum StoreStructureType {

    // nie zmieniaj kolejności
    // nowe dodaj na koniec
    // todo czy description może być zasobem np .getString(R.string.departments_structure_description)

    BY_SECTORS("struktura sektorowa", true),
    BY_MARKETS ("struktura wg rynków", false),
    BY_DEPARTMENTS ("struktura działowa", true);

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
