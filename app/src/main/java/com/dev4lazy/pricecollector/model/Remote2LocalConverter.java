package com.dev4lazy.pricecollector.model;

import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.EanCode;
import com.dev4lazy.pricecollector.model.entities.OwnArticleInfo;
import com.dev4lazy.pricecollector.model.entities.Sector;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysis;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_data.RemoteDepartment;
import com.dev4lazy.pricecollector.remote_data.RemoteEanCode;
import com.dev4lazy.pricecollector.remote_data.RemoteSector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Remote2LocalConverter {

    public Analysis createAnalysis(RemoteAnalysis remoteAnalysis ) {
        Analysis analysis = new Analysis();
        analysis.setCreationDate( remoteAnalysis.getCreationDate() );
        analysis.setDueDate( remoteAnalysis.getDueDate() );
        analysis.setFinishDate( remoteAnalysis.getFinishDate() );
        analysis.setConfirmationDate( remoteAnalysis.getConfirmationDate() );
        return analysis;
    }

    public Article createArticle(RemoteAnalysisRow remoteAnalysisRow ) {
        Article article = new Article();
        article.setRemote_id( remoteAnalysisRow.getArticleCode() );
        article.setName( remoteAnalysisRow.getArticleName() ) ;
        return article;
    }

    public ArrayList<Article> createArticlesList(ArrayList<RemoteAnalysisRow> remoteAnalysisRowList ) {
        ArrayList<Article> articlesList = new ArrayList<>();
        Article article;
        for ( RemoteAnalysisRow remoteAnalysisRow : remoteAnalysisRowList ) {
           article = createArticle( remoteAnalysisRow );
           articlesList.add( article );
        }
        return articlesList;
    }

    public Department createDepartment(RemoteDepartment remoteDepartment ) {
        Department department = new Department();
        department.setRemote_id( remoteDepartment.getId() );
        department.setName( remoteDepartment.getName() );
        department.setSymbol( remoteDepartment.getSymbol() );
        return department;
    }

    public Sector createSector(RemoteSector remoteSector ) {
        Sector sector = new Sector();
        sector.setRemote_id( remoteSector.getId() );
        sector.setName( remoteSector.getName() );
        return sector;
    }

    public OwnArticleInfo createOwnArticleInfo(
            RemoteAnalysisRow remoteAnalysisRow,
            Article article,
            Department department,
            Sector sector ) {
        OwnArticleInfo ownArticleInfo = new OwnArticleInfo();
        ownArticleInfo.setArticleId( article.getId() );
        ownArticleInfo.setOwnCode( String.valueOf( remoteAnalysisRow.getArticleCode() ) );
        ownArticleInfo.setRefPrice( remoteAnalysisRow.getArticleRefPrice() );
        ownArticleInfo.setStorePrice( remoteAnalysisRow.getArticleStorePrice() );
        ownArticleInfo.setSectorId( sector.getId() );
        ownArticleInfo.setDepartmentId( department.getId() );
        return ownArticleInfo;
    }

    public EanCode createEanCode(RemoteEanCode remoteEanCode, Article article ) {
        EanCode localEanCode = new EanCode();
        // remoteEanCode.article_id = casto;
        localEanCode.remote_id = remoteEanCode.getId();
        localEanCode.value = remoteEanCode.getValue();
        localEanCode.articleId = article.getId();
        return localEanCode;
    }

    /**
     *
     * @param remoteEanCodesHashMap - mapa eanów pobranych z bazy zdalnej
     * @param articlesHashMap - mapa Article pobrana z bazy lokalnej
     * @return ArrayList<EanCode>
     */
    public ArrayList<EanCode> createEanCodesList(
            HashMap<Integer, RemoteEanCode> remoteEanCodesHashMap,
            HashMap<Integer, Article> articlesHashMap ) {
        // Podczas tworzenia Article z RemoteAnalysisRow (createArticle() ) do Article.remoteId jest wpisywany kod casto.
        // Podobnie przy tworzeniu RemoteEanCode, do jego pola articleId również jest wpisywany kod casto.
        // Dzięki temu można sparować eanCode z Article.
        ArrayList<EanCode> eanCodesList = new ArrayList<>();
        RemoteEanCode remoteEanCode;
        for (Map.Entry<Integer, RemoteEanCode> remoteEanCodeEntry : remoteEanCodesHashMap.entrySet() ) {
            remoteEanCode = remoteEanCodeEntry.getValue();
            Article article = articlesHashMap.get( remoteEanCodeEntry.getKey() );
            eanCodesList.add( createEanCode( remoteEanCode, article ));
        }
        return eanCodesList;
    }

    public AnalysisArticle createAnalysisArticle(
            RemoteAnalysisRow remoteAnalysisRow,
            Analysis analysis,
            // todo? Article article,
            OwnArticleInfo ownArticleInfo,
            Store copetitorStore
    ) {
        AnalysisArticle analysisArticle = new AnalysisArticle();
        analysisArticle.setAnalysisId( analysis.getId() );
        //analysisArticle.setArticleId( article.getId() );
        analysisArticle.setArticleId( ownArticleInfo.getArticleId() );
        analysisArticle.setOwnArticleInfoId( ownArticleInfo.getId() );
        analysisArticle.setArticleRefPrice( remoteAnalysisRow.getArticleRefPrice() );
        analysisArticle.setArticleStorePrice( remoteAnalysisRow.getArticleStorePrice() );
        return analysisArticle;
    }

    public AnalysisArticle createAnalysisArticlesList(
            Analysis analysis,
            Article article,
            Store copetitorStore
    ) {
        return null;
    }
}
