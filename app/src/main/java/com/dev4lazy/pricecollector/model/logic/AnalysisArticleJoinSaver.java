package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.utils.TaskChain;
import com.dev4lazy.pricecollector.utils.TaskLink;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;

import androidx.paging.DataSource;

public class AnalysisArticleJoinSaver {

    private AnalysisArticleJoinsListViewModel analysisArticleJoinsListViewModel;
    private AnalysisArticleJoinDataUpdater analysisArticleJoinDataUpdater;
    private AnalysisArticleJoinValuesStateHolder valuesStateHolder;

    public AnalysisArticleJoinSaver(
            AnalysisArticleJoinsListViewModel analysisArticleJoinsListViewModel,
            AnalysisArticleJoinValuesStateHolder valuesStateHolder ) {
        this.analysisArticleJoinsListViewModel = analysisArticleJoinsListViewModel;
        this.valuesStateHolder = valuesStateHolder;
        analysisArticleJoinDataUpdater = new AnalysisArticleJoinDataUpdater( );
    }

    public void startSavingDataChain( AnalysisArticleJoin analysisArticleJoin ) {
        TaskChain taskChain = analysisArticleJoinDataUpdater.getTaskChain();
        /*
        A. Jeśli dane artykułu referencyjnego (nazwa, opis) (Article) zostały zmienione, trzeba:
            1. zapisać go lub uaktualnić.
            2. Sprawdzić, czy przy tej okazji został zmieniony jego kod EAN (EanCode)
                Jeśli tak i :
                a) nie ma ustawionej wartości, to trzeba go usunąć z BD, jeśli jest w DB
                b) ma ustawiną wartość - należy ean zapisać i zarejestrowac tą zmianę w cenie (CompetitorPrice).
            3. Jeśli Ean nie był zmienioeny, to trzeba sprawdzić, czy cena była zmieniana i
                a) jesli była zmieniana, i jeśli tak, to zapisać ją
                b) jeśli nie była zmieniana, to w sytuacji, gdy artykuł referencyjny jeszcze nie istnieje w BD,
                to trzeba jego id zapisać w cenie - czyli trzeba ją tak, czy siak, zapisać
        B. Jeśli dane artykułu referencyjnego (nazwa, opis) (Article) nie zostały zmienione, trzeba:
            1. Sprawdzić, czy został zmieniony kod EAN (EanCode). Jeśli został zmieniony, to
                a) gdy nie ma ustawionej wartości, to trzeba go usunąć z BD (jeśli jest w DB)
                Gdy ma ustawioną wartość, to:
                b) jeśli artykuł referencyjny jeszcze nie istnieje w BD, to trzeba go zapisać
                c) zapisać ean
                d) i zaktualizować id art ref i ean w cenie (CompetitorPrice).
            2. Jeśli Ean nie był zmieniony, to
                a) trzeba sprawdzićż czy cena była zmieniona i zapisać ją
        C. Jeśli został zmieniony Artykuł Strategiczny (AnalysisArticle) to trzeba go zapisać.
        */
/*A*/   if (valuesStateHolder.isReferenceArticleChangedFlagSet() ) {
            savingChangesOfReferenceArticle( analysisArticleJoin, taskChain );
/*B*/   } else {
    /*B1*/  if (valuesStateHolder.isReferenceArticleEanChangedFlagSet()) { /**/
                savingChangesOfRefArtEanIfRefArtIsNotChanged( analysisArticleJoin, taskChain );
    /*B2*/  } else {
        /*B2a*/ if (valuesStateHolder.isCompetitorPriceChangedFlagSet()) {
                    taskChain.addTaskLink( analysisArticleJoinDataUpdater.new CompetitorPriceUpdater( ) );
                }
            }
        }
  /*C*/ if (valuesStateHolder.isCommentsChangedFlagSet()) {
            taskChain.addTaskLink( analysisArticleJoinDataUpdater.new AnalysisArticleUpdater( ) );
        }
        analysisArticleJoinDataUpdater.getTaskChain().setAfterAllToDoTask( new DataSourceInvalidator() );
        analysisArticleJoinDataUpdater.getTaskChain().startIt( analysisArticleJoin, valuesStateHolder);
    }

