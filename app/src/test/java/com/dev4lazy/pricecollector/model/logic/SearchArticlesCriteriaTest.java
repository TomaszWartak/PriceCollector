package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.Sector;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SearchArticlesCriteriaTest {

    private SearchArticlesCriteria sac = null;

    @Before
    public void prepare() {
        sac = new SearchArticlesCriteria();
    }

    private Sector getDummySector( int sectorId ) {
        Sector result = new Sector();
        result.setId( sectorId );
        return result;
    }

    private Department getDummyDepartment(int departmentId ) {
        Department result = new Department();
        result.setId( departmentId );
        return result;
    }
    
    @Test
    public void isFilterSet() {
        sac.clearAll();
        assertEquals(false, sac.isFilterSet());

        sac.clearAll();
        sac.setArticleName("name");
        assertEquals(true, sac.isFilterSet());

        sac.clearAll();
        sac.setArticleName("name");
        sac.setArticleName("");
        assertEquals(false, sac.isFilterSet());

        sac.clearAll();
        sac.setArticleEAN("ean");
        assertEquals(true, sac.isFilterSet());

        sac.clearAll();
        sac.setArticleEAN("ean");
        sac.setArticleEAN("");
        assertEquals(false, sac.isFilterSet());

        sac.clearAll();
        sac.setArticleSKU("sku");
        assertEquals(true, sac.isFilterSet());

        sac.clearAll();
        sac.setArticleSKU("sku");
        sac.setArticleSKU("");
        assertEquals(false, sac.isFilterSet());

        sac.clearAll();
        sac.setArticleAnyText("anyText");
        assertEquals(true, sac.isFilterSet());

        sac.clearAll();
        sac.setArticleAnyText("anyText");
        sac.setArticleAnyText("");
        assertEquals(false, sac.isFilterSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(1));
        assertEquals(true, sac.isFilterSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(1));
        sac.setSelectedSector(getDummySector(0));
        assertEquals(false, sac.isFilterSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(1));
        assertEquals(true, sac.isFilterSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(1));
        sac.setSelectedDepartment(getDummyDepartment(0));
        assertEquals(false, sac.isFilterSet());

        sac.clearAll();
        sac.setDataFilterSet( true );
        assertEquals(true, sac.isFilterSet());

        sac.clearAll();
        sac.setDataFilterSet( false );
        assertEquals(false, sac.isFilterSet());

    }

    @Test
    public void isFilterNotSet() {
        sac.clearAll();
        assertEquals(true, sac.isFilterNotSet());

        sac.clearAll();
        sac.setArticleName("name");
        assertEquals(false, sac.isFilterNotSet());

        sac.clearAll();
        sac.setArticleName("name");
        sac.setArticleName("");
        assertEquals(true, sac.isFilterNotSet());

        sac.clearAll();
        sac.setArticleEAN("ean");
        assertEquals(false, sac.isFilterNotSet());

        sac.clearAll();
        sac.setArticleEAN("ean");
        sac.setArticleEAN("");
        assertEquals(true, sac.isFilterNotSet());

        sac.clearAll();
        sac.setArticleSKU("sku");
        assertEquals(false, sac.isFilterNotSet());

        sac.clearAll();
        sac.setArticleSKU("sku");
        sac.setArticleSKU("");
        assertEquals(true, sac.isFilterNotSet());

        sac.clearAll();
        sac.setArticleAnyText("anyText");
        assertEquals(false, sac.isFilterNotSet());

        sac.clearAll();
        sac.setArticleAnyText("anyText");
        sac.setArticleAnyText("");
        assertEquals(true, sac.isFilterNotSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(1));
        assertEquals(false, sac.isFilterNotSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(1));
        sac.setSelectedSector(getDummySector(0));
        assertEquals(true, sac.isFilterNotSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(1));
        assertEquals(false, sac.isFilterNotSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(1));
        sac.setSelectedDepartment(getDummyDepartment(0));
        assertEquals(true, sac.isFilterNotSet());

        sac.clearAll();
        sac.setDataFilterSet( true );
        assertEquals(false, sac.isFilterNotSet());

        sac.clearAll();
        sac.setDataFilterSet( false );
        assertEquals(true, sac.isFilterNotSet());

    }

    @Test
    public void isDataFilterSet() {
        sac.clearAll();
        assertEquals(false, sac.isDataFilterSet());

        sac.clearAll();
        sac.setArticleName("name");
        assertEquals(true, sac.isDataFilterSet());

        sac.clearAll();
        sac.setArticleName("");
        assertEquals(false, sac.isDataFilterSet());

        sac.clearAll();
        sac.setArticleEAN("ean");
        assertEquals(true, sac.isDataFilterSet());

        sac.clearAll();
        sac.setArticleEAN("");
        assertEquals(false, sac.isDataFilterSet());

        sac.clearAll();
        sac.setArticleSKU("sku");
        assertEquals(true, sac.isDataFilterSet());

        sac.clearAll();
        sac.setArticleSKU("");
        assertEquals(false, sac.isDataFilterSet());

        sac.clearAll();
        sac.setArticleAnyText("anyText");
        assertEquals(true, sac.isDataFilterSet());

        sac.clearAll();
        sac.setArticleAnyText("");
        assertEquals(false, sac.isDataFilterSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(1));
        assertEquals(false, sac.isDataFilterSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(0));
        assertEquals(false, sac.isDataFilterSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(1));
        assertEquals(false, sac.isDataFilterSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(0));
        assertEquals(false, sac.isDataFilterSet());
    }

    @Test
    public void isDataFilterNotSet() {
        sac.clearAll();
        assertEquals(true, sac.isDataFilterNotSet());

        sac.clearAll();
        sac.setArticleName("name");
        assertEquals(false, sac.isDataFilterNotSet());

        sac.clearAll();
        sac.setArticleName("");
        assertEquals(true, sac.isDataFilterNotSet());

        sac.clearAll();
        sac.setArticleEAN("ean");
        assertEquals(false, sac.isDataFilterNotSet());

        sac.clearAll();
        sac.setArticleEAN("");
        assertEquals(true, sac.isDataFilterNotSet());

        sac.clearAll();
        sac.setArticleSKU("sku");
        assertEquals(false, sac.isDataFilterNotSet());

        sac.clearAll();
        sac.setArticleSKU("");
        assertEquals(true, sac.isDataFilterNotSet());

        sac.clearAll();
        sac.setArticleAnyText("anyText");
        assertEquals(false, sac.isDataFilterNotSet());

        sac.clearAll();
        sac.setArticleAnyText("");
        assertEquals(true, sac.isDataFilterNotSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(1));
        assertEquals(true, sac.isDataFilterNotSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(0));
        assertEquals(true, sac.isDataFilterNotSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(1));
        assertEquals(true, sac.isDataFilterNotSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(0));
        assertEquals(true, sac.isDataFilterNotSet());
    }

    @Test
    public void isStructurefilterSet() {
        sac.clearAll();
        assertEquals(false, sac.isStructureFilterSet());

        sac.clearAll();
        sac.setArticleName("name");
        assertEquals(false, sac.isStructureFilterSet());

        sac.clearAll();
        sac.setArticleName("");
        assertEquals(false, sac.isStructureFilterSet());

        sac.clearAll();
        sac.setArticleEAN("ean");
        assertEquals(false, sac.isStructureFilterSet());

        sac.clearAll();
        sac.setArticleEAN("");
        assertEquals(false, sac.isStructureFilterSet());

        sac.clearAll();
        sac.setArticleSKU("sku");
        assertEquals(false, sac.isStructureFilterSet());

        sac.clearAll();
        sac.setArticleSKU("");
        assertEquals(false, sac.isStructureFilterSet());

        sac.clearAll();
        sac.setArticleAnyText("anyText");
        assertEquals(false, sac.isStructureFilterSet());

        sac.clearAll();
        sac.setArticleAnyText("");
        assertEquals(false, sac.isStructureFilterSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(1));
        assertEquals(true, sac.isStructureFilterSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(0));
        assertEquals(false, sac.isStructureFilterSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(1));
        assertEquals(true, sac.isStructureFilterSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(0));
        assertEquals(false, sac.isStructureFilterSet());

    }

    @Test
    public void isStructurefilterNotSet() {
        sac.clearAll();
        assertEquals(true, sac.isStructureFilterNotSet());

        sac.clearAll();
        sac.setArticleName("name");
        assertEquals(true, sac.isStructureFilterNotSet());

        sac.clearAll();
        sac.setArticleName("");
        assertEquals(true, sac.isStructureFilterNotSet());

        sac.clearAll();
        sac.setArticleEAN("ean");
        assertEquals(true, sac.isStructureFilterNotSet());

        sac.clearAll();
        sac.setArticleEAN("");
        assertEquals(true, sac.isStructureFilterNotSet());

        sac.clearAll();
        sac.setArticleSKU("sku");
        assertEquals(true, sac.isStructureFilterNotSet());

        sac.clearAll();
        sac.setArticleSKU("");
        assertEquals(true, sac.isStructureFilterNotSet());

        sac.clearAll();
        sac.setArticleAnyText("anyText");
        assertEquals(true, sac.isStructureFilterNotSet());

        sac.clearAll();
        sac.setArticleAnyText("");
        assertEquals(true, sac.isStructureFilterNotSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(1));
        assertEquals(false, sac.isStructureFilterNotSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(0));
        assertEquals(true, sac.isStructureFilterNotSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(1));
        assertEquals(false, sac.isStructureFilterNotSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(0));
        assertEquals(true, sac.isStructureFilterNotSet());
    }

    @Test
    public void areCriteriaSet() {
        sac.clearAll();
        assertEquals(false, sac.areCriteriaSet());

        sac.clearAll();
        sac.setArticleName("name");
        assertEquals(true, sac.areCriteriaSet());

        sac.clearAll();
        sac.setArticleName("");
        assertEquals(false, sac.areCriteriaSet());

        sac.clearAll();
        sac.setArticleEAN("ean");
        assertEquals(true, sac.areCriteriaSet());

        sac.clearAll();
        sac.setArticleEAN("");
        assertEquals(false, sac.areCriteriaSet());

        sac.clearAll();
        sac.setArticleSKU("sku");
        assertEquals(true, sac.areCriteriaSet());

        sac.clearAll();
        sac.setArticleSKU("");
        assertEquals(false, sac.areCriteriaSet());

        sac.clearAll();
        sac.setArticleAnyText("anyText");
        assertEquals(true, sac.areCriteriaSet());

        sac.clearAll();
        sac.setArticleAnyText("");
        assertEquals( false, sac.areCriteriaSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(1));
        assertEquals(true, sac.areCriteriaSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(0));
        assertEquals(false, sac.areCriteriaSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(1));
        assertEquals(true, sac.areCriteriaSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(0));
        assertEquals(false, sac.areCriteriaSet());
    }

    @Test
    public void areDataCriteriaSet() {
        sac.clearAll();
        assertEquals(false, sac.areDataCriteriaSet());

        sac.clearAll();
        sac.setArticleName("name");
        assertEquals(true, sac.areDataCriteriaSet());

        sac.clearAll();
        sac.setArticleName("");
        assertEquals(false, sac.areDataCriteriaSet());

        sac.clearAll();
        sac.setArticleEAN("ean");
        assertEquals(true, sac.areDataCriteriaSet());

        sac.clearAll();
        sac.setArticleEAN("");
        assertEquals(false, sac.areDataCriteriaSet());

        sac.clearAll();
        sac.setArticleSKU("sku");
        assertEquals(true, sac.areDataCriteriaSet());

        sac.clearAll();
        sac.setArticleSKU("");
        assertEquals(false, sac.areDataCriteriaSet());

        sac.clearAll();
        sac.setArticleAnyText("anyText");
        assertEquals(true, sac.areDataCriteriaSet());

        sac.clearAll();
        sac.setArticleAnyText("");
        assertEquals( false, sac.areDataCriteriaSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(1));
        assertEquals(false, sac.areDataCriteriaSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(0));
        assertEquals(false, sac.areDataCriteriaSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(1));
        assertEquals(false, sac.areDataCriteriaSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(0));
        assertEquals(false, sac.areDataCriteriaSet());
    }

    @Test
    public void areStructureCriteriaSet() {
        sac.clearAll();
        assertEquals(false, sac.areStructureCriteriaSet());

        sac.clearAll();
        sac.setArticleName("name");
        assertEquals(false, sac.areStructureCriteriaSet());

        sac.clearAll();
        sac.setArticleName("");
        assertEquals(false, sac.areStructureCriteriaSet());

        sac.clearAll();
        sac.setArticleEAN("ean");
        assertEquals(false, sac.areStructureCriteriaSet());

        sac.clearAll();
        sac.setArticleEAN("");
        assertEquals(false, sac.areStructureCriteriaSet());

        sac.clearAll();
        sac.setArticleSKU("sku");
        assertEquals(false, sac.areStructureCriteriaSet());

        sac.clearAll();
        sac.setArticleSKU("");
        assertEquals(false, sac.areStructureCriteriaSet());

        sac.clearAll();
        sac.setArticleAnyText("anyText");
        assertEquals(false, sac.areStructureCriteriaSet());

        sac.clearAll();
        sac.setArticleAnyText("");
        assertEquals( false, sac.areStructureCriteriaSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(1));
        assertEquals(true, sac.areStructureCriteriaSet());

        sac.clearAll();
        sac.setSelectedSector(getDummySector(0));
        assertEquals(false, sac.areStructureCriteriaSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(1));
        assertEquals(true, sac.areStructureCriteriaSet());

        sac.clearAll();
        sac.setSelectedDepartment(getDummyDepartment(0));
        assertEquals(false, sac.areStructureCriteriaSet());
    }

}