package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.CompetitorPrice;
import com.dev4lazy.pricecollector.model.entities.EanCode;
import com.dev4lazy.pricecollector.model.entities.OwnArticleInfo;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;

import java.util.List;

@Dao
public interface AnalysisArticleDao extends _Dao<AnalysisArticle> {

    @Override
    @Query("SELECT COUNT(*) FROM analysis_articles")
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM analysis_articles")
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM analysis_articles")
    int deleteAll();

    @Override
    @Query("SELECT * FROM analysis_articles")
    List<AnalysisArticle> getAll();

    @Override
    @Query("SELECT * FROM analysis_articles")
    LiveData<List<AnalysisArticle>> getAllLiveData();

    @Override
    @Query("SELECT * from analysis_articles ORDER BY article_id ASC")
    DataSource.Factory<Integer, AnalysisArticle> getAllPaged();

    @Override
    @RawQuery(observedEntities = AnalysisArticle.class)
    List<AnalysisArticle> getViaQuery( SimpleSQLiteQuery query );

    @Override
    @RawQuery(observedEntities = AnalysisArticle.class)
    LiveData<List<AnalysisArticle>> getViaQueryLiveData( SimpleSQLiteQuery query );

    @Override
    @Query("SELECT * FROM analysis_articles WHERE id= :id")
    List<AnalysisArticle> findById( int id) ;

    @Override
    @Query("SELECT * FROM analysis_articles WHERE id= :id")
    LiveData<List<AnalysisArticle>> findByIdLiveData( int id) ;

    // TODO XXX dummy method?
    @Override
    @Query("SELECT * FROM analysis_articles WHERE id= :name")
    List<AnalysisArticle> findByName(String name);

    @RawQuery(
            observedEntities = {
                    AnalysisArticle.class,
                    Article.class,
                    CompetitorPrice.class,
                    OwnArticleInfo.class,
                    EanCode.class
            }
    )
    DataSource.Factory<Integer, AnalysisArticleJoin> getAnalysisArticlesJoinViaQueryPaged(SimpleSQLiteQuery query);

