package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.model.logic.LocalDataRepository;
import com.dev4lazy.pricecollector.model.logic.SearchArticlesCriteria;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.CustomSql;
import com.healthmarketscience.sqlbuilder.SelectQuery;

public class AnalysisArticleJoinsListViewModel extends AndroidViewModel {

    private /* TODO XXX final */ LiveData<PagedList<AnalysisArticleJoin>> analysisRowsLiveData;
    private SearchArticlesCriteria searchArticlesCriteria;

    public AnalysisArticleJoinsListViewModel(Application application) {
        super(application);
        searchArticlesCriteria = new SearchArticlesCriteria();
        // TODO XXX buildAnalysisiArticleJoinsPagedList();
    }

    public void buildAnalysisiArticleJoinsPagedList( int analysisId ) {
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        DataSource.Factory<Integer, AnalysisArticleJoin> factory;
        if ( searchArticlesCriteria.isFilterSet() ) {
              SelectQuery selectQuery = new SelectQuery()
                    .addCustomColumns(
                            new CustomSql( "aa1.id" ),
                            new CustomSql( "aa1.analysis_id" ),
                            new CustomSql( "aa1.article_id" ),
                            new CustomSql( "aa1.competitor_store_id" ),
                            new CustomSql( "aa1.article_store_price" ),
                            new CustomSql( "aa1.article_ref_price" ),
                            new CustomSql( "aa1.article_new_price" ),
                            new CustomSql( "a1.name name" ),
                            new CustomSql( "own_articles_infos.ownCode" ),
                            new CustomSql( "own_articles_infos.sector_id" ),
                            new CustomSql( "own_articles_infos.department_id" ),
                            new CustomSql( "ec1.value" ),
                            new CustomSql( "aa1.reference_article_id" ),
                            new CustomSql( "ec2.value referenceArticleEan" ),
                            new CustomSql( "a2.name referenceArticleName" )
                    )
                    .addCustomFromTable("analysis_articles aa1")
                    .addCustomJoin(" INNER JOIN articles a1 ON (a1.id = aa1.article_id)")
                    .addCustomJoin(" INNER JOIN own_articles_infos ON (own_articles_infos.article_id = aa1.article_id)" )
                    .addCustomJoin(" INNER JOIN ean_codes ec1 ON (ec1.article_id = aa1.article_id)" )
                    .addCustomJoin(" LEFT OUTER JOIN articles a2 ON (a2.id = aa1.reference_article_id)" )
                    .addCustomJoin(" LEFT OUTER JOIN ean_codes ec2 ON (ec2.article_id = aa1.reference_article_id)" )
                    .addCondition( BinaryCondition.equalTo( new CustomSql( "aa1.analysis_id" ), analysisId ) );
            selectQuery = addSearchCriteriaToQuery( selectQuery );
            String queryString = selectQuery.validate().toString();
            factory = localDataRepository.getAnalysisArticlesJoinViaQueryPaged( new SimpleSQLiteQuery( queryString ) );
        } else {
            factory = localDataRepository.getAllAnalysisArticlesJoin( analysisId );
        }
        LivePagedListBuilder<Integer, AnalysisArticleJoin> pagedListBuilder = new LivePagedListBuilder<Integer, AnalysisArticleJoin>(factory, 50);
        analysisRowsLiveData = pagedListBuilder.build();
    }

    private SelectQuery addSearchCriteriaToQuery( SelectQuery query ) {
        if (searchArticlesCriteria.isFilterSet()) {
            if (searchArticlesCriteria.isArticleNameSet() ) {
                query.addCondition(BinaryCondition.like(
                        new CustomSql( "a1.name" ), "%"+searchArticlesCriteria.getArticleName()+"%" )
                );
            }
            if (searchArticlesCriteria.isArticleEANSet() ) {
                query.addCondition(BinaryCondition.like(
                        new CustomSql( "ec1.value" ), "%"+searchArticlesCriteria.getArticleEAN()+"%" )
                );
            }
            if (searchArticlesCriteria.isArticleSKUSet() ) {
                query.addCondition(BinaryCondition.like(
                        new CustomSql( "own_articles_infos.ownCode" ), "%"+searchArticlesCriteria.getArticleSKU()+"%" )
                );
            }
            if (searchArticlesCriteria.isArticleAnyTextSet() ) {
                // query.addCondition(BinaryCondition.like(new CustomSql( "ec1.value" ), "%"+searchArticlesCriteria.getArticleAnyText()+"%"));
            }
            if (searchArticlesCriteria.isArticleSectorIdSet() ) {
                query.addCondition(BinaryCondition.equalTo(
                        new CustomSql( "own_articles_infos.sector_id" ), searchArticlesCriteria.getArticleSectorId() )
                );
            }
            if (searchArticlesCriteria.isArticleDepartamentIdSet() ) {
                query.addCondition(BinaryCondition.equalTo(
                        new CustomSql( "own_articles_infos.department_id" ), searchArticlesCriteria.getArticleDepartmentId() )
                );
            }
        }
        return query;
    }

    public LiveData<PagedList<AnalysisArticleJoin>> getAnalysisArticleJoinsListLiveData() {
        return analysisRowsLiveData;
    }

    public SearchArticlesCriteria getSearchArticlesCriteria() {
        return searchArticlesCriteria;
    }

}