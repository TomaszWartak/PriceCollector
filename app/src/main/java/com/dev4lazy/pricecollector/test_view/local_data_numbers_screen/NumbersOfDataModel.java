package com.dev4lazy.pricecollector.test_view.local_data_numbers_screen;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dev4lazy.pricecollector.model.db.LocalDatabase;


public class NumbersOfDataModel extends AndroidViewModel {

    private LiveData<Integer> numberOfAnalysis;
    private LiveData<Integer> numberOfAnalysisArticle;
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

    public NumbersOfDataModel(Application application) {
        super(application);
        LocalDatabase localDatabase = LocalDatabase.getInstance();
        numberOfAnalysis = localDatabase.analysisDao().getNumberOfLiveData();
        numberOfAnalysisArticle = localDatabase.analysisArticleDao().getNumberOfLiveData();
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

    public LiveData<Integer> getNumberOfAnalysisArticle() {
        return numberOfAnalysisArticle;
    }

    public LiveData<Integer> getNumberOfAnalysisCompetitorSlot() {
        return numberOfAnalysisCompetitorSlot;
    }

    public LiveData<Integer> getNumberOfArticle() {
        return numberOfArticle;
    }

    public LiveData<Integer> getNumberOfCompany() {
        return numberOfCompany;
    }

    public LiveData<Integer> getNumberOfCompetitorPrice() {
        return numberOfCompetitorPrice;
    }

    public LiveData<Integer> getNumberOfCountry() {
        return numberOfCountry;
    }

    public LiveData<Integer> getNumberOfDepartment() {
        return numberOfDepartment;
    }

    public LiveData<Integer> getNumberOfDepartmentInSector() {
        return numberOfDepartmentInSector;
    }

    public LiveData<Integer> getNumberOfEanCode() {
        return numberOfEanCode;
    }

    public LiveData<Integer> getNumberOfFamily() {
        return numberOfFamily;
    }

    public LiveData<Integer> getNumberOfMarket() {
        return numberOfMarket;
    }

    public LiveData<Integer> getNumberOfModule() {
        return numberOfModule;
    }

    public LiveData<Integer> getNumberOfOwnArticleInfo() {
        return numberOfOwnArticleInfo;
    }

    public LiveData<Integer> getNumberOfOwnStore() {
        return numberOfOwnStore;
    }

    public LiveData<Integer> getNumberOfSector() {
        return numberOfSector;
    }

    public LiveData<Integer> getNumberOfStore() {
        return numberOfStore;
    }

    public LiveData<Integer> getNumberOfUOProject() {
        return numberOfUOProject;
    }

}
