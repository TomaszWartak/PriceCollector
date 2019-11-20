package com.dev4lazy.pricecollector.model;

import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.EanCode;
import com.dev4lazy.pricecollector.model.entities.Family;
import com.dev4lazy.pricecollector.model.entities.Market;
import com.dev4lazy.pricecollector.model.entities.Module;
import com.dev4lazy.pricecollector.model.entities.OwnArticleInfo;
import com.dev4lazy.pricecollector.model.entities.Sector;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.model.entities.UOProject;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysis;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_data.RemoteDepartment;
import com.dev4lazy.pricecollector.remote_data.RemoteEanCode;
import com.dev4lazy.pricecollector.remote_data.RemoteFamily;
import com.dev4lazy.pricecollector.remote_data.RemoteMarket;
import com.dev4lazy.pricecollector.remote_data.RemoteModule;
import com.dev4lazy.pricecollector.remote_data.RemoteSector;
import com.dev4lazy.pricecollector.remote_data.RemoteUOProject;

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

    public Family createFamily(RemoteFamily remoteFamily ) {
        Family family = new Family();
        family.setRemote_id( remoteFamily.getId() );
        family.setName( remoteFamily.getName() );
        return family;
    }
    
    public Market createMarket(RemoteMarket remoteMarket ) {
        Market market = new Market();
        market.setRemote_id( remoteMarket.getId() );
        market.setName( remoteMarket.getName() );
        return market;
    }

    public Module createModule(RemoteModule remoteModule ) {
        Module module = new Module();
        module.setRemote_id( remoteModule.getId() );
        module.setName( remoteModule.getName() );
        return module;
    }

    public UOProject createUOProject(RemoteUOProject remoteUOProject ) {
        UOProject uoProject = new UOProject();
        uoProject.setRemote_id( remoteUOProject.getId() );
        uoProject.setName( remoteUOProject.getName() );
        return uoProject;
    }
    
    public OwnArticleInfo createOwnArticleInfo(
            RemoteAnalysisRow remoteAnalysisRow,
            Article article,
            Department department,
            Sector sector,
            Family family,
            Module module,
            Market market,
            UOProject uoProject ) {
        OwnArticleInfo ownArticleInfo = new OwnArticleInfo();
        ownArticleInfo.setArticleId( article.getId() );
        ownArticleInfo.setOwnCode( String.valueOf( remoteAnalysisRow.getArticleCode() ) );
        Double articleRefPrice = remoteAnalysisRow.getArticleRefPrice();
        if (articleRefPrice==null) {
            articleRefPrice=0.0;
        }
        ownArticleInfo.setRefPrice( articleRefPrice );
        Double articleStorePrice = remoteAnalysisRow.getArticleStorePrice();
        if (articleStorePrice==null) {
            articleStorePrice = articleRefPrice.doubleValue();
        }
        ownArticleInfo.setStorePrice( articleStorePrice );
        ownArticleInfo.setSectorId( sector.getId() );
        ownArticleInfo.setDepartmentId( department.getId() );
        ownArticleInfo.setFamilyId( family.getId() );
        ownArticleInfo.setMarketId( market.getId() );
        ownArticleInfo.setModuleId( module.getId() );
        ownArticleInfo.setUoProjectId( uoProject.getId() );
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