    private class DataSourceInvalidator extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            DataSource dataSource = analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().getValue().getDataSource();
            dataSource.invalidate();
        }
    }

    public void savingChangesOfReferenceArticle(
            AnalysisArticleJoin analysisArticleJoin,
            TaskChain taskChain ) {
 /*A1*/ taskChain.addTaskLink( analysisArticleJoinDataUpdater.new ReferenceArticleUpdater() );
        if (valuesStateHolder.isReferenceArticleEanChangedFlagSet()) {
     /*A2*/ savingChangesOfRefArtEanIfRefArtIsChanged( analysisArticleJoin, taskChain );
        } else {
     /*A3*/ savingChangesOfCompetitorPriceIfRefArtIsChanged( analysisArticleJoin, taskChain );
        }
    }

    public void /*A2*/ savingChangesOfRefArtEanIfRefArtIsChanged(
            AnalysisArticleJoin analysisArticleJoin,
            TaskChain taskChain ) {
/*A2a*/ if (isReferenceArticleEanCleared(analysisArticleJoin)) {
            if (isReferenceArticleEanInDB(analysisArticleJoin)) {
                taskChain.addTaskLink( analysisArticleJoinDataUpdater.new ReferenceArticleEanDeleter( ) );
                taskChain.addTaskLink( analysisArticleJoinDataUpdater.new CompetitorPriceUpdater( ) );
            }
/*A2b*/} else {
            taskChain.addTaskLink( analysisArticleJoinDataUpdater.new ReferenceArticleEanUpdater( ) );
            taskChain.addTaskLink(analysisArticleJoinDataUpdater.new CompetitorPriceUpdater( ) );
        }
    }

    public void /*A3*/ savingChangesOfCompetitorPriceIfRefArtIsChanged(
            AnalysisArticleJoin analysisArticleJoin,
            TaskChain taskChain ) {
/*A3a*/ if (valuesStateHolder.isCompetitorPriceChangedFlagSet()) {
            taskChain.addTaskLink( analysisArticleJoinDataUpdater.new CompetitorPriceUpdater( ) );
        } else {
    /*A3b*/  if (isReferenceArticleNotInDB(analysisArticleJoin)) {
                taskChain.addTaskLink( analysisArticleJoinDataUpdater.new CompetitorPriceUpdater( ) );
            }
        }
    }

    public void savingChangesOfRefArtEanIfRefArtIsNotChanged(
            AnalysisArticleJoin analysisArticleJoin,
            TaskChain taskChain ) {
/*B1a*/ if (isReferenceArticleEanCleared(analysisArticleJoin)) {
            if (isReferenceArticleEanInDB(analysisArticleJoin)) {
                taskChain.addTaskLink( analysisArticleJoinDataUpdater.new ReferenceArticleEanDeleter( ) );
                taskChain.addTaskLink( analysisArticleJoinDataUpdater.new CompetitorPriceUpdater( ) );
            }
         } else {
     /*B1b*/ if (isReferenceArticleNotInDB(analysisArticleJoin)) {
                 taskChain.addTaskLink( analysisArticleJoinDataUpdater.new ReferenceArticleUpdater( ) );
             }
     /*B1c*/ taskChain.addTaskLink( analysisArticleJoinDataUpdater.new ReferenceArticleEanUpdater( ) );
     /*B1d*/ taskChain.addTaskLink( analysisArticleJoinDataUpdater.new CompetitorPriceUpdater( ) );
         }
/*B1b*/
    }

    public boolean isReferenceArticleNotInDB(AnalysisArticleJoin analysisArticleJoin) {
        if (analysisArticleJoin.getReferenceArticleId() == null) {
            return true;
        }
        return analysisArticleJoin.getReferenceArticleId() < 1;
    }

    public boolean isReferenceArticleEanInDB(AnalysisArticleJoin analysisArticleJoin) {
        if (analysisArticleJoin.getReferenceArticleEanCodeId() == null) {
            return false;
        }
        return analysisArticleJoin.getReferenceArticleEanCodeId() > 0;
    }

    public boolean isReferenceArticleEanCleared(AnalysisArticleJoin analysisArticleJoin) {
        return analysisArticleJoin.isReferenceArticleEanNotSet();
    }

}