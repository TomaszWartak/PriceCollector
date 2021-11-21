package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoinForPricesUpload;
import com.dev4lazy.pricecollector.model.utils.AppDataFeeder;
import com.dev4lazy.pricecollector.model.utils.DataFeeder;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.utils.TaskChain;
import com.dev4lazy.pricecollector.utils.TaskLink;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.CustomSql;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UnaryCondition;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import static com.dev4lazy.pricecollector.model.utils.DataFeeder.CASTORAMA_INDEX;
import static com.dev4lazy.pricecollector.model.utils.DataFeeder.LEROY_MERLIN_INDEX;
import static com.dev4lazy.pricecollector.model.utils.DataFeeder.LOCAL_COMPETITOR_INDEX;
import static com.dev4lazy.pricecollector.model.utils.DataFeeder.OBI_INDEX;

/**
 * AnalysisDataUploader
 *
 * Służy do wysłania wyników badania cen na serwer zdalny (serwer Aplikacji Badanie Cen).
 *
 */
public class AnalysisDataUploader {

    private Analysis analysisToUpload;
    // TODO XXX private int analysisId;
    // TODO XXX private int remoteAnalysisId;

    public AnalysisDataUploader( Analysis analysis ) {
        this.analysisToUpload = analysis;
        // TODO XXX this.analysisId = analysisToUpload.getId();
    }

    public void uploadData() {
        new TaskChain()
            .addTaskLink( new AnalysisArticleJoinGetter( ) )
            .addTaskLink( new PricesMapMaker( ) )
            .addTaskLink( new RemoteAnalysisRowsCollector( ) )
            .suspendHere()
            .addTaskLink( new RemoteAnalysisRowsDataUpdater() )
            .addTaskLink( new RemoteAnalysisRowsSaver() )
            .suspendHere()
            .addTaskLink( new AnalysisLastDataSentDateUpdater() )
            .startIt();
    }

    class AnalysisArticleJoinGetter extends TaskLink {

        @Override
        protected void doIt(Object... data) {
            getAnalysisArticlesJoinsForPricesUpload();
        }

        private void getAnalysisArticlesJoinsForPricesUpload() {
            MutableLiveData<List<AnalysisArticleJoinForPricesUpload>> result = new MutableLiveData<>();
            Observer<List<AnalysisArticleJoinForPricesUpload>> resultObserver = new Observer<List<AnalysisArticleJoinForPricesUpload>>() {
                @Override
                public void onChanged(List<AnalysisArticleJoinForPricesUpload> analysisArticleJoinForPricesUploadList) {
                    result.removeObserver(this); // this = Observer :-)
                    if (!analysisArticleJoinForPricesUploadList.isEmpty()) {
                        // TODO XXX remoteAnalysisId = analysisArticleJoinForPricesUploadList.get(0).getRemote_analysis_id();
                        runNextTaskLink( analysisArticleJoinForPricesUploadList );
                    }
                }
            };
            result.observeForever( resultObserver );
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            String analysisArticlesJoinWithPricesQueryString = getQuery();
            localDataRepository.getAnalysisArticleJoinsForPricesUploadViaStringQuery( analysisArticlesJoinWithPricesQueryString, result );
        }

        private String getQuery( ) {
            SelectQuery analysisArticlesJoinWithPricesQuery = new SelectQuery()
                    .addCustomColumns(
                            new CustomSql( "analyzes.remote_id remote_analysis_id" ),
                            new CustomSql( "own_articles_infos.ownCode article_ownCode" ),
                            new CustomSql( "companies.name competitor_company_name" ),
                            new CustomSql( "cp.competitor_store_price" )
                    )
                    .addCustomFromTable("analysis_articles aa1")
                    .addCustomJoin(" INNER JOIN analyzes ON (analyzes.id = aa1.analysis_id)" )
                    .addCustomJoin(" INNER JOIN own_articles_infos ON (own_articles_infos.article_id = aa1.article_id)" )
                    .addCustomJoin(" INNER JOIN competitors_prices cp ON (cp.analysis_article_id = aa1.id)" )
                    .addCustomJoin(" INNER JOIN stores ON (stores.id = cp.competitor_store_id) ")
                    .addCustomJoin(" INNER JOIN companies ON (companies.id = stores.company_id) ")
                    .addCondition(
                        ComboCondition.and(
                            BinaryCondition.equalTo( new CustomSql( "aa1.analysis_id" ), analysisToUpload.getId() ),
                            UnaryCondition.isNotNull( new CustomSql( "cp.competitor_store_price" ) )
                        )
                    );
            analysisArticlesJoinWithPricesQuery = analysisArticlesJoinWithPricesQuery.validate();
            return analysisArticlesJoinWithPricesQuery.toString();
        }

