package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;
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

    // dummy method?
    @Override
    @Query("SELECT * FROM analysis_articles WHERE id= :name")
    List<AnalysisArticle> findByName(String name);

// TODO !!!! najpierw skopiuj remote RemoteAnalysisRow do local analysis_article

    @Query(
            /*
            "SELECT " +
                    "aa1.id, " +
                    "aa1.analysis_id, " +
                    "aa1.article_id, " +
                    "aa1.competitor_store_id, " +
                    "aa1.competitor_store_price, " +
                    "aa1.article_store_price, " +
                    "aa1.article_ref_price, " +
                    "aa1.article_new_price, " +
                    "aa2.reference_article_id, " +
                    "a1.name, " +
                    "a2.name, " +
                    "own_articles_infos.ownCode, " +
                    "ec1.value, " +
                    "ec2.value " +
            "FROM " +
                    "analysis_articles aa1, analysis_articles aa2 " +
            "INNER JOIN " +
                    "articles a1 ON a1.id = aa1.article_id "+
            "INNER JOIN " +
                    "own_articles_infos ON own_articles_infos.article_id = aa1.article_id " +
            "INNER JOIN " +
                    "ean_codes ec1 ON ec1.article_id = aa1.article_id "  +
            "INNER JOIN " +
                    "articles a2 ON a2.id = aa2.reference_article_id " +
            "INNER JOIN " +
                    "ean_codes ec2 ON ec2.article_id = aa2.reference_article_id "
             */
            "SELECT " +
                    "aa1.id, " +
                    "aa1.analysis_id, " +
                    "aa1.article_id, " +
                    "aa1.competitor_store_id, " +
                    "aa1.article_store_price, " +
                    "aa1.article_ref_price, " +
                    "aa1.article_new_price, " +
                    "a1.name name, " +
                    "own_articles_infos.ownCode, " +
                    "ec1.value, " +
                    "aa1.reference_article_id, " +
                    "ec2.value referenceArticleEan, " +
                    "a2.name referenceArticleName " +
            "FROM " +
                    "analysis_articles aa1 " +
            "INNER JOIN " +
            "       articles a1 ON a1.id = aa1.article_id  " +
            "INNER JOIN " +
                    "own_articles_infos ON own_articles_infos.article_id = aa1.article_id " +
            "INNER JOIN " +
                   "ean_codes ec1 ON ec1.article_id = aa1.article_id   " +
            "LEFT OUTER JOIN " +
                    "articles a2 ON a2.id = aa1.reference_article_id " +
            "LEFT OUTER JOIN " +
                    "ean_codes ec2 ON ec2.article_id = aa1.reference_article_id"
    )
    DataSource.Factory<Integer, AnalysisArticleJoin>  getAllAnalysisArticlesJoin();

    /*
    
    "SELECT " +
                    "aa1.id, " +
                    "aa1.analysis_id, " +
                    "aa1.article_id, " +
                    "aa1.competitor_store_id, " +
                    "aa1.article_store_price, " +
                    "aa1.article_ref_price, " +
                    "aa1.article_new_price, " +
                    "a1.name, " +
                    "own_articles_infos.ownCode, " +
                    "ec1.value ean, " +
                    "aa1.reference_article_id, " +
					"ec2.value ref_ean, " +
				    "a2.name ref_name " +
 "FROM " +
                    analysis_articles aa1 " +
            "INNER JOIN " +
                    "articles a1 ON a1.id = aa1.article_id  " +
            "INNER JOIN " +
                    "own_articles_infos ON own_articles_infos.article_id = aa1.article_id " +
            "INNER JOIN " +
                    ean_codes ec1 ON ec1.article_id = aa1.article_id   " +
            "LEFT OUTER JOIN " +
                    articles a2 ON a2.id = aa1.reference_article_id " +
            "LEFT OUTER JOIN " +
                   "ean_codes ec2 ON ec2.article_id = aa1.reference_article_id"
    
    
    SELECT
                     aa1.id,
                     aa1.analysis_id,
                     aa1.article_id,
                     aa1.competitor_store_id,
                     aa1.competitor_store_price,
                     aa1.article_store_price,
                     aa1.article_ref_price,
                     aa1.article_new_price,
                     a1.name,
                     own_articles_infos.ownCode,
                     ec1.value
             FROM
                     analysis_articles aa1
			 INNER JOIN
                     articles a1 ON a1.id = aa1.article_id
             INNER JOIN
                     own_articles_infos ON own_articles_infos.article_id = aa1.article_id
             INNER JOIN
                     ean_codes ec1 ON ec1.article_id = aa1.article_id
     */
}