    /*
    @Query( "SELECT " +
                "aa1.id, " +
                "aa1.analysis_id, " +
                "aa1.article_id, " +
                "aa1.own_article_info_id, " +
                "aa1.article_store_price, " +
                "aa1.article_ref_price, " +
                "aa1.article_new_price, " +
                // TODO XXX "IFNULL (aa1.competitor_store_id, '-1'), " +
                "IFNULL (cp.competitor_store_id, '-1') competitor_store_id," +
                // TODO XXX "IFNULL (aa1.competitor_store_price_id, '-1'), " +
                "IFNULL (cp.id, '-1') competitor_store_price_id, " +
                "cp.competitor_store_price, " +
                // TODO XXX "IFNULL (aa1.reference_article_id, '-1'), " +
                "IFNULL (cp.reference_article_id, '-1'), " +
                "aa1.comments, " +
                "a1.name name, " +
                "own_articles_infos.ownCode, " +
                "ec1.value, " +
                "ec2.value referenceArticleEan, " +
                "a2.name referenceArticleName, " +
                "a2.description " +
            "FROM analysis_articles aa1 " +
            "INNER JOIN articles a1 ON a1.id = aa1.article_id  " +
            "INNER JOIN own_articles_infos ON own_articles_infos.article_id = aa1.article_id " +
            "INNER JOIN ean_codes ec1 ON ec1.article_id = aa1.article_id   " +
            // TODO XXX "LEFT OUTER JOIN competitors_prices cp ON (cp.id = aa1.competitor_store_price_id) " +
            "LEFT OUTER JOIN competitors_prices cp ON (cp.analysis_article_id = aa1.id) " +
            "LEFT OUTER JOIN articles a2 ON a2.id = aa1.reference_article_id " +
            "LEFT OUTER JOIN ean_codes ec2 ON ec2.article_id = aa1.reference_article_id "  +
            // TODO XXX "WHERE (aa1.analysis_id= :analysisId) AND (cp.competitor_store_id= :storeId)"
            "WHERE (aa1.analysis_id= :analysisId) AND ((cp.competitor_store_id=:storeId ) OR (cp.competitor_store_id IS NULL))" )
    DataSource.Factory<Integer, AnalysisArticleJoin>  getAllAnalysisArticlesJoin( int analysisId, int storeId );

    @Query( "SELECT " +
            "aa1.id, " +
            "aa1.analysis_id, " +
            "aa1.article_id, " +
            "aa1.own_article_info_id, " +
            "aa1.article_store_price, " +
            "aa1.article_ref_price, " +
            "aa1.article_new_price, " +
            // TODO XXX "IFNULL (aa1.competitor_store_id, '-1'), " +
            "IFNULL (cp.competitor_store_id, '-1') competitor_store_id," +
            // TODO XXX "IFNULL (aa1.competitor_store_price_id, '-1'), " +
            "IFNULL (cp.id, '-1') competitor_store_price_id, " +
            "cp.competitor_store_price, " +
            // TODO XXX "IFNULL (aa1.reference_article_id, '-1'), " +
            "IFNULL (cp.reference_article_id, '-1'), " +
            "aa1.comments, " +
            "a1.name name, " +
            "own_articles_infos.ownCode, " +
            "ec1.value, " +
            "ec2.value referenceArticleEan, " +
            "a2.name referenceArticleName, " +
            "a2.description " +
            "FROM analysis_articles aa1 " +
            "INNER JOIN articles a1 ON a1.id = aa1.article_id  " +
            "INNER JOIN own_articles_infos ON own_articles_infos.article_id = aa1.article_id " +
            "LEFT OUTER JOIN ean_codes ec1 ON ec1.article_id = aa1.article_id   " +
            // "TODO XXX LEFT OUTER JOIN competitors_prices cp ON (cp.id = aa1.competitor_store_price_id) " +
            "INNER JOIN competitors_prices cp ON (cp.analysis_article_id = aa1.id) " +
            "LEFT OUTER JOIN articles a2 ON a2.id = aa1.reference_article_id " +
            "LEFT OUTER JOIN ean_codes ec2 ON ec2.article_id = aa1.reference_article_id "  +
            // TODO XXX "WHERE (aa1.analysis_id= :analysisId) AND (cp.competitor_store_id= :storeId)"
            "WHERE (aa1.analysis_id= :analysisId) AND ((cp.competitor_store_id=:storeId ) OR (cp.competitor_store_id IS NULL))" +

            "UNION " +

            "SELECT " +
            "aa1.id, " +
            "aa1.analysis_id, " +
            "aa1.article_id, " +
            "aa1.own_article_info_id, " +
            "aa1.article_store_price, " +
            "aa1.article_ref_price, " +
            "aa1.article_new_price, " +
            // TODO XXX "IFNULL (aa1.competitor_store_id, '-1'), " +
            "IFNULL (cp.competitor_store_id, '-1') competitor_store_id," +
            // "IFNULL (aa1.competitor_store_price_id, '-1'), " +
            "IFNULL (cp.id, '-1') competitor_store_price_id, " +
            "cp.competitor_store_price, " +
            // TODO XXX "IFNULL (aa1.reference_article_id, '-1'), " +
            "IFNULL (cp.reference_article_id, '-1'), " +
            "aa1.comments, " +
            "a1.name name, " +
            "own_articles_infos.ownCode, " +
            "ec1.value, " +
            "ec2.value referenceArticleEan, " +
            "a2.name referenceArticleName, " +
            "a2.description " +
            "FROM analysis_articles aa1 " +
            "INNER JOIN articles a1 ON a1.id = aa1.article_id  " +
            "INNER JOIN own_articles_infos ON own_articles_infos.article_id = aa1.article_id " +
            "LEFT OUTER JOIN ean_codes ec1 ON ec1.article_id = aa1.article_id   " +
            // TODO XXX "LEFT OUTER JOIN competitors_prices cp ON (cp.id = aa1.competitor_store_price_id) " +
            "LEFT OUTER JOIN competitors_prices cp ON (cp.analysis_article_id = aa1.id) " +
            "LEFT OUTER JOIN articles a2 ON a2.id = aa1.reference_article_id " +
            "LEFT OUTER JOIN ean_codes ec2 ON ec2.article_id = aa1.reference_article_id "  +
            // TODO XXX "WHERE (aa1.analysis_id= :analysisId) AND (cp.competitor_store_id= :storeId)"
            "WHERE " +
                "(aa1.analysis_id= :analysisId) AND " +
                "aa1.id NOT IN (" +
                    "SELECT " +
                    "aa1.id " +
                    "FROM " +
                    "analysis_articles aa1 " +
                    "INNER JOIN " +
                    "competitors_prices cp ON (cp.analysis_article_id = aa1.id) " +
                    "WHERE " +
                    "cp.competitor_store_id=:storeId)"

    )
    DataSource.Factory<Integer, AnalysisArticleJoin>  getAllAnalysisArticlesJoin2( int analysisId, int storeId );
     */