        /* TODO XXX
        @Override
        protected void takeData(Object... data) {
        }

         */

    }

    class PricesMapMaker extends TaskLink {

        private ArrayList<AnalysisArticleJoinForPricesUpload> analysisArticleJoinForPricesUploadList;

        @Override
        protected void doIt(Object... data) {
            analysisArticleJoinForPricesUploadList = (ArrayList<AnalysisArticleJoinForPricesUpload>)data[0];
            makePricesMap();
        }

        private void makePricesMap() {
            HashMap<String, ArticlePricesNode> pricesMap = new HashMap<>();
            // TODO XXX pricesMap = analysisArticleJoinList.stream().collect(Collectors.toMap(v -> (String) v.getArticle_ownCode("rollno"), e -> e));
            for (AnalysisArticleJoinForPricesUpload analysisArticleJoinForPricesUpload : analysisArticleJoinForPricesUploadList) {
                if (isPriceNotEmpty(analysisArticleJoinForPricesUpload)) {
                    String ownCode = analysisArticleJoinForPricesUpload.getArticle_ownCode();
                    if (pricesMap.containsKey( ownCode )) {
                        ArticlePricesNode articlePricesNode = pricesMap.get( ownCode );
                        articlePricesNode.addArticlePrice( analysisArticleJoinForPricesUpload );
                    } else {
                        pricesMap.put( ownCode, new ArticlePricesNode( analysisArticleJoinForPricesUpload) );
                    }
                }
            }
            runNextTaskLink( pricesMap );
        }

        private boolean isPriceNotEmpty( AnalysisArticleJoinForPricesUpload analysisArticleJoinForPricesUpload ) {
            Double competitorStorePrice = analysisArticleJoinForPricesUpload.getCompetitor_store_price();
            return (
                (competitorStorePrice!=null) &&
                (competitorStorePrice!=0.0)
            );
        }

      /* TODO XXX
        @Override
        protected void takeData(Object... data) {
            analysisArticleJoinForPricesUploadList = (ArrayList<AnalysisArticleJoinForPricesUpload>)data[0];
        }

         */
    }

    class ArticlePricesNode {

        private ArrayList<AnalysisArticleJoinForPricesUpload> articlePrices;

        public ArticlePricesNode( AnalysisArticleJoinForPricesUpload analysisArticleJoinForPricesUpload ) {
            articlePrices = new ArrayList<>();
            addArticlePrice( analysisArticleJoinForPricesUpload );
        }

        public ArrayList<AnalysisArticleJoinForPricesUpload> getArticlePrices() {
            return articlePrices;
        }

        protected void addArticlePrice(AnalysisArticleJoinForPricesUpload analysisArticleJoinForPricesUpload ){
            articlePrices.add( analysisArticleJoinForPricesUpload );
        }

    }

    class RemoteAnalysisRowsCollector extends TaskLink {

        private HashMap<String, ArticlePricesNode> pricesMap;
        private HashMap<String, RemoteAnalysisRow> remoteAnalysisRowsMap;

        @Override
        protected void doIt(Object... data) {
            pricesMap = (HashMap<String, ArticlePricesNode>) data[0];
            collectRemoteAnalysisRows();
        }

