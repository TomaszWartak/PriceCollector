package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.RoomWarnings;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;

import java.util.List;

@Dao
public interface AnalysisArticleDao {
    
    @Insert
    void insert( AnalysisArticle analysisArticle );
    
    @Update
    void update( AnalysisArticle analysisArticle );
    
    @Delete
    void delete( AnalysisArticle analysisArticle );
    
    @Query("DELETE FROM analysis_articles")
    void deleteAll();
    
    @Query("SELECT * FROM analysis_articles WHERE id= :id")
    LiveData<List<AnalysisArticle>> findAnalysisArticleById(String id);
    
    @Query("SELECT * FROM analysis_articles")
    LiveData<List<AnalysisArticle>> getAllAnalysisArticles();
    
    @RawQuery(observedEntities = AnalysisArticle.class)
    LiveData<List<AnalysisArticle>> getAnalysisArticlesViaQuery( SupportSQLiteQuery query );

    // TODO !!!! najpierw skopiuj remote AnalysisRow do local analysis_article

    @Query(
            "SELECT " +
                    "analysis_articles.*, articles.name, own_articles_infos.ownCode, ean_codes.* " +
            "FROM " +
                    "analysis_articles " +
            "INNER JOIN " +
                    "articles ON articles.id = article_Id "+
            "INNER JOIN " +
                    "own_articles_infos ON own_articles_infos.article_id = article_Id " +
            "INNER JOIN " +
                    "ean_codes ON ean_codes.article_id = article_Id"

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
