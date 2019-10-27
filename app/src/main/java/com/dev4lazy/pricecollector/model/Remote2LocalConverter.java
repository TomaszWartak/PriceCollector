package com.dev4lazy.pricecollector.model;

import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.EanCode;
import com.dev4lazy.pricecollector.model.entities.OwnArticleInfo;
import com.dev4lazy.pricecollector.model.entities.Sector;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysis;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_data.RemoteDepartment;
import com.dev4lazy.pricecollector.remote_data.RemoteEanCode;
import com.dev4lazy.pricecollector.remote_data.RemoteSector;

public class Remote2LocalConverter {

    public Analysis getAnalysis( RemoteAnalysis remoteAnalysis ) {
        Analysis analysis = new Analysis();
        analysis.setCreationDate( remoteAnalysis.getCreationDate() );
        analysis.setDueDate( remoteAnalysis.getDueDate() );
        analysis.setFinishDate( remoteAnalysis.getFinishDate() );
        analysis.setConfirmationDate( remoteAnalysis.getConfirmationDate() );
        return analysis;
    }

    public Article getArticle( RemoteAnalysisRow remoteAnalysisRow ) {
        Article article = new Article();
        article.setRemote_id( remoteAnalysisRow.getArticleCode() );
        article.setName( remoteAnalysisRow.getArticleName() ) ;
        return article;
    }

    public Department getDepartment( RemoteDepartment remoteDepartment ) {
        Department department = new Department();
        department.setRemote_id( remoteDepartment.getId() );
        department.setName( remoteDepartment.getName() );
        department.setSymbol( remoteDepartment.getSymbol() );
        return department;
    }

    public Sector getSector( RemoteSector remoteSector ) {
        Sector sector = new Sector();
        sector.setRemote_id( remoteSector.getId() );
        sector.setName( remoteSector.getName() );
        return sector;
    }

    public OwnArticleInfo getOwnArticleInfo(
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

    public EanCode getEanCode( RemoteEanCode remoteEanCode, Article article ) {
        EanCode localEanCode = new EanCode();
        localEanCode.remote_id = remoteEanCode.getId();
        localEanCode.value = remoteEanCode.getValue();
        localEanCode.articleId = article.getId();
        return localEanCode;
    }

}