        private void collectRemoteAnalysisRows() {
            /*
            - pobieram klucz (remote_id + ownName) z mapy
            - tworzę obiekt odczytu RAR RemoteAnalysisRowGetter, przekazuję klucz i mówię mu - pobierz (chyba czekam,
              aż wszystkie zostaną utworzone?)
            - po pobraniu wkładam RAR do listy i sprawdzam, czy wszystkie zostały pobrane
            - jeśli wszystkie, lub minął czas (jeśli nie wszystkie, to raport zwrotny... LOL),
              to dobrze byłoby zapisać wszyztkie -> kolejna lista obiektów do zapisania
            - remoteDataRepository.insertAnalysisRow(remoteAnalysisRow);
             */
            remoteAnalysisRowsMap = new HashMap<>();
            TaskChain remoteAnalysisRowGetterTaskChain = new TaskChain();
            for (String articleCode : pricesMap.keySet() ) {
                remoteAnalysisRowGetterTaskChain.addTaskLink( new RemoteAnalysisRowGetter( articleCode ) );
            }
            TaskChain analysisDataUploaderTaskChain = getTaskChain();
            remoteAnalysisRowGetterTaskChain.addTaskLink(
                new TaskLink() {
                  @Override
                  protected void doIt(Object... data) {
                      analysisDataUploaderTaskChain.resume( pricesMap, remoteAnalysisRowsMap );
                  }
              }
            );
            remoteAnalysisRowGetterTaskChain.startIt( remoteAnalysisRowsMap );
        }

        /* TODO XXX
        @Override
        protected void takeData(Object... data) {
            pricesMap = (HashMap<String, ArticlePricesNode>) data[0];
        }

         */

        class RemoteAnalysisRowGetter extends TaskLink {

            private String articleCode;
            private HashMap<String, RemoteAnalysisRow> remoteAnalysisRowsMap;

            public RemoteAnalysisRowGetter(String articleCode) {
                this.articleCode = articleCode;
            }

            @Override
            protected void doIt(Object... data) {
                remoteAnalysisRowsMap = (HashMap<String, RemoteAnalysisRow>) data[0];
                MutableLiveData<List<RemoteAnalysisRow>> result = new MutableLiveData<>();
                Observer<List<RemoteAnalysisRow>> resultObserver = new Observer<List<RemoteAnalysisRow>>() {
                    @Override
                    public void onChanged(List<RemoteAnalysisRow> remoteAnalysisRowsList) {
                        result.removeObserver(this); // this = Observer :-)
                        if (!remoteAnalysisRowsList.isEmpty()) {
                            remoteAnalysisRowsMap.put( articleCode, remoteAnalysisRowsList.get(0) );
                            runNextTaskLink( remoteAnalysisRowsMap );
                        }
                    }
                };
                result.observeForever( resultObserver );
                RemoteDataRepository remoteDataRepository = AppHandle.getHandle().getRepository().getRemoteDataRepository();
                String remoteAnalysisRowQueryString = getQuery();
                remoteDataRepository.getRemoteAnalysisRowViaQuery( remoteAnalysisRowQueryString, result );
            }

            private String getQuery( ) {
                SelectQuery analysisArticlesJoinWithPricesQuery = new SelectQuery()
                        .addAllColumns()
                        .addCustomFromTable("analysis_rows")
                        .addCondition(
                                ComboCondition.and(
                                        BinaryCondition.equalTo( new CustomSql( "analysis_rows.articleCode" ), articleCode ),
                                        BinaryCondition.equalTo( new CustomSql( "analysis_rows.analysisId" ), analysisToUpload.getRemote_id() /* remoteAnalysisId*/ )
                                )
                        );
                analysisArticlesJoinWithPricesQuery = analysisArticlesJoinWithPricesQuery.validate();
                return analysisArticlesJoinWithPricesQuery.toString();
            }

        /* TODO XXX
        @Override
        protected void takeData(Object... data) {
            remoteAnalysisRowsMap = (HashMap<String, RemoteAnalysisRow>) data[0];
        }

         */

        }


    }

    class RemoteAnalysisRowsDataUpdater extends TaskLink {

        private HashMap<String, ArticlePricesNode> pricesMap;
        private HashMap<String, RemoteAnalysisRow> remoteAnalysisRowsMap;

