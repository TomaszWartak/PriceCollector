package com.dev4lazy.pricecollector.model.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "own_articles_infos",  // w bazie zdalnej nie ma takiej tabeli - zob. [dbo].[DCT_Article]
    foreignKeys = {
        @ForeignKey( entity = Article.class, parentColumns = "id", childColumns = "article_id" ),
        @ForeignKey( entity = Sector.class, parentColumns = "id", childColumns = "sector_id" ),
        @ForeignKey( entity = Department.class, parentColumns = "id", childColumns = "department_id" ),
        @ForeignKey( entity = Family.class, parentColumns = "id", childColumns = "family_id" ),
        @ForeignKey( entity = Module.class, parentColumns = "id", childColumns = "module_id" ),
        @ForeignKey( entity = UOProject.class, parentColumns = "id", childColumns = "uo_project_id" )
    },
    indices = {
        @Index( value = "article_id", unique = true ),
        @Index( value = "sector_id" ),
        @Index( value = "department_id" ),
        @Index( value = "family_id" ),
        @Index( value = "module_id" ),
        @Index( value = "uo_project_id" )
    }
)
public class OwnArticleInfo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int remoteId; // klucz głowny w bazie zdalnej - w tym przypadku w bazie zdalnej nie ma takiej tabeli
    @ColumnInfo(name = "article_id")
    private int articleId;
    private String ownCode; // kod briko
    private int sapCode;
    private int magentoCode;
    private double refPrice; // todo ok: to chyba nie jest potrzebne, bo jest w CompetitorPrice.
    //   Nie... Gdyż CompetitorPrice jest tworzone dopiero, gdy zapisujesz dane w trakcoe badania cen
    private double storePrice; // todo ok: to chyba nie jest potrzebne, bo jest w CompetitorPrice
    //   Nie... Gdyż CompetitorPrice jest tworzone dopiero, gdy zapisujesz dane w trakcoe badania cen
    private double storeMarginPercent; // todo ok: to chyba nie jest potrzebne, bo jest w CompetitorPrice
    //   Nie... Gdyż CompetitorPrice jest tworzone dopiero, gdy zapisujesz dane w trakcoe badania cen
    @ColumnInfo(name = "sector_id")
    private int sectorId;
    @ColumnInfo(name = "market_id")
    private int marketId;
    @ColumnInfo(name = "department_id")
    private int departmentId;
    @ColumnInfo(name = "family_id")
    private int familyId;
    @ColumnInfo(name = "module_id")
    private int moduleId;
    @ColumnInfo(name = "uo_project_id")
    private int uoProjectId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(int remoteId) {
        this.remoteId = remoteId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getOwnCode() {
        return ownCode;
    }

    public void setOwnCode(String ownCode) {
        this.ownCode = ownCode;
    }

    public int getSapCode() {
        return sapCode;
    }

    public void setSapCode(int sapCode) {
        this.sapCode = sapCode;
    }

    public int getMagentoCode() {
        return magentoCode;
    }

    public void setMagentoCode(int magentoCode) {
        this.magentoCode = magentoCode;
    }

    public double getRefPrice() {
        return refPrice;
    }

    public void setRefPrice(double refPrice) {
        this.refPrice = refPrice;
    }

    public double getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(double storePrice) {
        this.storePrice = storePrice;
    }

    public double getStoreMarginPercent() {
        return storeMarginPercent;
    }

    public void setStoreMarginPercent(double storeMarginPercent) {
        this.storeMarginPercent = storeMarginPercent;
    }

    public int getSectorId() {
        return sectorId;
    }

    public void setSectorId(int sectorId) {
        this.sectorId = sectorId;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getUoProjectId() {
        return uoProjectId;
    }

    public void setUoProjectId(int uoProjectId) {
        this.uoProjectId = uoProjectId;
    }
}
