package com.dev4lazy.pricecollector.view.E5_article_screen;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.CompetitorPrice;
import com.dev4lazy.pricecollector.model.entities.EanCode;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.model.logic.LocalDataRepository;
import com.dev4lazy.pricecollector.utils.TaskChain;
import com.dev4lazy.pricecollector.utils.TaskLink;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.paging.DataSource;

public class AnalysisArticleJoinDataUpdater {
    // TODO XXX private final AnalysisArticlesPagerFragment analysisArticlesPagerFragment;
    private AnalysisArticleJoinsListViewModel analysisArticleJoinsListViewModel;

    private TaskChain taskChain;

    public AnalysisArticleJoinDataUpdater(AnalysisArticleJoinsListViewModel analysisArticleJoinsListViewModel ) {
       this.analysisArticleJoinsListViewModel = analysisArticleJoinsListViewModel;
       taskChain = new TaskChain();
    }

    public TaskChain getTaskChain() {
        return taskChain;
    }

    public void setTaskChain(TaskChain taskChain) {
        this.taskChain = taskChain;
    }

    class AnalysisArticleJoinUpdater extends TaskLink {

        protected AnalysisArticleJoin aAJ;
        protected AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder cI;

        public AnalysisArticleJoinUpdater(TaskChain taskChain ) {
            super(taskChain);
        }

        @Override
        public void doIt() {}

        @Override
        public void takeData(Object... data) {
            aAJ = (AnalysisArticleJoin)data[0];
            cI = (AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder)data[1];
        }

    }

    class ReferenceArticleUpdater extends AnalysisArticleJoinUpdater {

        public ReferenceArticleUpdater(
                TaskChain taskChain,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            super(taskChain);
            aAJ = analysisArticleJoin;
            cI = valuesStateHolder;
        }

        @Override
        public void doIt() {
            saveReferenceArticle( aAJ, cI );
        }

        void saveReferenceArticle(
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            valuesStateHolder.clearFlagReferenceArticleChanged();
            Article referenceArticle = prepareReferenceArticleData(analysisArticleJoin);
            if (isReferenceArticleInDB(analysisArticleJoin)) {
                updateReferenceArticle(referenceArticle, analysisArticleJoin, valuesStateHolder);
            } else {
                insertReferenceArticle(referenceArticle, analysisArticleJoin, valuesStateHolder);
            }
        }

        boolean isReferenceArticleInDB(AnalysisArticleJoin analysisArticleJoin) {
            if (analysisArticleJoin.getReferenceArticleId() == null) {
                return false;
            }
            return analysisArticleJoin.getReferenceArticleId() > 0;
        }

        Article prepareReferenceArticleData(AnalysisArticleJoin analysisArticleJoin) {
            Article article = new Article();
            article.setId( getProperId( analysisArticleJoin.getReferenceArticleId()) );
            article.setRemote_id(-1); // -1 = Artykuł referencyjny nie ma odpowiednika w zdalnej bazie danych
            article.setName(analysisArticleJoin.getReferenceArticleName());
            article.setDescription(analysisArticleJoin.getReferenceArticleDescription());
            return article;
        }

