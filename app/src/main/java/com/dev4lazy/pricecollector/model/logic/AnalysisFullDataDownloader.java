package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.EanCode;
import com.dev4lazy.pricecollector.model.entities.Family;
import com.dev4lazy.pricecollector.model.entities.Market;
import com.dev4lazy.pricecollector.model.entities.Module;
import com.dev4lazy.pricecollector.model.entities.OwnArticleInfo;
import com.dev4lazy.pricecollector.model.entities.Sector;
import com.dev4lazy.pricecollector.model.entities.UOProject;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteEanCode;
import com.dev4lazy.pricecollector.utils.TaskChain;
import com.dev4lazy.pricecollector.utils.TaskLink;
import com.dev4lazy.pricecollector.view.utils.ProgressPresentingManager;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.CustomSql;
import com.healthmarketscience.sqlbuilder.SelectQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import static com.dev4lazy.pricecollector.view.utils.ProgressPresenter.DONT_HIDE_WHEN_FINISHED;
import static com.dev4lazy.pricecollector.view.utils.ProgressPresenter.HIDE_WHEN_FINISHED;

/**
 * AnalysisFullDataDownloader
 *
 * Służy do pobrania danych potrzebnych do badania cen z serwera zdalnego (serwera Aplikacji Badanie Cen).
 *
 */
public class AnalysisFullDataDownloader {

    private Analysis analysis;
    private ProgressPresentingManager progressPresentingManager;
    private MutableLiveData<Boolean> finalResult;
    private ArrayList<RemoteAnalysisRow> classScopeRemoteAnalysisRowsList;
    private HashMap<Integer, Article> classScopeArticleMap; // klucz: Article.getRemote_id()
    private HashMap<String, Sector> classScopeSectorMap; // klucz: Sector.getName()
    private HashMap<String, Department> classScopeDepartmentMap; // klucz: Department.getSymbol()
    private HashMap<Integer, OwnArticleInfo> classScopeOwnArticleInfoMap; // OwnArticleInfo.getArticleId()
    private Family classScopeDummyFamily;
    private Market classScopeDummyMarket;
    private Module classScopeDummyModule;
    private UOProject classScopeDummyUOProject;

    public void downloadData(
            Analysis analysis,
            MutableLiveData<Boolean> finalResult,
            ProgressPresentingManager progressPresentingManager ) {
        this.analysis = analysis;
        this.progressPresentingManager = progressPresentingManager;
        this.finalResult = finalResult;
        new TaskChain()
                .addTaskLink(new RemoteAnalysisRowsGetter())
                .addTaskLink(new NewArticlesListMaker())
                .addTaskLink(new AllArticlesGetter())
                .addTaskLink(new NewArticlesDuplicatesRemover())
                .addTaskLink(new NewArticlesSaver())
                .addTaskLink(new AllArticlesGetter())
                .addTaskLink(new AllArticlesMapCreator())
                .addTaskLink(new RemoteEanCodesGetter())
                .addTaskLink(new NewEanCodesListMaker())
                .addTaskLink(new AllEanCodesGetter())
                .addTaskLink(new NewEanCodesDuplicatesRemover())
                .addTaskLink(new NewEanCodesSaver())
                .addTaskLink(new SectorsGetter())
                .addTaskLink(new SectorsMapCreator())
                .addTaskLink(new DepartmentsGetter())
                .addTaskLink(new DepartmentsMapCreator())
                .addTaskLink(new FamiliesGetter())
                .addTaskLink(new DummyFamilyCreator())
                .addTaskLink(new MarketsGetter())
                .addTaskLink(new DummyMarketCreator())
                .addTaskLink(new ModulesGetter())
                .addTaskLink(new DummyModuleCreator())
                .addTaskLink(new UOProjectGetter())
                .addTaskLink(new DummyUOProjectCreator())
                .addTaskLink(new AllOwnArticleInfosGetter())
                .addTaskLink(new NewOwnArticleInfosListMaker())
                .addTaskLink(new NewOwnArticleInfosDuplicatesRemover())
                .addTaskLink(new NewOwnArticleInfosSaver())
                .addTaskLink(new AllOwnArticleInfosGetter())
                .addTaskLink(new OwnArticleInfoMapMaker())
                .addTaskLink(new AnalysisArticlesListMaker())
                .addTaskLink(new AnalysisArticlesSaver())
                .addTaskLink(new AnalysisUpdater())
                .startIt( );

    }

