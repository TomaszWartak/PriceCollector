package com.dev4lazy.pricecollector.model.db;

import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.CompetitorPrice;
import com.dev4lazy.pricecollector.model.entities.EanCode;
import com.dev4lazy.pricecollector.model.entities.OwnArticleInfo;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoinForPricesUpload;

import java.util.List;

import androidx.room.Dao;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

@Dao
public interface AnalysisArticleJoinDao {

    @RawQuery(
            observedEntities = {
                    AnalysisArticle.class,
                    Article.class,
                    CompetitorPrice.class,
                    OwnArticleInfo.class,
                    EanCode.class
            }
    )
    List<AnalysisArticleJoin> getAnalysisArticlesJoinsViaQuery(SimpleSQLiteQuery query);

    @RawQuery(
            observedEntities = {
                    Analysis.class,
                    CompetitorPrice.class,
                    Company.class,
                    OwnArticleInfo.class
            }
    )
    List<AnalysisArticleJoinForPricesUpload> getAnalysisArticlesJoinsForPricesUploadViaQuery(SimpleSQLiteQuery query);

}