        void updateReferenceArticle(
                Article referenceArticle,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            MutableLiveData<Integer> updateResult = new MutableLiveData<Integer>();
            Observer<Integer> updatingResultObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer updatedCount) {
                    updateResult.removeObserver(this); // this = observer...
                    DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                    dataSource.invalidate();
                    runNextTaskLink( analysisArticleJoin, valuesStateHolder);
                }
            };
            updateResult.observeForever(updatingResultObserver);
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            localDataRepository.updateArticle(referenceArticle, updateResult);
        }

        void insertReferenceArticle(
                Article referenceArticle,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            MutableLiveData<Long> insertResult = new MutableLiveData<Long>();
            Observer<Long> insertingResultObserver = new Observer<Long>() {
                @Override
                public void onChanged(Long insertedReferenceArticleId) {
                    insertResult.removeObserver(this); // this = observer...
                    analysisArticleJoin.setReferenceArticleId(insertedReferenceArticleId.intValue());
                    DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                    dataSource.invalidate();
                    runNextTaskLink( analysisArticleJoin, valuesStateHolder);
                }
            };
            insertResult.observeForever(insertingResultObserver);
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            localDataRepository.insertArticle(referenceArticle, insertResult);
        }

    }

    class ReferenceArticleEanUpdater extends AnalysisArticleJoinUpdater {

        public ReferenceArticleEanUpdater(
                TaskChain taskChain,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            super( taskChain );
            aAJ = analysisArticleJoin;
            cI = valuesStateHolder;
        }

        @Override
        public void doIt() {
            saveReferenceArticleEan(aAJ, cI);
        }

        void saveReferenceArticleEan(
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            valuesStateHolder.clearFlagReferenceArticleEanChanged();
            EanCode eanCode = prepareEanCodeData( analysisArticleJoin );
            insertEanCode( eanCode, analysisArticleJoin, valuesStateHolder );
        }

        EanCode prepareEanCodeData( AnalysisArticleJoin analysisArticleJoin ) {
            EanCode eanCode = new EanCode();
            // todo
            //  id pobrać z refernceArticle? - nie... bo tam nie ma id EAN
            //  Trzeba ew. pobrać cały obiekt na podstawie article_id z ref Article.
            //  Jesli jest zmieniany, to znaczy, że to jest inny Ean do tego samego artykułu...?
            //  Czy założyć, że jeden artykuł, to jeden EAN <-- NIE...
            //  OK. Zakładamy, że jest wiele Eanów do jednego artykułu.
            //  - zmiana wartrości ean -> dodanie noiwego kodu ean
            //  - jesli jest wiele eanów -> który wyświetlić przy art ref i analysis article?
            //      - pierwszy
            //  - jak się zachowa query do joina przy wielu eanach do jednego artykułu?
            //  - generalnie nie obsługuję nigdzie wielu eanach do jednego artykułu
            //  Może dodac pole idEan do Joina, to będzie łatwiej może
            eanCode.setId( analysisArticleJoin.getReferenceArticleEanCodeIdInt() );
            // todo remote_id ustawić na -1
            eanCode.setRemote_id(-1);
            // todo skopiowac z Join
            eanCode.setValue( analysisArticleJoin.getReferenceArticleEanCodeValue() );
            // todo skopiowac z ref Article
            eanCode.setArticleId( analysisArticleJoin.getReferenceArticleId() );
            return eanCode;
        }

        private void insertEanCode(
                EanCode eanCode,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            MutableLiveData<Long> insertResult = new MutableLiveData<Long>();
            Observer<Long> insertingResultObserver = new Observer<Long>() {
                @Override
                public void onChanged(Long insertedEanCodeId) {
                    insertResult.removeObserver(this); // this = observer...
                    analysisArticleJoin.setReferenceArticleEanCodeId( insertedEanCodeId.intValue() );
                    DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                    dataSource.invalidate();
                    runNextTaskLink( analysisArticleJoin, valuesStateHolder);
                }
            };
            insertResult.observeForever(insertingResultObserver);
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            localDataRepository.insertEanCode( eanCode, insertResult);
        }

    }

    class ReferenceArticleEanDeleter extends AnalysisArticleJoinUpdater {

        public ReferenceArticleEanDeleter(
                TaskChain taskChain,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            super( taskChain );
            aAJ = analysisArticleJoin;
            cI = valuesStateHolder;
        }

        @Override
        public void doIt() {
            deleteReferenceArticleEan(aAJ, cI);
        }

        void deleteReferenceArticleEan(
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            valuesStateHolder.clearFlagReferenceArticleEanChanged();
            EanCode eanCode = prepareEanCodeData( analysisArticleJoin );
            deleteEanCode( eanCode, analysisArticleJoin, valuesStateHolder );
        }

        EanCode prepareEanCodeData( AnalysisArticleJoin analysisArticleJoin ) {
            EanCode eanCode = new EanCode();
            eanCode.setId( analysisArticleJoin.getReferenceArticleEanCodeIdInt() );
            eanCode.setArticleId( analysisArticleJoin.getReferenceArticleId() );
            eanCode.setValue( analysisArticleJoin.getEanCode() );
            eanCode.setRemote_id(-1); // -1 = w zdalnej bazie danych nie ma takiego kodu
            return eanCode;
        }

        private void deleteEanCode(
                EanCode eanCode,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            MutableLiveData<Integer> deleteResult = new MutableLiveData<>();
            Observer<Integer> deletingResultObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer deletedCount) {
                    deleteResult.removeObserver(this); // this = observer...
                    // TODO !!! trzeba zaktualizowac dane w join i Competitor price
                    // join .referenceArticleEanCodeId i .referenceArticleEan
                    // CP .referenceArticleEanCodeId
                    analysisArticleJoin.setReferenceArticleEanCodeId( null );
                    analysisArticleJoin.setReferenceArticleEanCodeValue( null );
                    DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                    dataSource.invalidate();
                    runNextTaskLink( analysisArticleJoin, valuesStateHolder);
                }
            };
            deleteResult.observeForever(deletingResultObserver);
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            localDataRepository.deleteEanCode( eanCode, deleteResult);
        }

    }

    class CompetitorPriceUpdater extends AnalysisArticleJoinUpdater {

        public CompetitorPriceUpdater(
                TaskChain taskChain,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            super( taskChain );
            aAJ = analysisArticleJoin;
            cI = valuesStateHolder;
        }

        @Override
        public void doIt() {
            saveCompetitorPrice( aAJ, cI );
        }

        void saveCompetitorPrice(
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            valuesStateHolder.clearFlagPriceChanged();
            CompetitorPrice competitorPrice = prepareCompetitorPriceData(analysisArticleJoin);
            if (isCompetitorStorePriceInDB(analysisArticleJoin)) {
                updateCompetitorPrice(competitorPrice, analysisArticleJoin, valuesStateHolder);
            } else {
                insertCompetitorPrice(competitorPrice, analysisArticleJoin, valuesStateHolder);
            }

        }

        boolean isCompetitorStorePriceInDB(AnalysisArticleJoin analysisArticleJoin) {
            if (analysisArticleJoin.getCompetitorStorePriceId() == null) {
                return false;
            }
            return analysisArticleJoin.getCompetitorStorePriceId() > 0;
        }

        void updateCompetitorPrice(
                CompetitorPrice competitorPrice,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            MutableLiveData<Integer> updateResult = new MutableLiveData<Integer>();
            Observer<Integer> updatingResultObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer updatedCount) {
                    updateResult.removeObserver(this); // this = observer...
                    DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                    dataSource.invalidate();
                    runNextTaskLink( analysisArticleJoin, valuesStateHolder);
                }
            };
            updateResult.observeForever(updatingResultObserver);
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            localDataRepository.updateCompetitorPrice(competitorPrice, updateResult);

        }

        CompetitorPrice prepareCompetitorPriceData(AnalysisArticleJoin analysisArticleJoin) {
            CompetitorPrice competitorPrice = new CompetitorPrice();
            competitorPrice.setId( getProperId( analysisArticleJoin.getCompetitorStorePriceId() ) );
            competitorPrice.setAnalysisId( analysisArticleJoin.getAnalysisId() );
            competitorPrice.setAnalysisArticleId( analysisArticleJoin.getAnalysisArticleId() );
            competitorPrice.setOwnArticleInfoId( analysisArticleJoin.getOwnArticleInfoId() );
            competitorPrice.setCompetitorStoreId( analysisArticleJoin.getCompetitorStoreIdInt() );
            competitorPrice.setCompetitorStorePrice( analysisArticleJoin.getCompetitorStorePrice());
            competitorPrice.setReferenceArticleId( analysisArticleJoin.getReferenceArticleIdInt());
            competitorPrice.setReferenceArticleEanCodeId( getProperId( analysisArticleJoin.getReferenceArticleEanCodeId() ) );
            return competitorPrice;
        }

        void insertCompetitorPrice(
                CompetitorPrice competitorPrice,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            MutableLiveData<Long> insertResult = new MutableLiveData<Long>();
            Observer<Long> insertingResultObserver = new Observer<Long>() {
                @Override
                public void onChanged(Long insertedCompetitorPriceId) {
                    insertResult.removeObserver(this); // this = observer...
                    analysisArticleJoin.setCompetitorStorePriceId( insertedCompetitorPriceId.intValue() );
                    DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                    dataSource.invalidate();
                    runNextTaskLink( analysisArticleJoin, valuesStateHolder);
                }
            };
            insertResult.observeForever(insertingResultObserver);
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            localDataRepository.insertCompetitorPrice(competitorPrice, insertResult);
        }
    }

    class AnalysisArticleUpdater extends AnalysisArticleJoinUpdater {

        public AnalysisArticleUpdater(
                TaskChain taskChain,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            super( taskChain );
            aAJ = analysisArticleJoin;
            cI = valuesStateHolder;
        }

        @Override
        public void doIt() {
            saveAnalysisArticle(aAJ, cI);
        }

        void saveAnalysisArticle(
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            valuesStateHolder.clearFlagCommentsChanged();
            AnalysisArticle analysisArticle = prepareAnalysisArticleData(analysisArticleJoin);
            updateAnalysisArticle( analysisArticle, analysisArticleJoin, valuesStateHolder);
        }

        AnalysisArticle prepareAnalysisArticleData(AnalysisArticleJoin analysisArticleJoin) {
            AnalysisArticle analysisArticle = new AnalysisArticle();
            analysisArticle.setId(analysisArticleJoin.getAnalysisArticleId());
            analysisArticle.setAnalysisId(analysisArticleJoin.getAnalysisId());
            analysisArticle.setArticleId(analysisArticleJoin.getArticleId());
            analysisArticle.setOwnArticleInfoId(analysisArticleJoin.getOwnArticleInfoId());
            analysisArticle.setArticleStorePrice(analysisArticleJoin.getArticleStorePrice());
            analysisArticle.setArticleRefPrice(analysisArticleJoin.getArticleRefPrice());
            // analysisArticle.setArticleNewPrice( null );
            analysisArticle.setReferenceArticleId(analysisArticleJoin.getReferenceArticleIdInt());
            // analysisArticle.setCompetitorStorePriceId( analysisArticleJoin.getCompetitorStorePriceId() );
            analysisArticle.setComments(analysisArticleJoin.getComments());
            return analysisArticle;
        }

        void updateAnalysisArticle(
                AnalysisArticle analysisArticle,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder) {
            MutableLiveData<Integer> updateResult = new MutableLiveData<Integer>();
            Observer<Integer> updatingResultObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer updatedCount) {
                    updateResult.removeObserver(this); // this = observer...
                    DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                    dataSource.invalidate();
                    runNextTaskLink( analysisArticleJoin, valuesStateHolder);
                }
            };
            updateResult.observeForever(updatingResultObserver);
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            localDataRepository.updateAnalysisArticle(analysisArticle, updateResult);
        }
    }

    private int getProperId( Integer id ) {
        int properId;
        if (idIsNotSet(id)) {
            properId=0;
        } else {
            properId = id.intValue();
        }
        return properId;
    }

    private boolean idIsNotSet( Integer id ) {
        return ((id==null) || (id<1));
    }

}