    @Query( "SELECT " +
            "aa1.id, " +
            "aa1.analysis_id, " +
            "aa1.article_id, " +
            "aa1.own_article_info_id, " +
            "aa1.article_store_price, " +
            "aa1.article_ref_price, " +
            "aa1.article_new_price, " +
            // TODO XXX "IFNULL (aa1.competitor_store_id, '-1'), " +
            // TODO XXX "IFNULL (cp.competitor_store_id, '-1') competitor_store_id," +
            "cp.competitor_store_id, " +
            // TODO XXX "IFNULL (aa1.competitor_store_price_id, '-1'), " +
            // TODO XXX "IFNULL (cp.id, NULL) competitor_store_price_id, " +
            "cp.id competitor_store_price_id, " +
            "cp.competitor_store_price, " +
            // TODO XXX "IFNULL (aa1.reference_article_id, '-1'), " +
            // TODO XXX "IFNULL (cp.reference_article_id, '-1'), " +
            "cp.reference_article_id, " +
            "aa1.comments, " +
            "a1.name name, " +
            "own_articles_infos.ownCode, " +
            "ec1.value, " +
            "ec2.id referenceArticleEanCodeId, " +
            "a2.name referenceArticleName, " +
            "ec2.value referenceArticleEan, " +
            "a2.description " +
            "FROM analysis_articles aa1 " +
            "INNER JOIN articles a1 ON a1.id = aa1.article_id  " +
            "INNER JOIN own_articles_infos ON own_articles_infos.article_id = aa1.article_id " +
            "LEFT OUTER JOIN ean_codes ec1 ON ec1.article_id = aa1.article_id   " +
            // TODO XXX "LEFT OUTER JOIN competitors_prices cp ON (cp.id = aa1.competitor_store_price_id) " +
            "INNER JOIN competitors_prices cp ON (cp.analysis_article_id = aa1.id) " +
            // TODO XXX "LEFT OUTER JOIN articles a2 ON a2.id = aa1.reference_article_id " +
            "LEFT OUTER JOIN articles a2 ON a2.id = cp.reference_article_id " +
            // TODO XXX "LEFT OUTER JOIN ean_codes ec2 ON ec2.article_id = aa1.reference_article_id " /**/ +
            "LEFT OUTER JOIN ean_codes ec2 ON ec2.id = cp.reference_article_ean_id " /**/ +
            // TODO XXX "WHERE (aa1.analysis_id= :analysisId) AND (cp.competitor_store_id= :storeId)"
            "WHERE (aa1.analysis_id= :analysisId) AND (cp.competitor_store_id=:storeId )" +

            "UNION " +

            "SELECT " +
            "aa1.id, " +
            "aa1.analysis_id, " +
            "aa1.article_id, " +
            "aa1.own_article_info_id, " +
            "aa1.article_store_price, " +
            "aa1.article_ref_price, " +
            "aa1.article_new_price, " +
            // TODO XXX "IFNULL (aa1.competitor_store_id, '-1'), " +
            // TODO XXX "-1," +
            "NULL, " +
            // TODO XXX "IFNULL (aa1.competitor_store_price_id, '-1'), " +
            // TODO XXX "-1, " +
            "NULL, " +
            "NULL, " +
            // TODO XXX "IFNULL (aa1.reference_article_id, '-1'), " +
            // TODO XXX "-1, " +
            "NULL, " +
            "aa1.comments, " +
            "a1.name, " +
            "own_articles_infos.ownCode, " +
            "ec1.value, " +
            "NULL, " +
            "NULL, " +
            "NULL, " +
            "NULL " +
            "FROM analysis_articles aa1 " +
            "INNER JOIN articles a1 ON a1.id = aa1.article_id  " +
            "INNER JOIN own_articles_infos ON own_articles_infos.article_id = aa1.article_id " +
            "LEFT OUTER JOIN ean_codes ec1 ON ec1.article_id = aa1.article_id   " +
            // TODO XXX "LEFT OUTER JOIN competitors_prices cp ON (cp.id = aa1.competitor_store_price_id) " +
            "LEFT OUTER JOIN competitors_prices cp ON (cp.analysis_article_id = aa1.id) " +
            // TODO XXX "LEFT OUTER JOIN articles a2 ON a2.id = aa1.reference_article_id " +
            "LEFT OUTER JOIN articles a2 ON a2.id = cp.reference_article_id " +
            // TODO XXX "LEFT OUTER JOIN ean_codes ec2 ON ec2.article_id = aa1.reference_article_id " /**/ +
            "LEFT OUTER JOIN ean_codes ec2 ON ec2.id = cp.reference_article_ean_id " /**/ +
            // TODO XXX "WHERE (aa1.analysis_id= :analysisId) AND (cp.competitor_store_id= :storeId)"
            "WHERE " +
            "(aa1.analysis_id= :analysisId) AND " +
            "aa1.id NOT IN (" +
                "SELECT " +
                "aa1.id " +
                "FROM " +
                "analysis_articles aa1 " +
                "INNER JOIN " +
                "competitors_prices cp ON (cp.analysis_article_id = aa1.id) " +
                "WHERE " +
                "cp.competitor_store_id=:storeId" +
            ")"

    )
    DataSource.Factory<Integer, AnalysisArticleJoin>  getAllAnalysisArticlesJoin3( int analysisId, int storeId );
}
