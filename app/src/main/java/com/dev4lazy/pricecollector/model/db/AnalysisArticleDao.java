package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;

import java.util.List;

@Dao
public interface AnalysisArticleDao {
    
    @Insert
    void insert( AnalysisArticle analysisArticle);
    
    @Update
    void update( AnalysisArticle analysisArticle);
    
    @Delete
    void delete( AnalysisArticle analysisArticle);
    
    @Query("DELETE FROM analysis_articles")
    void deleteAll();
    
    @Query("SELECT * FROM analysis_articles WHERE id= :id")
    LiveData<List<AnalysisArticle>> findAnalysisArticleById(String id);
    
    @Query("SELECT * FROM analysis_articles")
    LiveData<List<AnalysisArticle>> getAllAnalysisArticles();

    @Query("SELECT * from analysis_articles ORDER BY article_id ASC")
    DataSource.Factory<Integer, AnalysisArticle> getAllAnalysisArticlesPaged();

    @RawQuery(observedEntities = AnalysisArticle.class)
    LiveData<List<AnalysisArticle>> getAnalysisArticlesViaQuery(SupportSQLiteQuery query );

    // TODO !!!! najpierw skopiuj remote RemoteAnalysisRow do local analysis_article

    @Query(
            /*
            "SELECT " +
                    "analysis_articles.*, articles.name, own_articles_infos.ownCode, ean_codes.value " +
            "FROM " +
                    "analysis_articles " +
            "INNER JOIN " +
                    "articles ON articles.id = analysis_articles.article_id "+
            "INNER JOIN " +
                    "own_articles_infos ON own_articles_infos.article_id = analysis_articles.article_id " +
            "INNER JOIN " +
                    "ean_codes ON ean_codes.article_id = analysis_articles.article_id " +
            "INNER JOIN " +
                    "articles ON articles.id = analysis_articles.reference_article_id " +
            "INNER JOIN " +
                    "ean_codes ON ean_codes.article_id = analysis_articles.reference_article_id "
             */
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

    )
    LiveData<List<AnalysisArticleJoin>> getAllAnalysisArticlesJoin();

    /* TODO
    private int id; // from AnalysisArticle.
    private int analysisId; // from AnalysisArticle.
    private int article_Id; // from AnalysisArticle.
    private int competitorStoreId; // from AnalysisArticle.
    private Double competitorStorePrice; // from AnalysisArticle.
    private int referenceArticleId; // from AnalysisArticle.
    private String comments; // from AnalysisArticle.

    private String articleName; // from Article.name by AnalysisArticle.article_Id
    private String ownCode; // kod casto from Article. by AnalysisArticle.article_Id
    private String eanCode; // from Article. by AnalysisArticle.article_Id
    private String referenceArticleName; // from Article. by AnalysisArticle.referenceArticleId
    private String referenceArticleEan; // from Article. by AnalysisArticle.referenceArticleId
    */
}
