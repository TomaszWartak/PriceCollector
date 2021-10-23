package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.AppHandle;
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
import com.dev4lazy.pricecollector.view.ProgressPresenter;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.CustomSql;
import com.healthmarketscience.sqlbuilder.SelectQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * AnalysisDataDownloader_2
 *
 * Służy do pobrania danych potrzebnych do badania cen z serwera zdalnego (serwera Aplikacji Badanie Cen).
 *
 */
public class AnalysisDataDownloader_2 {

    private Analysis analysis;
    private ProgressPresenter progressPresenter;
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

    public void downloadData( Analysis analysis, MutableLiveData<Boolean> finalResult, ProgressPresenter progressPresenter ) {
        this.analysis = analysis;
        this.progressPresenter = progressPresenter;
        this.finalResult = finalResult;
        new TaskChain()
                .addTaskLink(new RemoteAnalysisRowsGetter())
                .addTaskLink(new NewArticlesListMaker())
                .addTaskLink(new NewArticlesSaver())
                .addTaskLink(new AllArticlesGetter())
                .addTaskLink(new AllArticlesMapCreator())
                .addTaskLink(new RemoteEanCodesGetter())
                .addTaskLink(new NewEanCodesListMaker())
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
                .addTaskLink(new OwnArticleInfoListMaker())
                .addTaskLink(new NewOwnArticleInfosSaver())
                .addTaskLink(new AllOwnArticleInfosGetter())
                .addTaskLink(new OwnArticleInfoMapMaker())
                .addTaskLink(new AnalysisArticlesListMaker())
                .addTaskLink(new AnalysisArticlesSaver())
                // TODO XXX .suspendHere()
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
            Analysis analysis = (Analysis)data[0];
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
            ArrayList<Article> articlesList = converter.createArticlesList( classScopeRemoteAnalysisRowsList );
            runNextTaskLink( articlesList );
        }
    }

    private class NewArticlesSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            ArrayList<Article> articlesList = (ArrayList<Article>)data[0];
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            if (progressPresenter!=null) {
                progressPresenter.reset(articlesList.size());
            }
            // TODO XXX tutaj trzeba zmienić nie na grupowy zapis, tylko pojedynczy
            //  Czy na pewno? To chyba jeszcze nie tutaj. Wystarczy chybatylko result zrobić,
            //  żeby mozna było uruchomić następne zadanie
            localDataRepository.insertArticles( articlesList, progressPresenter );
            runNextTaskLink();
        }
    }

    private class AllArticlesGetter extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            MutableLiveData<List<Article>> result = new MutableLiveData<>();
            Observer<List<Article>> resultObserver = new Observer<List<Article>>() {
                @Override
                public void onChanged(List<Article> articleList) {
                    if ((articleList != null)&&(!articleList.isEmpty())) {
                        result.removeObserver(this); // this = observer...
                        runNextTaskLink(articleList);
                    }
                }
            };
            result.observeForever(resultObserver);
            AppHandle.getHandle().getRepository().getLocalDataRepository().getAllArticles(result);
        }
    }

    private class AllArticlesMapCreator extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<Article> articleList = (List<Article>)data[0];
            classScopeArticleMap = new HashMap<>();
            for (Article article : articleList ) {
                classScopeArticleMap.put( article.getRemote_id(), article );
            }
            runNextTaskLink(articleList);
        }
    }

    private class RemoteEanCodesGetter extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<Article> articleList = (List<Article>)data[0];
            MutableLiveData<List<RemoteEanCode>> result = new MutableLiveData<>();
            Observer<List<RemoteEanCode>> resultObserver = new Observer<List<RemoteEanCode>>() {
                @Override
                public void onChanged(List<RemoteEanCode> remoteEanCodesList) {
                    if ((remoteEanCodesList != null)&&(!remoteEanCodesList.isEmpty())) {
                        result.removeObserver(this); // this = observer...
                        runNextTaskLink( remoteEanCodesList, articleList );
                    }
                }
            };
            result.observeForever(resultObserver);
            AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllEanCodes(result);
        }
    }

    private class NewEanCodesListMaker extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<RemoteEanCode> remoteEanCodesList = (List<RemoteEanCode>)data[0];
            List<Article> articleList = (List<Article>)data[1];
            ArrayList<EanCode> eanCodeList = convertToEanCodes( remoteEanCodesList, articleList  );
            runNextTaskLink();
        }


        private ArrayList<EanCode> convertToEanCodes (
                List<RemoteEanCode> remoteEanCodesList,
                List<Article> articleList ) {
            // TODO ??? HashMap<Integer, RemoteEanCode> remoteEanCodesHashMap =
            // TODO ??? remoteEanCodesList.stream().collect( Collectors.toMap(RemoteEanCode::getArticleId, Function.identity() ));
            // TODO ??? remoteEanCodesList.stream().collect( Collectors.toMap( RemoteEanCode::getArticleId, b->b ));
            HashMap<Integer, RemoteEanCode> remoteEanCodesHashMap = new HashMap<>();
            for (RemoteEanCode remoteEanCode : remoteEanCodesList) {
                remoteEanCodesHashMap.put( remoteEanCode.getArticleId(), remoteEanCode );
            }
            HashMap<Integer, Article> articlesHashMap = new HashMap<>();
            for (Article article : articleList ) {
                articlesHashMap.put( article.getRemote_id(), article );
            }
            Remote2LocalConverter converter = new Remote2LocalConverter();
            return converter.createEanCodesList( remoteEanCodesHashMap, articlesHashMap );
        }

    }

    private class NewEanCodesSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            ArrayList<EanCode> eanCodeList = (ArrayList<EanCode>)data[0];
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            if (progressPresenter!=null) {
                progressPresenter.reset(eanCodeList.size());
            }
            localDataRepository.insertEanCodes( eanCodeList, progressPresenter );
            runNextTaskLink();
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
                runNextTaskLink();
            }
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

    private class OwnArticleInfoListMaker extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            Remote2LocalConverter converter = new Remote2LocalConverter();
            OwnArticleInfo ownArticleInfo;
            ArrayList<OwnArticleInfo> ownArticleInfoList = new ArrayList<>();
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
                ownArticleInfoList.add( ownArticleInfo );
            }
            runNextTaskLink(  ownArticleInfoList  );
        }
    }

    private class NewOwnArticleInfosSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            ArrayList<OwnArticleInfo> ownArticleInfoList =  (ArrayList<OwnArticleInfo>)data[0];
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
            if (progressPresenter!=null) {
                progressPresenter.reset(ownArticleInfoList.size());
            }
            insertResult.observeForever(insertingResultObserver);
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            localDataRepository.insertOwnArticleInfos( ownArticleInfoList, progressPresenter, insertResult );
        }
    }

    private class AllOwnArticleInfosGetter extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            MutableLiveData<List<OwnArticleInfo>> result = new MutableLiveData<>();
            Observer<List<OwnArticleInfo>> resultObserver = new Observer<List<OwnArticleInfo>>() {
                @Override
                public void onChanged(List<OwnArticleInfo> ownArticleInfosList) {
                    if ((ownArticleInfosList != null)&&(!ownArticleInfosList.isEmpty())) {
                        result.removeObserver(this); // this = observer...
                        runNextTaskLink( ownArticleInfosList );
                    }
                }
            };
            result.observeForever(resultObserver);
            AppHandle.getHandle().getRepository().getLocalDataRepository().getAllOwnArticleInfos(result);
        }
    }

    private class OwnArticleInfoMapMaker extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            List<OwnArticleInfo> ownArticleInfosList = (List<OwnArticleInfo>)data[0];
            classScopeOwnArticleInfoMap = new HashMap<>();
            for (OwnArticleInfo ownArticleInfo : ownArticleInfosList ) {
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
                    analysis.setDataDownloaded( true );
                    localDataRepository.updateAnalysis( analysis, null );
                    finalResult.postValue( true );
                }
            };
            if (progressPresenter!=null) {
                progressPresenter.reset( analysisArticlesList.size() );
            }
            insertResult.observeForever( insertingResultObserver );
            localDataRepository.insertAnalysisArticles( analysisArticlesList, progressPresenter, insertResult );
        }
    }

}