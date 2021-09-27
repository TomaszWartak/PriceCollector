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
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.CustomSql;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.UnionQuery;

import static com.healthmarketscience.sqlbuilder.SetOperationQuery.Type.UNION;

public class AnalysisArticleJoinsListViewModel extends AndroidViewModel {

    private /* TODO XXX final */ LiveData<PagedList<AnalysisArticleJoin>> analysisRowsLiveData;
    private SearchArticlesCriteria searchArticlesCriteria;

    public AnalysisArticleJoinsListViewModel(Application application) {
        super(application);
        searchArticlesCriteria = new SearchArticlesCriteria();
        // TODO XXX buildAnalysisiArticleJoinsPagedList();
    }

    public void buildAnalysisiArticleJoinsPagedList( int analysisId, int storeId ) {
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        DataSource.Factory<Integer, AnalysisArticleJoin> factory;
        if ( searchArticlesCriteria.isFilterSet() ) {
              SelectQuery analysisArticlesJoinWithPricesQuery = new SelectQuery()
                    .addCustomColumns(
                            new CustomSql( "aa1.id" ),
                            new CustomSql( "aa1.analysis_id" ),
                            new CustomSql( "aa1.article_id" ),
                            new CustomSql( "aa1.own_article_info_id" ),
                            new CustomSql( "aa1.article_store_price" ),
                            new CustomSql( "aa1.article_ref_price" ),
                            new CustomSql( "aa1.article_new_price" ),
                            // TODO XXX new CustomSql( "IFNULL (aa1.competitor_store_id, '-1'), " ),
                            // TODO XXX new CustomSql( "IFNULL (cp.competitor_store_id, '-1')" ),
                            new CustomSql( "cp.competitor_store_id" ),
                            // TODO XXX new CustomSql( "IFNULL (aa1.competitor_store_price_id, '-1'), " ),
                            // TODO XXX new CustomSql( "IFNULL (cp.id, '-1')" ),
                            new CustomSql( "cp.id competitor_store_price_id" ),
                            new CustomSql( "cp.competitor_store_price" ),
                            // TODO XXX new CustomSql( "IFNULL (aa1.reference_article_id, '-1'), " ),
                            // TODO XXX new CustomSql( "IFNULL (cp.reference_article_id, '-1') " ),
                            new CustomSql( "cp.reference_article_id" ),
                            new CustomSql( "aa1.comments" ),
                            new CustomSql( "a1.name name" ),
                            new CustomSql( "own_articles_infos.ownCode" ),
                            new CustomSql( "ec1.value" ),
                            new CustomSql( "own_articles_infos.sector_id" ),
                            new CustomSql( "own_articles_infos.department_id" ),
                            new CustomSql( "a2.name referenceArticleName" ),
                            new CustomSql( "ec2.id referenceArticleEanCodeId" ),
                            new CustomSql( "ec2.value referenceArticleEan" ),
                            new CustomSql( "a2.description" )
                    )
                    .addCustomFromTable("analysis_articles aa1")
                    .addCustomJoin(" INNER JOIN articles a1 ON (a1.id = aa1.article_id)")
                    .addCustomJoin(" INNER JOIN own_articles_infos ON (own_articles_infos.article_id = aa1.article_id)" )
                    .addCustomJoin(" LEFT OUTER JOIN ean_codes ec1 ON (ec1.article_id = aa1.article_id)" )
                    .addCustomJoin(" INNER JOIN competitors_prices cp ON (cp.analysis_article_id = aa1.id)" )
                    .addCustomJoin(" LEFT OUTER JOIN articles a2 ON (a2.id = cp.reference_article_id)" )
                    .addCustomJoin(" LEFT OUTER JOIN ean_codes ec2 ON (ec2.id = cp.reference_article_ean_id)" )
                    // TODO XXX "WHERE (aa1.analysis_id= :analysisId) AND ((cp.competitor_store_id=:storeId ) OR (cp.competitor_store_id IS NULL))
                    // TODO XXX .addCondition( BinaryCondition.equalTo( new CustomSql( "aa1.analysis_id" ), analysisId ) )
                    .addCondition( ComboCondition.and(
                            BinaryCondition.equalTo( new CustomSql( "aa1.analysis_id" ), analysisId ),
                            BinaryCondition.equalTo( new CustomSql( "cp.competitor_store_id" ), storeId ) )
                    );
            analysisArticlesJoinWithPricesQuery = addSearchCriteriaToQuery( analysisArticlesJoinWithPricesQuery );
            analysisArticlesJoinWithPricesQuery = analysisArticlesJoinWithPricesQuery.validate();
            // TODO XXX String queryStringTest = analysisArticlesJoinWithPricesQuery.toString();

            SelectQuery analysisArticlesJoinRestQuery = new SelectQuery()
                    .addCustomColumns(
                            new CustomSql( "aa1.id" ),
                            new CustomSql( "aa1.analysis_id" ),
                            new CustomSql( "aa1.article_id" ),
                            new CustomSql( "aa1.own_article_info_id" ),
                            new CustomSql( "aa1.article_store_price" ),
                            new CustomSql( "aa1.article_ref_price" ),
                            new CustomSql( "aa1.article_new_price" ),
                            // TODO XXX new CustomSql( "-1" ),
                            new CustomSql( "NULL " ),
                            // TODO XXX new CustomSql( "-1" ),
                            new CustomSql( "NULL " ),
                            new CustomSql( "NULL" ),
                            // TODO XXX new CustomSql( "-1" ),
                            new CustomSql( "NULL " ),
                            new CustomSql( "aa1.comments" ),
                            new CustomSql( "a1.name" ),
                            new CustomSql( "own_articles_infos.ownCode" ),
                            new CustomSql( "ec1.value" ),
                            new CustomSql( "own_articles_infos.sector_id" ),
                            new CustomSql( "own_articles_infos.department_id" ),
                            new CustomSql( "NULL " ),
                            new CustomSql( "NULL " ),
                            new CustomSql( "NULL " ),
                            new CustomSql( "NULL" )
                    )
                    .addCustomFromTable("analysis_articles aa1")
                    .addCustomJoin(" INNER JOIN articles a1 ON (a1.id = aa1.article_id)")
                    .addCustomJoin(" INNER JOIN own_articles_infos ON (own_articles_infos.article_id = aa1.article_id)" )
                    .addCustomJoin(" LEFT OUTER JOIN ean_codes ec1 ON (ec1.article_id = aa1.article_id)" )
                    .addCustomJoin(" LEFT OUTER JOIN competitors_prices cp ON (cp.analysis_article_id = aa1.id)" )
                    .addCustomJoin(" LEFT OUTER JOIN articles a2 ON (a2.id = cp.reference_article_id)" )
                    .addCustomJoin(" LEFT OUTER JOIN ean_codes ec2 ON (ec2.id = cp.reference_article_ean_id)" )
                    .addCondition( BinaryCondition.equalTo( new CustomSql( "aa1.analysis_id" ), analysisId ) );

            analysisArticlesJoinRestQuery = addSearchCriteriaToQuery( analysisArticlesJoinRestQuery );
            analysisArticlesJoinRestQuery = analysisArticlesJoinRestQuery.validate();

            SelectQuery analysisArticlesWithoutPricesInnerQuery = new SelectQuery()
                    .addCustomColumns(
                            new CustomSql( "aa1.id" )
                    )
                    .addCustomFromTable("analysis_articles aa1")
                    .addCustomJoin(" INNER JOIN competitors_prices cp ON (cp.analysis_article_id = aa1.id)" )
                    .addCondition( BinaryCondition.equalTo( new CustomSql( "cp.competitor_store_id" ), storeId ) );
            analysisArticlesWithoutPricesInnerQuery = analysisArticlesWithoutPricesInnerQuery.validate();
            // TODO XXX queryStringTest = AnalysisArticlesWithoutPricesInnerQuery.toString();

            analysisArticlesJoinRestQuery.addCondition(
                    new InCondition(
                            new CustomSql( "aa1.id" ),
                            analysisArticlesWithoutPricesInnerQuery
                    ).setNegate( true )
            );
            analysisArticlesJoinRestQuery = analysisArticlesJoinRestQuery.validate();

            // TODO XXX queryStringTest = analysisArticlesJoinRestQuery.toString();

            UnionQuery unionQuery = new UnionQuery(UNION, analysisArticlesJoinWithPricesQuery, analysisArticlesJoinRestQuery );
            // TODO XXX unionQuery = addSearchCriteriaToQuery( unionQuery );
            unionQuery = unionQuery.validate();
            String unionQueryString = unionQuery.toString();

            factory = localDataRepository.getAnalysisArticlesJoinViaQueryPaged( new SimpleSQLiteQuery( unionQueryString ) );
        } else {
            factory = localDataRepository.getAllAnalysisArticlesJoin( analysisId, storeId );
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