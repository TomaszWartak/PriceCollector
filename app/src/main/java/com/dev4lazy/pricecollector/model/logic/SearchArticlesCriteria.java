package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.Sector;

public class SearchArticlesCriteria {

    private boolean filterSet;
    private boolean dataFilterSet;
    private boolean structureFilterSet;
    private String articleName;
    private String articleEAN;
    private String articleSKU;
    private String articleAnyText;
    // TODO XXX private int articleSectorId;
    // TODO XXX private int articleDepartmentId;
    private Sector selectedSector = null;
    private Department selectedDepartment = null;

    public SearchArticlesCriteria() {
        clearAll();
    }

    public void clearAll() {
        articleName = "";
        articleEAN = "";
        articleSKU = "";
        articleAnyText = "";
        // TODO XXX articleSectorId = 0;
        // TODO XXX articleDepartmentId = 0;
        selectedSector = new Sector();
        selectedDepartment = new Department();
        filterSet = false;
        dataFilterSet = false;
        structureFilterSet = false;
            /* TODO XXX
            setArticleName("");
            setArticleEAN("");
            setArticleSKU("");
            setArticleAnyText("");
            setArticleSectorId( 0 );
            setArticleDepartmentId( 0 );
             */
    }

    public boolean isFilterSet() {
        return filterSet;
    }

    public boolean isFilterNotSet() {
        return !filterSet;
    }

    private void setFilterSet(boolean filterSet) {
        this.filterSet = filterSet;
    }

    public boolean isDataFilterSet() {
        return dataFilterSet;
    }

    public boolean isDataFilterNotSet() {
        return !dataFilterSet;
    }

    public void setDataFilterSet(boolean dataFilterSet) {
        this.dataFilterSet = dataFilterSet;
        /* TODO XXX
        if (isFilterNotSet()) {
            setFilterSet(isDataFilterSet());
        }
         */
        setFilterSet( isDataFilterSet() || isStructureFilterSet() );
    }

    public boolean isStructureFilterSet() {
        return structureFilterSet;
    }

    public boolean isStructureFilterNotSet() {
        return !structureFilterSet;
    }

    public void setStructureFilterSet(boolean structureFilterSet) {
        this.structureFilterSet = structureFilterSet;
        /* TODO XXX
        if (isFilterNotSet()) {
            setFilterSet( isStructureFilterSet() );
        }
        */
        setFilterSet( isDataFilterSet() || isStructureFilterSet() );
    }

    public boolean areCriteriaSet() {
        boolean result =
                areDataCriteriaSet() ||
                areStructureCriteriaSet();
        return result;
    }

    public boolean areDataCriteriaSet() {
        boolean result =
                isArticleNameSet() ||
                        isArticleEANSet() ||
                        isArticleSKUSet() ||
                        isArticleAnyTextSet();
        return result;
    }

    public boolean areStructureCriteriaSet() {
        boolean result =
                isArticleSectorIdSet() ||
                        isArticleDepartamentIdSet();
        return result;
    }

    public boolean isArticleNameSet() {
        return !articleName.equalsIgnoreCase("");
    }

    public boolean isArticleNameNotSet() {
        return articleName.equalsIgnoreCase("");
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
        if (isDataFilterNotSet()) {
            setDataFilterSet(isArticleNameSet());
        } else {
            setDataFilterSet(areDataCriteriaSet());
        }
    }

    public boolean isArticleEANSet() {
        return !articleEAN.equalsIgnoreCase("");
    }

    public boolean isArticleEANNotSet() {
        return articleEAN.equalsIgnoreCase("");
    }

    public String getArticleEAN() {
        return articleEAN;
    }

    public void setArticleEAN(String articleEAN) {
        this.articleEAN = articleEAN;
        if (isDataFilterNotSet()) {
            setDataFilterSet(isArticleEANSet());
        } else {
            setDataFilterSet(areDataCriteriaSet());
        }
    }

    public boolean isArticleSKUSet() {
        return !articleSKU.equalsIgnoreCase("");
    }

    public boolean isArticleSKUNotSet() {
        return articleSKU.equalsIgnoreCase("");
    }

    public String getArticleSKU() {
        return articleSKU;
    }

    public void setArticleSKU(String articleSKU) {
        this.articleSKU = articleSKU;
        if (isDataFilterNotSet()) {
            setDataFilterSet(isArticleSKUSet());
        } else {
            setDataFilterSet(areDataCriteriaSet());
        }
    }

    public boolean isArticleAnyTextSet() {
        return !articleAnyText.equalsIgnoreCase("");
    }

    public boolean isArticleAnyTextNotSet() {
        return articleAnyText.equalsIgnoreCase("");
    }

    public String getArticleAnyText() {
        return articleAnyText;
    }

    public void setArticleAnyText(String articleAnyText) {
        this.articleAnyText = articleAnyText;
        if (isDataFilterNotSet()) {
            setDataFilterSet(isArticleAnyTextSet());
        } else {
            setDataFilterSet(areDataCriteriaSet());
        }
    }

    public boolean isArticleSectorIdSet() {
        return selectedSector.getId()!=0;
    }

    public boolean isArticleSectorIdNotSet() {
        return selectedSector.getId()==0;
    }

    public int getArticleSectorId() {
        return selectedSector.getId();
    }

    public void setSelectedSector(Sector sector) {
        this.selectedSector = sector;
        if (isStructureFilterNotSet()) {
            setStructureFilterSet(isArticleSectorIdSet());
        } else {
            setStructureFilterSet(areStructureCriteriaSet());
        }
    }

    public Sector getSelectedSector() {
        return selectedSector;
    }

    public Department getSelectedDepartment() {
        return selectedDepartment;
    }

    public boolean isArticleDepartamentIdSet() {
        return selectedDepartment.getId()!=0;
    }

    public boolean isArticleDepartamentNotSet() {
        return selectedDepartment.getId()==0;
    }

    public int getArticleDepartmentId() {
        return selectedDepartment.getId();
    }

    public void setSelectedDepartment(Department department) {
        this.selectedDepartment = department;
        if (isStructureFilterNotSet()) {
            setStructureFilterSet(isArticleDepartamentIdSet());
        } else {
            setStructureFilterSet(areStructureCriteriaSet());
        }
    }

}