        @Override
        protected void doIt(Object... data) {
            pricesMap = (HashMap<String, ArticlePricesNode>)data[0];
            remoteAnalysisRowsMap = (HashMap<String, RemoteAnalysisRow>)data[1];
            for (String articleCode : pricesMap.keySet() ) {
                ArticlePricesNode articlePricesNode = pricesMap.get( articleCode );
                RemoteAnalysisRow remoteAnalysisRow = remoteAnalysisRowsMap.get( articleCode );
                DataFeeder dataFeeder = AppDataFeeder.getInstance();
                boolean localCopetitorWasAlready = false;
                for (AnalysisArticleJoinForPricesUpload analysisArticleJoinForPricesUpload : articlePricesNode.getArticlePrices() ) {
                    if (analysisArticleJoinForPricesUpload.getCompetitor_company_name().equals(dataFeeder.COMPANIES_NAMES[LEROY_MERLIN_INDEX])) {
                        remoteAnalysisRow.setArticleLmPrice( analysisArticleJoinForPricesUpload.getCompetitor_store_price() );
                    } else if (analysisArticleJoinForPricesUpload.getCompetitor_company_name().equals(dataFeeder.COMPANIES_NAMES[OBI_INDEX])) {
                        remoteAnalysisRow.setArticleObiPrice( analysisArticleJoinForPricesUpload.getCompetitor_store_price() );
                    } else if (analysisArticleJoinForPricesUpload.getCompetitor_company_name().equals(dataFeeder.COMPANIES_NAMES[CASTORAMA_INDEX])) {
                        remoteAnalysisRow.setArticleCastoramaPrice( analysisArticleJoinForPricesUpload.getCompetitor_store_price() );
                    } else if (analysisArticleJoinForPricesUpload.getCompetitor_company_name().equals(dataFeeder.COMPANIES_NAMES[LOCAL_COMPETITOR_INDEX])) {
                        if (localCopetitorWasAlready) {
                            remoteAnalysisRow.setArticleLocalCompetitor2Price(analysisArticleJoinForPricesUpload.getCompetitor_store_price());
                        } else {
                            remoteAnalysisRow.setArticleLocalCompetitor1Price(analysisArticleJoinForPricesUpload.getCompetitor_store_price());
                            localCopetitorWasAlready = true;
                        }
                    }
                }
            }
            runNextTaskLink( remoteAnalysisRowsMap );
        }

    }

    class RemoteAnalysisRowsSaver extends TaskLink {

        @Override
        protected void doIt(Object... data) {
            HashMap<String, RemoteAnalysisRow> remoteAnalysisRowsMap = (HashMap<String, RemoteAnalysisRow>)data[0];
            TaskChain remoteAnalysisRowsSaverTaskChain = new TaskChain();
            for (RemoteAnalysisRow remoteAnalysisRow : remoteAnalysisRowsMap.values() ) {
                remoteAnalysisRowsSaverTaskChain.addTaskLink( new RemoteAnalysisRowSaver( remoteAnalysisRow ) );
            }
            TaskChain analysisDataUploaderTaskChain = getTaskChain();
            remoteAnalysisRowsSaverTaskChain.addTaskLink(
                    new TaskLink() {
                        @Override
                        protected void doIt(Object... data) {
                            analysisDataUploaderTaskChain.resume( );
                        }
                    }
            );
            remoteAnalysisRowsSaverTaskChain.startIt();
        }

    }

    class RemoteAnalysisRowSaver extends TaskLink {

        private RemoteAnalysisRow remoteAnalysisRow = null;

        public RemoteAnalysisRowSaver( RemoteAnalysisRow remoteAnalysisRow) {
            this.remoteAnalysisRow = remoteAnalysisRow;
        }

        @Override
        protected void doIt(Object... data) {
            MutableLiveData<Integer> updateResult = new MutableLiveData<>();
            Observer<Integer> updatingResultObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer updatedCount) {
                    updateResult.removeObserver(this); // this = Observer :-)
                    runNextTaskLink( );
                }
            };
            updateResult.observeForever(updatingResultObserver);
            RemoteDataRepository remoteDataRepository = AppHandle.getHandle().getRepository().getRemoteDataRepository();
            remoteDataRepository.updateRemoteAnalysisRow( remoteAnalysisRow, updateResult );
        }

    }

    class AnalysisLastDataSentDateUpdater extends TaskLink {

        @Override
        protected void doIt(Object... data) {
            analysisToUpload.setFinishDate( new Date() );
            MutableLiveData<Integer> updateResult = new MutableLiveData<>();
            Observer<Integer> updatingResultObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer updatedCount) {
                    updateResult.removeObserver(this); // this = Observer :-)
                    runNextTaskLink( );
                }
            };
            updateResult.observeForever(updatingResultObserver);
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            localDataRepository.updateAnalysis( analysisToUpload, updateResult );        }
    }

}