    private class RemoteAnalysisRowsGetter extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            MutableLiveData<List<RemoteAnalysisRow>> result = new MutableLiveData<>();
            Observer<List<RemoteAnalysisRow>> resultObserver = new Observer<List<RemoteAnalysisRow>>() {
                @Override
                public void onChanged(List<RemoteAnalysisRow> remoteAnalysisRowsList) {
                    if ((remoteAnalysisRowsList != null)&&(!remoteAnalysisRowsList.isEmpty())) {
                        result.removeObserver(this); // this = observer...
                        classScopeRemoteAnalysisRowsList = (ArrayList)remoteAnalysisRowsList;
                        runNextTaskLink();
                    }
                }
            };
            result.observeForever(resultObserver);
            String remoteAnalysisRowQueryString = getQuery( analysis.getRemote_id() );
            AppHandle.getHandle().getRepository().getRemoteDataRepository().getRemoteAnalysisRowViaQuery( remoteAnalysisRowQueryString, result );
        }

        private String getQuery( int remoteAnalysisId ) {
            SelectQuery analysisArticlesJoinWithPricesQuery = new SelectQuery()
                    .addAllColumns( )
                    .addCustomFromTable("analysis_rows")
                    .addCondition( BinaryCondition.equalTo( new CustomSql( "analysis_rows.analysisId" ), remoteAnalysisId ) );
            analysisArticlesJoinWithPricesQuery = analysisArticlesJoinWithPricesQuery.validate();
            return analysisArticlesJoinWithPricesQuery.toString();
        }

    }

    private class NewArticlesListMaker extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            Remote2LocalConverter converter = new Remote2LocalConverter();
            ArrayList<Article> newArticlesList = converter.createArticlesList( classScopeRemoteAnalysisRowsList );
            runNextTaskLink( newArticlesList );
        }
    }

    private class AllArticlesGetter extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<Article> newArticlesList = (List<Article>)data[0];
            MutableLiveData<List<Article>> result = new MutableLiveData<>();
            Observer<List<Article>> resultObserver = new Observer<List<Article>>() {
                @Override
                public void onChanged(List<Article> allArticlesList) {
                    if (allArticlesList != null) {
                        result.removeObserver(this); // this = observer...
                        runNextTaskLink( newArticlesList, allArticlesList );
                    }
                }
            };
            result.observeForever(resultObserver);
            AppHandle.getHandle().getRepository().getLocalDataRepository().getAllArticles(result);
        }
    }

    private class NewArticlesDuplicatesRemover extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<Article> newArticlesList = (List<Article>)data[0];
            List<Article> allArticlesList = (List<Article>)data[1];
            if (!allArticlesList.isEmpty()) {
                HashMap<Integer,Article> allArticlesMap = new HashMap<>(
                        allArticlesList
                                .stream()
                                .collect( Collectors.toMap( Article::getRemote_id, article->article ) ) );
                newArticlesList = newArticlesList
                        .stream()
                        .filter( article -> !allArticlesMap.containsKey( article.getRemote_id() ) )
                        .collect( Collectors.toList() );
            }
            runNextTaskLink( newArticlesList );
        }
    }

    private class NewArticlesSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            ArrayList<Article> newArticlesList =  (ArrayList<Article>)data[0];
            if (newArticlesList.size()>0) {
                MutableLiveData<Long> insertResult = new MutableLiveData<>();
                Observer<Long> insertingResultObserver = new Observer<Long>() {
                    @Override
                    public void onChanged(Long lastAddedArticleId) {
                        insertResult.removeObserver(this); // this = observer...
                        if (lastAddedArticleId != null) {
                            runNextTaskLink(newArticlesList);
                        }
                    }
                };
                progressPresentingManager.resetProgressPresenter( newArticlesList.size(), DONT_HIDE_WHEN_FINISHED );
                progressPresentingManager.showMessagePresenter( AppHandle.getHandle().getString(R.string.articles_list) ); 
                insertResult.observeForever(insertingResultObserver);
                LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                localDataRepository.insertArticles( newArticlesList, insertResult, progressPresentingManager.getProgressPresenterWrapper() );
            } else {
                runNextTaskLink( newArticlesList );
            }
        }
    }

    // tutaj w łańcuchu jest wywołanie AllArticlesGetter()

    private class AllArticlesMapCreator extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<Article> allArticlesList = (List<Article>)data[1];
            classScopeArticleMap = new HashMap<>(
                    allArticlesList
                            .stream()
                            .collect( Collectors.toMap( Article::getRemote_id, article->article ) ) );
            runNextTaskLink( allArticlesList );
        }
    }

    private class RemoteEanCodesGetter extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<Article> allArticlesList = (List<Article>)data[0];
            MutableLiveData<List<RemoteEanCode>> result = new MutableLiveData<>();
            Observer<List<RemoteEanCode>> resultObserver = new Observer<List<RemoteEanCode>>() {
                @Override
                public void onChanged(List<RemoteEanCode> remoteEanCodesList) {
                    if ((remoteEanCodesList != null)&&(!remoteEanCodesList.isEmpty())) {
                        result.removeObserver(this); // this = observer...
                        runNextTaskLink( remoteEanCodesList, allArticlesList);
                    }
                }
            };
            result.observeForever(resultObserver);
            AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllRemoteEanCodes(result);
        }
    }

    private class NewEanCodesListMaker extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<RemoteEanCode> remoteEanCodesList = (List<RemoteEanCode>)data[0];
            List<Article> allArticlesList = (List<Article>)data[1];
            ArrayList<EanCode> newEanCodeList = convertToEanCodes( remoteEanCodesList, allArticlesList);
            runNextTaskLink(newEanCodeList);
        }

        private ArrayList<EanCode> convertToEanCodes (
                List<RemoteEanCode> remoteEanCodesList,
                List<Article> allArticlesList) {
            HashMap<Integer, RemoteEanCode> remoteEanCodesHashMap = new HashMap<>(
                remoteEanCodesList
                    .stream()
                    .collect( Collectors.toMap( RemoteEanCode::getArticleId, remoteEanCode -> remoteEanCode ))
            );
            HashMap<Integer, Article> articlesHashMap = new HashMap<>(
                allArticlesList
                    .stream()
                    .collect( Collectors.toMap( Article::getRemote_id, article -> article ) )
            );
            Remote2LocalConverter converter = new Remote2LocalConverter();
            return converter.createEanCodesList( remoteEanCodesHashMap, articlesHashMap );
        }

    }

    private class AllEanCodesGetter extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<EanCode> newEanCodesList = (List<EanCode>)data[0];
            MutableLiveData<List<EanCode>> result = new MutableLiveData<>();
            Observer<List<EanCode>> resultObserver = new Observer<List<EanCode>>() {
                @Override
                public void onChanged(List<EanCode> allEanCodesList) {
                    if (allEanCodesList != null) {
                        result.removeObserver(this); // this = observer...
                        runNextTaskLink( newEanCodesList, allEanCodesList );
                    }
                }
            };
            result.observeForever(resultObserver);
            AppHandle.getHandle().getRepository().getLocalDataRepository().getAllEanCodes( result );
        }
    }

    private class NewEanCodesDuplicatesRemover extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<EanCode> newEanCodesList = (List<EanCode>)data[0];
            List<EanCode> allEanCodesList = (List<EanCode>)data[1];
            if (!allEanCodesList.isEmpty()) {
                HashMap<String,EanCode> allEanCodesMap = new HashMap<>(
                        allEanCodesList
                                .stream()
                                .collect( Collectors.toMap( EanCode::getValue, eanCode->eanCode ) ) );
                newEanCodesList = newEanCodesList
                        .stream()
                        .filter( eanCode -> !allEanCodesMap.containsKey( eanCode.getValue() ) )
                        .collect( Collectors.toList() );
            }
            runNextTaskLink(newEanCodesList);
        }
    }

    private class NewEanCodesSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            ArrayList<EanCode> newEanCodesList =  (ArrayList<EanCode>)data[0];
            if (newEanCodesList.size()>0) {
                MutableLiveData<Long> insertResult = new MutableLiveData<>();
                Observer<Long> insertingResultObserver = new Observer<Long>() {
                    @Override
                    public void onChanged(Long lastAddedEanCodeId) {
                        insertResult.removeObserver(this); // this = observer...
                        if (lastAddedEanCodeId != null) {
                            runNextTaskLink();
                        }
                    }
                };
                progressPresentingManager.resetProgressPresenter( newEanCodesList.size(), DONT_HIDE_WHEN_FINISHED );
                progressPresentingManager.showMessagePresenter( AppHandle.getHandle().getString(R.string.ean_codes_list) );
                insertResult.observeForever(insertingResultObserver);
                LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                localDataRepository.insertEanCodes( newEanCodesList, insertResult, progressPresentingManager.getProgressPresenterWrapper() );
            } else {
                runNextTaskLink();
            }
        }
    }

    private class SectorsGetter extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            MutableLiveData<List<Sector>> result = new MutableLiveData<>();
            Observer<List<Sector>> resultObserver = new Observer<List<Sector>>() {
                @Override
                public void onChanged(List<Sector> sectorList) {
                    if ((sectorList != null)&&(!sectorList.isEmpty())) {
                        result.removeObserver(this); // this = observer...
                        runNextTaskLink(sectorList);
                    }
                }
            };
            result.observeForever(resultObserver);
            AppHandle.getHandle().getRepository().getLocalDataRepository().getAllSectors(result);
        }
    }

    private class SectorsMapCreator extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<Sector> sectorList = (List<Sector>)data[0];
            classScopeSectorMap = new HashMap<>();
            for (Sector sector : sectorList ) {
                classScopeSectorMap.put( sector.getName(), sector );
            }
            runNextTaskLink();
        }
    }

    private class DepartmentsGetter extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            MutableLiveData<List<Department>> result = new MutableLiveData<>();
            Observer<List<Department>> resultObserver = new Observer<List<Department>>() {
                @Override
                public void onChanged(List<Department> departmentList) {
                    if ((departmentList != null)&&(!departmentList.isEmpty())) {
                        result.removeObserver(this); // this = observer...
                        runNextTaskLink( departmentList );
                    }
                }
            };
            result.observeForever(resultObserver);
            AppHandle.getHandle().getRepository().getLocalDataRepository().getAllDepartments(result);
        }
    }

    private class DepartmentsMapCreator extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<Department> departmentList = (List<Department>)data[0];
            classScopeDepartmentMap = new HashMap<>();
            for (Department department : departmentList ) {
                classScopeDepartmentMap.put( department.getSymbol(), department );
            }
            runNextTaskLink();
        }
    }

    private class FamiliesGetter extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            MutableLiveData<List<Family>> result = new MutableLiveData<>();
            Observer<List<Family>> resultObserver = new Observer<List<Family>>() {
                @Override
                public void onChanged( List<Family> familiesList) {
                    if ((familiesList != null)&&(!familiesList.isEmpty())) {
                        result.removeObserver(this); // this = observer...
                        runNextTaskLink( familiesList );
                    }
                }
            };
            result.observeForever(resultObserver);
            AppHandle.getHandle().getRepository().getLocalDataRepository().getAllFamilies( result );
        }
    }

    private class DummyFamilyCreator extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<Family> familiesList = (List<Family>)data[0];
            classScopeDummyFamily = familiesList.get(0);
            runNextTaskLink();
        }
    }

    private class MarketsGetter extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            MutableLiveData<List<Market>> result = new MutableLiveData<>();
            Observer<List<Market>> resultObserver = new Observer<List<Market>>() {
                @Override
                public void onChanged( List<Market> marketsList) {
                    if ((marketsList != null)&&(!marketsList.isEmpty())) {
                        result.removeObserver(this); // this = observer...
                        runNextTaskLink( marketsList );
                    }
                }
            };
            result.observeForever(resultObserver);
            AppHandle.getHandle().getRepository().getLocalDataRepository().getAllMarkets( result );
        }
    }

    private class DummyMarketCreator extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<Market> marketsList = (List<Market>)data[0];
            classScopeDummyMarket = marketsList.get(0);
            runNextTaskLink();
        }
    }

    private class ModulesGetter extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            MutableLiveData<List<Module>> result = new MutableLiveData<>();
            Observer<List<Module>> resultObserver = new Observer<List<Module>>() {
                @Override
                public void onChanged( List<Module> modulesList ) {
                    if ((modulesList != null)&&(!modulesList.isEmpty())) {
                        result.removeObserver(this); // this = observer...
                        runNextTaskLink( modulesList );
                    }
                }
            };
            result.observeForever(resultObserver);
            AppHandle.getHandle().getRepository().getLocalDataRepository().getAllModules( result );
        }
    }

    private class DummyModuleCreator extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<Module> modulesList = (List<Module>)data[0];
            classScopeDummyModule = modulesList.get(0);
            runNextTaskLink();
        }
    }

    private class UOProjectGetter extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            MutableLiveData<List<UOProject>> result = new MutableLiveData<>();
            Observer<List<UOProject>> resultObserver = new Observer<List<UOProject>>() {
                @Override
                public void onChanged( List<UOProject> uoProjectsList ) {
                    if ((uoProjectsList != null)&&(!uoProjectsList.isEmpty())) {
                        result.removeObserver(this); // this = observer...
                        runNextTaskLink( uoProjectsList );
                    }
                }
            };
            result.observeForever(resultObserver);
            AppHandle.getHandle().getRepository().getLocalDataRepository().getAllUOProjects( result );

        }
    }

    private class DummyUOProjectCreator extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<UOProject> uoProjectsList = (List<UOProject>)data[0];
            classScopeDummyUOProject = uoProjectsList.get(0);
            runNextTaskLink();
        }
    }

    private class AllOwnArticleInfosGetter extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            MutableLiveData<List<OwnArticleInfo>> result = new MutableLiveData<>();
            Observer<List<OwnArticleInfo>> resultObserver = new Observer<List<OwnArticleInfo>>() {
                @Override
                public void onChanged(List<OwnArticleInfo> actualOwnArticleInfosList) {
                    if ((actualOwnArticleInfosList != null)) {
                        result.removeObserver(this); // this = observer...
                        runNextTaskLink( actualOwnArticleInfosList);
                    }
                }
            };
            result.observeForever(resultObserver);
            AppHandle.getHandle().getRepository().getLocalDataRepository().getAllOwnArticleInfos(result);
        }
    }

    private class NewOwnArticleInfosListMaker extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<OwnArticleInfo> actualOwnArticleInfosList = (List<OwnArticleInfo>)data[0];
            Remote2LocalConverter converter = new Remote2LocalConverter();
            OwnArticleInfo ownArticleInfo;
            ArrayList<OwnArticleInfo> newOwnArticleInfosList = new ArrayList<>();
            for (RemoteAnalysisRow remoteAnalysisRow : classScopeRemoteAnalysisRowsList) {
                ownArticleInfo = converter.createOwnArticleInfo(
                        remoteAnalysisRow,
                        classScopeArticleMap.get( remoteAnalysisRow.getArticleCode() ),
                        classScopeDepartmentMap.get( remoteAnalysisRow.getDepartment() ),
                        classScopeSectorMap.get( remoteAnalysisRow.getSector() ),
                        classScopeDummyFamily,
                        classScopeDummyModule,
                        classScopeDummyMarket,
                        classScopeDummyUOProject
                );
                newOwnArticleInfosList.add( ownArticleInfo );
            }
            runNextTaskLink( newOwnArticleInfosList, actualOwnArticleInfosList );
        }
    }

    private class NewOwnArticleInfosDuplicatesRemover extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<OwnArticleInfo> newOwnArticleInfosList = (List<OwnArticleInfo>)data[0];
            List<OwnArticleInfo> actualOwnArticleInfosList = (List<OwnArticleInfo>)data[1];
            if (!actualOwnArticleInfosList.isEmpty()) {
                HashMap<String,OwnArticleInfo> actualOwnArticleInfosMap = new HashMap<>(
                        actualOwnArticleInfosList
                                .stream()
                                .collect( Collectors.toMap( OwnArticleInfo::getOwnCode, ownArticleInfo->ownArticleInfo ) ) );
                newOwnArticleInfosList = newOwnArticleInfosList
                        .stream()
                        .filter( ownArticleInfo -> !actualOwnArticleInfosMap.containsKey( ownArticleInfo.getOwnCode() ) )
                        .collect( Collectors.toList() );
            }
            runNextTaskLink( newOwnArticleInfosList );
        }
    }

    private class NewOwnArticleInfosSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            ArrayList<OwnArticleInfo> newOwnArticleInfoList =  (ArrayList<OwnArticleInfo>)data[0];
            if (newOwnArticleInfoList.size()>0) {
                MutableLiveData<Long> insertResult = new MutableLiveData<>();
                Observer<Long> insertingResultObserver = new Observer<Long>() {
                    @Override
                    public void onChanged(Long lastAddedOwnArticleInfoId) {
                        insertResult.removeObserver(this); // this = observer...
                        if (lastAddedOwnArticleInfoId!=null) {
                            runNextTaskLink();
                        }
                    }
                };
                progressPresentingManager.resetProgressPresenter( newOwnArticleInfoList.size(), DONT_HIDE_WHEN_FINISHED );
                progressPresentingManager.showMessagePresenter( AppHandle.getHandle().getString(R.string.additional_info) );
                insertResult.observeForever(insertingResultObserver);
                LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                localDataRepository.insertOwnArticleInfos( newOwnArticleInfoList, progressPresentingManager.getProgressPresenterWrapper(), insertResult );
            } else {
                runNextTaskLink();
            }

        }
    }

    // Tutaj wywołanie AllOwnArticleInfosGetter

    private class OwnArticleInfoMapMaker extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<OwnArticleInfo> actualOwnArticleInfosList = (List<OwnArticleInfo>)data[0];
            classScopeOwnArticleInfoMap = new HashMap<>();
            for (OwnArticleInfo ownArticleInfo : actualOwnArticleInfosList) {
                classScopeOwnArticleInfoMap.put( ownArticleInfo.getArticleId(), ownArticleInfo );
            }
            runNextTaskLink();
        }
    }

    private class AnalysisArticlesListMaker extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            Remote2LocalConverter converter = new Remote2LocalConverter();
            AnalysisArticle analysisArticle;
            ArrayList<AnalysisArticle> analysisArticlesList = new ArrayList<>();
            Article article;
            OwnArticleInfo ownArticleInfo;
            for (RemoteAnalysisRow remoteAnalysisRow : classScopeRemoteAnalysisRowsList) {
                article = classScopeArticleMap.get( remoteAnalysisRow.getArticleCode() );
                ownArticleInfo = classScopeOwnArticleInfoMap.get( article.getId() );
                analysisArticle = converter.createAnalysisArticle(
                        remoteAnalysisRow,
                        analysis,
                        ownArticleInfo
                );
                analysisArticlesList.add( analysisArticle );
            }
            runNextTaskLink( analysisArticlesList );
        }
    }

    private class AnalysisArticlesSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            ArrayList<AnalysisArticle> analysisArticlesList = (ArrayList<AnalysisArticle>)data[0];
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            MutableLiveData<Long> insertResult = new MutableLiveData<>();
            Observer<Long> insertingResultObserver = new Observer<Long>() {
                @Override
                public void onChanged(Long lastInsertedId) {
                    insertResult.removeObserver( this ); // this = observer...
                    runNextTaskLink();
                }
            };
            progressPresentingManager.resetProgressPresenter(  analysisArticlesList.size(), HIDE_WHEN_FINISHED );
            progressPresentingManager.showMessagePresenter( AppHandle.getHandle().getString(R.string.strategic_articles_list) );
            insertResult.observeForever( insertingResultObserver );
            localDataRepository.insertAnalysisArticles( analysisArticlesList, progressPresentingManager.getProgressPresenterWrapper(), insertResult );
        }
    }


    private class AnalysisUpdater extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            MutableLiveData<Integer> updateResult = new MutableLiveData<Integer>();
            Observer<Integer> updatingResultObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer updatedCount) {
                    updateResult.removeObserver(this); // this = observer...
                    finalResult.postValue( true );
                }
            };
            updateResult.observeForever( updatingResultObserver );
            analysis.setDataDownloaded( true );
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            localDataRepository.updateAnalysis( analysis, updateResult );
        }
    }
}