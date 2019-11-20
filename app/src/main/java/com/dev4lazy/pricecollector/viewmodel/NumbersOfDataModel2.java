package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dev4lazy.pricecollector.model.db.LocalDatabase;


public class NumbersOfDataModel2 extends AndroidViewModel {

    private LiveData<Integer> numberOfAnalysis;
    private LiveData<Integer> numberOfAnalysisArticle;
    private LiveData<Integer> numberOfAnalysisArticleShortData;
    private LiveData<Integer> numberOfAnalysisCompetitorSlot;
    private LiveData<Integer> numberOfArticle;
    private LiveData<Integer> numberOfCompany;
    private LiveData<Integer> numberOfCompetitorPrice;
    private LiveData<Integer> numberOfCountry;
    private LiveData<Integer> numberOfDepartment;
    private LiveData<Integer> numberOfDepartmentInSector;
    private LiveData<Integer> numberOfEanCode;
    private LiveData<Integer> numberOfFamily;
    private LiveData<Integer> numberOfMarket;
    private LiveData<Integer> numberOfModule;
    private LiveData<Integer> numberOfOwnArticleInfo;
    private LiveData<Integer> numberOfOwnStore;
    private LiveData<Integer> numberOfSector;
    private LiveData<Integer> numberOfStore;
    private LiveData<Integer> numberOfUOProject;

    public NumbersOfDataModel2(Application application) {
        super(application);
        LocalDatabase localDatabase = LocalDatabase.getInstance();
        numberOfAnalysis = localDatabase.analysisDao().getNumberOfLiveData();
        numberOfAnalysisArticle = localDatabase.analysisArticleDao().getNumberOfLiveData();
        // numberOfAnalysisArticleShortData = localDatabase.analysisArticleShortDataDao().getNumberOfLiveData();
        numberOfAnalysisCompetitorSlot = localDatabase.analysisCompetitorSlotDao().getNumberOfLiveData();
        numberOfArticle = localDatabase.articleDao().getNumberOfLiveData();
        numberOfCompany = localDatabase.companyDao().getNumberOfLiveData();
        numberOfCompetitorPrice = localDatabase.competitorPriceDao().getNumberOfLiveData();
        numberOfCountry = localDatabase.countryDao().getNumberOfLiveData();
        numberOfDepartment = localDatabase.departmentDao().getNumberOfLiveData();
        numberOfDepartmentInSector = localDatabase.departmentInSectorDao().getNumberOfLiveData();
        numberOfEanCode = localDatabase.eanCodeDao().getNumberOfLiveData();
        numberOfFamily = localDatabase.familyDao().getNumberOfLiveData();
        numberOfMarket = localDatabase.marketDao().getNumberOfLiveData();
        numberOfModule = localDatabase.moduleDao().getNumberOfLiveData();
        numberOfOwnArticleInfo = localDatabase.ownArticleInfoDao().getNumberOfLiveData();
        numberOfOwnStore = localDatabase.ownStoreDao().getNumberOfLiveData();
        numberOfSector = localDatabase.sectorDao().getNumberOfLiveData();
        numberOfStore = localDatabase.storeDao().getNumberOfLiveData();
        numberOfUOProject = localDatabase.uoProjectDao().getNumberOfLiveData();
    }

    public LiveData<Integer> getNumberOfAnalysis() {
        return numberOfAnalysis;
    }

    /* todo??? i niżej
    public void setNumberOfAnalysis(Integer nrOfAnalysis) {
        this.numberOfAnalysis.setValue( nrOfAnalysis );
    }

     */

    public LiveData<Integer> getNumberOfAnalysisArticle() {
        return numberOfAnalysisArticle;
    }
/*
    public void setNumberOfAnalysisArticle(Integer nrOfAnalysisArticle) {
        this.numberOfAnalysisArticle.setValue( nrOfAnalysisArticle );
    }

 */

    public LiveData<Integer> getNumberOfAnalysisArticleShortData() {
        return numberOfAnalysisArticleShortData;
    }
/*
    public void setNumberOfAnalysisArticleShortData(Integer nrOfAnalysisArticleShortData) {
        this.numberOfAnalysisArticleShortData.setValue( nrOfAnalysisArticleShortData );
    }

 */

