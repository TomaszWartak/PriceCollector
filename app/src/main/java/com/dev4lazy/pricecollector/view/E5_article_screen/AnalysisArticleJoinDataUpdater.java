package com.dev4lazy.pricecollector.view.E5_article_screen;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.CompetitorPrice;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.model.logic.LocalDataRepository;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;

import java.util.ArrayList;

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

    /* TODO XXX
    void saveReferenceArticle(
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinViewModel.ChangeInformer changeInformer) {
        changeInformer.clearFlagReferenceArticleChanged();
        if (isReferenceArticleInDB(analysisArticleJoin)) {
            if (analysisArticleJoin.isReferenceArticleDescriptionSet()) {
                Article referenceArticle = prepareReferenceArticleData(analysisArticleJoin);
                updateReferenceArticle(referenceArticle, analysisArticleJoin, changeInformer);
            }
        } else {
            if (analysisArticleJoin.isReferenceArticleDescriptionSet()) {
                Article referenceArticle = prepareReferenceArticleData(analysisArticleJoin);
                insertReferenceArticle(referenceArticle, analysisArticleJoin, changeInformer);
            }
        }
    }

    boolean isReferenceArticleInDB(AnalysisArticleJoin analysisArticleJoin) {
        if (analysisArticleJoin.getReferenceArticleId() == null) {
            return false;
        }
        return analysisArticleJoin.getReferenceArticleId() > -1;
    }

    Article prepareReferenceArticleData(AnalysisArticleJoin analysisArticleJoin) {
        Article article = new Article();
        article.setId(analysisArticleJoin.getReferenceArticleIdInt());
        article.setRemote_id(-1); // Artykuł referencyjny nie ma odpowiednika w zdalnej bazie danych
        article.setName(analysisArticleJoin.getReferenceArticleName());
        article.setDescription(analysisArticleJoin.getReferenceArticleDescription());
        // TODO ??? co z kodem kreskowym artykułu referencyjnego
        return article;
    }

    void updateReferenceArticle(
            Article referenceArticle,
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinViewModel.ChangeInformer changeInformer) {
        MutableLiveData<Integer> updateResult = new MutableLiveData<Integer>();
        Observer<Integer> updatingResultObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer updatedCount) {
                updateResult.removeObserver(this); // this = observer...
                DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                dataSource.invalidate();
                if (changeInformer.isPriceChangedFlagSet()) {
                    saveCompetitorPrice(analysisArticleJoin, changeInformer);
                }else {
                    if ( changeInformer.isCommentsChangedFlagSet() ) {
                        saveAnalysisArticle( analysisArticleJoin, changeInformer );
                    }
                }
            }
        };
        updateResult.observeForever(updatingResultObserver);
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        localDataRepository.updateArticle(referenceArticle, updateResult);
    }

    void insertReferenceArticle(
            Article referenceArticle,
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinViewModel.ChangeInformer changeInformer) {
        MutableLiveData<Long> insertResult = new MutableLiveData<Long>();
        Observer<Long> insertingResultObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long insertedReferenceArticleId) {
                insertResult.removeObserver(this); // this = observer...
                analysisArticleJoin.setAnalysisArticleId(insertedReferenceArticleId.intValue());
                // TODO XXX analysisArticlesPagerFragment.getAnalysisArticlesViewPager().getAdapter().notifyDataSetChanged();
                // TODO !!! Kolej na CompetitorPrice i zapisanie zmian w niej.
                //  Czyli jak nie było zmian w CompetitorPrice to nie ma potrezeby zapisu..
                //  Ale wtedy nie zostanie wywołany zapis AnalysisArticle, który jest wołany po zapisie ceny...
                //  Czyli w saveCompetitorprice, trzeba sprawdzić, czy była w niej zmiana i jesli nie było
                //  to wywoałać saveAnalysisArticle...
                DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                dataSource.invalidate();
                if ( changeInformer.isPriceChangedFlagSet() ){
                    saveCompetitorPrice(analysisArticleJoin, changeInformer);
                } else {
                    if ( changeInformer.isCommentsChangedFlagSet() ) {
                        saveAnalysisArticle( analysisArticleJoin, changeInformer );
                    }
                }
            }
        };
        insertResult.observeForever(insertingResultObserver);
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        localDataRepository.insertArticle(referenceArticle, insertResult);
    }

    void saveCompetitorPrice(
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinViewModel.ChangeInformer changeInformer) {
        changeInformer.clearFlagPriceChanged();
        if (isCompetitorStorePriceInDB(analysisArticleJoin)) {
            // TODO chyba bez sensu jest sprawdzanie, czy cena została ustawiona,
            //  bo mogła byc wykasowana i trzeba to zapisać, a nie zostanie zapisane
            //  bo nie wykasowana
            if (true ) {
                // Pobierz, uaktualnij i zapisz cenę <- nie trzeba pobierać - dane CompetitorPrice
                // można skompletoweć z analysisArticleJoin.
                // startCompetitorPriceUpdatingChain( analysisArticleJoin );
                CompetitorPrice competitorPrice = prepareCompetitorPriceData(analysisArticleJoin);
                updateCompetitorPrice(competitorPrice, analysisArticleJoin, changeInformer);
            }
        } else {
            // TODO chyba bez sensu jest sprawdzanie, czy cena została ustawiona,
            //  bo mogła byc wykasowana i trzeba to zapisać, a nie zostanie zapisane
            //  bo nie wykasowana
            if (true ) {
                // Przygotuj i zapisz cenę
                CompetitorPrice competitorPrice = prepareCompetitorPriceData(analysisArticleJoin);
                insertCompetitorPrice(competitorPrice, analysisArticleJoin, changeInformer);
            }
        }

    }

    boolean isCompetitorStorePriceInDB(AnalysisArticleJoin analysisArticleJoin) {
        if (analysisArticleJoin.getCompetitorStorePriceId() == null) {
            return false;
        }
        return analysisArticleJoin.getCompetitorStorePriceId() > -1;
    }

    void updateCompetitorPrice(
            CompetitorPrice competitorPrice,
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinViewModel.ChangeInformer changeInformer) {
        MutableLiveData<Integer> updateResult = new MutableLiveData<Integer>();
        Observer<Integer> updatingResultObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer updatedCount) {
                updateResult.removeObserver(this); // this = observer...
                DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                dataSource.invalidate();
                if (changeInformer.isCommentsChangedFlagSet() ) {
                    saveAnalysisArticle(analysisArticleJoin, changeInformer);
                }
            }
        };
        updateResult.observeForever(updatingResultObserver);
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        localDataRepository.updateCompetitorPrice(competitorPrice, updateResult);

    }

    CompetitorPrice prepareCompetitorPriceData(AnalysisArticleJoin analysisArticleJoin) {
        CompetitorPrice competitorPrice = new CompetitorPrice();
        int id = analysisArticleJoin.getCompetitorStorePriceId();
        if (id<0) {
            id=0;
        }
        competitorPrice.setId( id );
        competitorPrice.setAnalysisId( analysisArticleJoin.getAnalysisId() );
        competitorPrice.setAnalysisArticleId( analysisArticleJoin.getAnalysisArticleId() );
        competitorPrice.setOwnArticleInfoId( analysisArticleJoin.getOwnArticleInfoId() );
        competitorPrice.setCompetitorStoreId( analysisArticleJoin.getCompetitorStoreIdInt() );
        competitorPrice.setCompetitorStorePrice( analysisArticleJoin.getCompetitorStorePrice());
        competitorPrice.setReferenceArticleId( analysisArticleJoin.getReferenceArticleIdInt());
        return competitorPrice;
    }

    void insertCompetitorPrice(
            CompetitorPrice competitorPrice,
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinViewModel.ChangeInformer changeInformer) {
        MutableLiveData<Long> insertResult = new MutableLiveData<Long>();
        Observer<Long> insertingResultObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long insertedCompetitorPriceId) {
                insertResult.removeObserver(this); // this = observer...
                analysisArticleJoin.setCompetitorStorePriceId(insertedCompetitorPriceId.intValue());
                DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                dataSource.invalidate();
                if (changeInformer.isCommentsChangedFlagSet()) {
                    saveAnalysisArticle(analysisArticleJoin, changeInformer);
                }
            }
        };
        insertResult.observeForever(insertingResultObserver);
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        localDataRepository.insertCompetitorPrice(competitorPrice, insertResult);
    }

    void saveAnalysisArticle(
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinViewModel.ChangeInformer changeInformer) {
        changeInformer.clearFlagCommentsChanged();
        AnalysisArticle analysisArticle = prepareAnalysisArticleData(analysisArticleJoin);
        updateAnalysisArticle(analysisArticle);
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

    void updateAnalysisArticle(AnalysisArticle analysisArticle) {
        MutableLiveData<Integer> updateResult = new MutableLiveData<Integer>();
        Observer<Integer> updatingResultObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer updatedCount) {
                updateResult.removeObserver(this); // this = observer...
                DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                dataSource.invalidate();
            }
        };
        updateResult.observeForever(updatingResultObserver);
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        localDataRepository.updateAnalysisArticle(analysisArticle, updateResult);
    }

     */

    class TaskChain {

        private ArrayList<TaskLink> chain;

        TaskChain() {
            chain = new ArrayList<>();
        }

        void addTaskLink( TaskLink taskLink ) {
            if (!chain.isEmpty()) {
                chain.get(chain.size()-1).setNextTaskLink(taskLink);
            }
            chain.add( taskLink );
        }

        void startIt() {
            if (!chain.isEmpty()) {
                chain.get(0).doIt();
            }
        }
    }

    abstract class TaskLink {

        private TaskChain taskChain;
        private TaskLink nextTaskLink;

        public TaskLink(TaskChain taskChain) {
            this.taskChain = taskChain;
        }

        protected TaskLink getNextTaskLink() {
            return nextTaskLink;
        }

        protected void setNextTaskLink(TaskLink nextTaskLink) {
            this.nextTaskLink = nextTaskLink;
        }

        /**
         * Uruchamia następny TaskLink (jeśli jest) z danymi potrzebnymi do wykonania.
         * Musi byc unmieszczona jako ostatnia instrukcja w kodzie każdego TaskLink.
         * @param data (Object...) - dane do przekazania do nastęnego TaskLink.
         */
        public void runNextTaskLink(Object... data ) {
            if (getNextTaskLink()!=null) {
                getNextTaskLink().takeData( data );
                getNextTaskLink().doIt();
            } else {
                taskChain.chain.clear();
            }
        }

        /**
         * Przekazuje dane potrzebne do wykonania następnego TaskLink.
         * @param data (Object...) - dane do przekazania do nastęnego TaskLink.
         */
        abstract protected void takeData(Object... data);

        /**
         * Wywołuje kod do wykonania.
         */
        abstract protected void doIt();

    }

    class AnalysisArticleJoinUpdater extends TaskLink {

        protected AnalysisArticleJoin aAJ;
        protected AnalysisArticleJoinViewModel.ChangeInformer cI;

        public AnalysisArticleJoinUpdater(TaskChain taskChain ) {
            super(taskChain);
        }

        @Override
        public void doIt() {}

        @Override
        public void takeData(Object... data) {
            aAJ = (AnalysisArticleJoin)data[0];
            cI = (AnalysisArticleJoinViewModel.ChangeInformer)data[1];
        }

    }

    class ReferenceArticleUpdater extends AnalysisArticleJoinUpdater {

        public ReferenceArticleUpdater(
                TaskChain taskChain,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.ChangeInformer changeInformer ) {
            super(taskChain);
            aAJ = analysisArticleJoin;
            cI = changeInformer;
        }

        @Override
        public void doIt() {
            saveReferenceArticle( aAJ, cI );
        }

        void saveReferenceArticle(
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.ChangeInformer changeInformer) {
            changeInformer.clearFlagReferenceArticleChanged();
            if (isReferenceArticleInDB(analysisArticleJoin)) {
                if (analysisArticleJoin.isReferenceArticleDescriptionSet()) {
                    Article referenceArticle = prepareReferenceArticleData(analysisArticleJoin);
                    updateReferenceArticle(referenceArticle, analysisArticleJoin, changeInformer);
                }
            } else {
                if (analysisArticleJoin.isReferenceArticleDescriptionSet()) {
                    Article referenceArticle = prepareReferenceArticleData(analysisArticleJoin);
                    insertReferenceArticle(referenceArticle, analysisArticleJoin, changeInformer);
                }
            }
        }

        boolean isReferenceArticleInDB(AnalysisArticleJoin analysisArticleJoin) {
            if (analysisArticleJoin.getReferenceArticleId() == null) {
                return false;
            }
            return analysisArticleJoin.getReferenceArticleId() > -1;
        }

        Article prepareReferenceArticleData(AnalysisArticleJoin analysisArticleJoin) {
            Article article = new Article();
            article.setId(analysisArticleJoin.getReferenceArticleIdInt());
            article.setRemote_id(-1); // Artykuł referencyjny nie ma odpowiednika w zdalnej bazie danych
            article.setName(analysisArticleJoin.getReferenceArticleName());
            article.setDescription(analysisArticleJoin.getReferenceArticleDescription());
            // TODO ??? co z kodem kreskowym artykułu referencyjnego
            return article;
        }

        void updateReferenceArticle(
                Article referenceArticle,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.ChangeInformer changeInformer) {
            MutableLiveData<Integer> updateResult = new MutableLiveData<Integer>();
            Observer<Integer> updatingResultObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer updatedCount) {
                    updateResult.removeObserver(this); // this = observer...
                    DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                    dataSource.invalidate();
                    runNextTaskLink( analysisArticleJoin, changeInformer );
                    /* TODO XXX
                    if (getNextLink()!=null) {
                        getNextLink().takeData( analysisArticleJoin, changeInformer );
                        getNextLink().doIt();
                    }
                    */
                }
            };
            updateResult.observeForever(updatingResultObserver);
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            localDataRepository.updateArticle(referenceArticle, updateResult);
        }

        void insertReferenceArticle(
                Article referenceArticle,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.ChangeInformer changeInformer) {
            MutableLiveData<Long> insertResult = new MutableLiveData<Long>();
            Observer<Long> insertingResultObserver = new Observer<Long>() {
                @Override
                public void onChanged(Long insertedReferenceArticleId) {
                    insertResult.removeObserver(this); // this = observer...
                    analysisArticleJoin.setAnalysisArticleId(insertedReferenceArticleId.intValue());
                    // TODO XXX analysisArticlesPagerFragment.getAnalysisArticlesViewPager().getAdapter().notifyDataSetChanged();
                    // TODO !!! Kolej na CompetitorPrice i zapisanie zmian w niej.
                    //  Czyli jak nie było zmian w CompetitorPrice to nie ma potrezeby zapisu..
                    //  Ale wtedy nie zostanie wywołany zapis AnalysisArticle, który jest wołany po zapisie ceny...
                    //  Czyli w saveCompetitorprice, trzeba sprawdzić, czy była w niej zmiana i jesli nie było
                    //  to wywoałać saveAnalysisArticle...
                    DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                    dataSource.invalidate();
                    runNextTaskLink( analysisArticleJoin, changeInformer );
                    /* TODO XXX
                    if (getNextLink()!=null) {
                        getNextLink().takeData( analysisArticleJoin, changeInformer );
                        getNextLink().doIt();
                    }
                    */
                }
            };
            insertResult.observeForever(insertingResultObserver);
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            localDataRepository.insertArticle(referenceArticle, insertResult);
        }

    }

    class CompetitorPriceUpdater extends AnalysisArticleJoinUpdater {

        public CompetitorPriceUpdater(
                TaskChain taskChain,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.ChangeInformer changeInformer ) {
            super( taskChain );
            aAJ = analysisArticleJoin;
            cI = changeInformer;
        }

        @Override
        public void doIt() {
            saveCompetitorPrice( aAJ, cI );
        }

        void saveCompetitorPrice(
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.ChangeInformer changeInformer) {
            changeInformer.clearFlagPriceChanged();
            if (isCompetitorStorePriceInDB(analysisArticleJoin)) {
                // TODO chyba bez sensu jest sprawdzanie, czy cena została ustawiona,
                //  bo mogła byc wykasowana i trzeba to zapisać, a nie zostanie zapisane
                //  bo nie wykasowana
                if (true /*TODO XXX  analysisArticleJoin.isCompetitorStorePriceSet()*/) {
                    // Pobierz, uaktualnij i zapisz cenę <- nie trzeba pobierać - dane CompetitorPrice
                    // można skompletoweć z analysisArticleJoin.
                    // startCompetitorPriceUpdatingChain( analysisArticleJoin );
                    CompetitorPrice competitorPrice = prepareCompetitorPriceData(analysisArticleJoin);
                    updateCompetitorPrice(competitorPrice, analysisArticleJoin, changeInformer);
                }
            } else {
                // TODO chyba bez sensu jest sprawdzanie, czy cena została ustawiona,
                //  bo mogła byc wykasowana i trzeba to zapisać, a nie zostanie zapisane
                //  bo nie wykasowana
                if (true /*TODO XXX analysisArticleJoin.isCompetitorStorePriceSet()*/) {
                    // Przygotuj i zapisz cenę
                    CompetitorPrice competitorPrice = prepareCompetitorPriceData(analysisArticleJoin);
                    insertCompetitorPrice(competitorPrice, analysisArticleJoin, changeInformer);
                }
            }

        }

        boolean isCompetitorStorePriceInDB(AnalysisArticleJoin analysisArticleJoin) {
            if (analysisArticleJoin.getCompetitorStorePriceId() == null) {
                return false;
            }
            return analysisArticleJoin.getCompetitorStorePriceId() > -1;
        }

        void updateCompetitorPrice(
                CompetitorPrice competitorPrice,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.ChangeInformer changeInformer) {
            MutableLiveData<Integer> updateResult = new MutableLiveData<Integer>();
            Observer<Integer> updatingResultObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer updatedCount) {
                    updateResult.removeObserver(this); // this = observer...
                    DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                    dataSource.invalidate();
                    runNextTaskLink( analysisArticleJoin, changeInformer );
                    /* TODO XXX
                    if (getNextLink()!=null) {
                        getNextLink().takeData( analysisArticleJoin, changeInformer );
                        getNextLink().doIt();
                    }
                    */
                }
            };
            updateResult.observeForever(updatingResultObserver);
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            localDataRepository.updateCompetitorPrice(competitorPrice, updateResult);

        }

        CompetitorPrice prepareCompetitorPriceData(AnalysisArticleJoin analysisArticleJoin) {
            CompetitorPrice competitorPrice = new CompetitorPrice();
            int id = analysisArticleJoin.getCompetitorStorePriceId();
            if (id<0) {
                id=0;
            }
            competitorPrice.setId( id );
            competitorPrice.setAnalysisId( analysisArticleJoin.getAnalysisId() );
            competitorPrice.setAnalysisArticleId( analysisArticleJoin.getAnalysisArticleId() );
            competitorPrice.setOwnArticleInfoId( analysisArticleJoin.getOwnArticleInfoId() );
            competitorPrice.setCompetitorStoreId( analysisArticleJoin.getCompetitorStoreIdInt() );
            competitorPrice.setCompetitorStorePrice( analysisArticleJoin.getCompetitorStorePrice());
            competitorPrice.setReferenceArticleId( analysisArticleJoin.getReferenceArticleIdInt());
            return competitorPrice;
        }

        void insertCompetitorPrice(
                CompetitorPrice competitorPrice,
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.ChangeInformer changeInformer) {
            MutableLiveData<Long> insertResult = new MutableLiveData<Long>();
            Observer<Long> insertingResultObserver = new Observer<Long>() {
                @Override
                public void onChanged(Long insertedCompetitorPriceId) {
                    insertResult.removeObserver(this); // this = observer...
                    analysisArticleJoin.setCompetitorStorePriceId( insertedCompetitorPriceId.intValue() );
                    DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                    dataSource.invalidate();
                    runNextTaskLink( analysisArticleJoin, changeInformer );
                    /* TODO XXX
                    if (getNextLink()!=null) {
                        getNextLink().takeData( analysisArticleJoin, changeInformer );
                        getNextLink().doIt();
                    }
                    */
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
                AnalysisArticleJoinViewModel.ChangeInformer changeInformer ) {
            super( taskChain );
            aAJ = analysisArticleJoin;
            cI = changeInformer;
        }

        @Override
        public void doIt() {
            saveAnalysisArticle(aAJ, cI);
        }

        void saveAnalysisArticle(
                AnalysisArticleJoin analysisArticleJoin,
                AnalysisArticleJoinViewModel.ChangeInformer changeInformer) {
            changeInformer.clearFlagCommentsChanged();
            AnalysisArticle analysisArticle = prepareAnalysisArticleData(analysisArticleJoin);
            updateAnalysisArticle( analysisArticle, analysisArticleJoin, changeInformer );
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
                AnalysisArticleJoinViewModel.ChangeInformer changeInformer ) {
            MutableLiveData<Integer> updateResult = new MutableLiveData<Integer>();
            Observer<Integer> updatingResultObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer updatedCount) {
                    updateResult.removeObserver(this); // this = observer...
                    DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
                    dataSource.invalidate();
                    runNextTaskLink( analysisArticleJoin, changeInformer );
                }
            };
            updateResult.observeForever(updatingResultObserver);
            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
            localDataRepository.updateAnalysisArticle(analysisArticle, updateResult);

        }
    }

}