    public LiveData<Integer> getNumberOfAnalysisCompetitorSlot() {
        return numberOfAnalysisCompetitorSlot;
    }
/*
    public void setNumberOfAnalysisCompetitorSlot(Integer nrOfAnalysisCompetitorSlot) {
        this.numberOfAnalysisCompetitorSlot.setValue( nrOfAnalysisCompetitorSlot );
    }
 */
    public LiveData<Integer> getNumberOfArticle() {
        return numberOfArticle;
    }

    /* public void setNumberOfArticle(Integer nrOfArticle) {
        this.numberOfArticle.setValue( nrOfArticle );
    }

     */

    public LiveData<Integer> getNumberOfCompany() {
        return numberOfCompany;
    }

    /* public void setNumberOfCompany(Integer nrOfCompany) {
        this.numberOfCompany.setValue( nrOfCompany );
    }

     */

    public LiveData<Integer> getNumberOfCompetitorPrice() {
        return numberOfCompetitorPrice;
    }

    /* public void setNumberOfCompetitorPrice(Integer nrOfCompetitorPrice) {
        this.numberOfCompetitorPrice.setValue( nrOfCompetitorPrice );
    }

     */

    public LiveData<Integer> getNumberOfCountry() {
        return numberOfCountry;
    }

    /* public void setNumberOfCountry(Integer nrOfCountry) {
        this.numberOfCountry.setValue( nrOfCountry );
    }

     */

    public LiveData<Integer> getNumberOfDepartment() {
        return numberOfDepartment;
    }

    /* public void setNumberOfDepartment(Integer nrOfDepartment) {
        this.numberOfDepartment.setValue( nrOfDepartment );
    }

     */

    public LiveData<Integer> getNumberOfDepartmentInSector() {
        return numberOfDepartmentInSector;
    }

    /* public void setNumberOfDepartmentInSector(Integer nrOfDepartmentInSector) {
        this.numberOfDepartmentInSector.setValue( nrOfDepartmentInSector );
    }

     */

    public LiveData<Integer> getNumberOfEanCode() {
        return numberOfEanCode;
    }

    /* public void setNumberOfEanCode(Integer nrOfEanCode) {
        this.numberOfEanCode.setValue( nrOfEanCode );
    }

     */

    public LiveData<Integer> getNumberOfFamily() {
        return numberOfFamily;
    }

    /* public void setNumberOfFamily(Integer nrOfFamily) {
        this.numberOfFamily.setValue( nrOfFamily );
    }

     */

    public LiveData<Integer> getNumberOfMarket() {
        return numberOfMarket;
    }

    /* public void setNumberOfMarket(Integer nrOfMarket) {
        this.numberOfMarket.setValue( nrOfMarket );
    }

     */

    public LiveData<Integer> getNumberOfModule() {
        return numberOfModule;
    }

    /* public void setNumberOfModule(Integer nrOfModule) {
        this.numberOfModule.setValue( nrOfModule );
    }

     */

    public LiveData<Integer> getNumberOfOwnArticleInfo() {
        return numberOfOwnArticleInfo;
    }

    /* public void setNumberOfOwnArticleInfo(Integer nrOfOwnArticleInfo) {
        this.numberOfOwnArticleInfo.setValue( nrOfOwnArticleInfo );
    }

     */

    public LiveData<Integer> getNumberOfOwnStore() {
        return numberOfOwnStore;
    }

    /* public void setNumberOfOwnStore(Integer nrOfOwnStore) {
        this.numberOfOwnStore.setValue( nrOfOwnStore );
    }

     */

    public LiveData<Integer> getNumberOfSector() {
        return numberOfSector;
    }

    /* public void setNumberOfSector(Integer nrOfSector) {
        this.numberOfSector.setValue( nrOfSector );
    }

     */

    public LiveData<Integer> getNumberOfStore() {
        return numberOfStore;
    }

    /* public void setNumberOfStore(Integer nrOfStore) {
        this.numberOfStore.setValue( nrOfStore );
    }

     */

    public LiveData<Integer> getNumberOfUOProject() {
        return numberOfUOProject;
    }

    /* public void setNumberOfUOProject(Integer nrOfUOProject) {
        this.numberOfUOProject.setValue( nrOfUOProject );
    }

     */


}
