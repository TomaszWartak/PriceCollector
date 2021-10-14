package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.utils.TaskChain;
import com.dev4lazy.pricecollector.view.E5_article_screen.AnalysisArticlesPagerFragment;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;

public class AnalysisArticleJoinSaver {

    private AnalysisArticleJoinDataUpdater analysisArticleJoinDataUpdater;
    private AnalysisArticleJoinViewModel analysisArticleJoinViewModel;

    public AnalysisArticleJoinSaver(
            AnalysisArticleJoinsListViewModel analysisArticleJoinsListViewModel,
            AnalysisArticleJoinViewModel analysisArticleJoinViewModel ) {
        analysisArticleJoinDataUpdater = new AnalysisArticleJoinDataUpdater( analysisArticleJoinsListViewModel );
        this.analysisArticleJoinViewModel = analysisArticleJoinViewModel;
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

    public void startSavingDataChain( AnalysisArticleJoin analysisArticleJoin ) {
        AnalysisArticleJoinValuesStateHolder valuesStateHolder = analysisArticleJoinViewModel.getValuesStateHolder();
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
            savingChangesOfReferenceArticle( analysisArticleJoin, valuesStateHolder, taskChain );
/*B*/   } else {
    /*B1*/  if (valuesStateHolder.isReferenceArticleEanChangedFlagSet()) { /**/
                savingChangesOfRefArtEanIfRefArtIsNotChanged(analysisArticleJoin, valuesStateHolder, taskChain );
    /*B2*/  } else {
        /*B2a*/ if (valuesStateHolder.isCompetitorPriceChangedFlagSet()) {
                    taskChain.addTaskLink(
                            analysisArticleJoinDataUpdater.new CompetitorPriceUpdater(
                                    taskChain,
                                    analysisArticleJoin,
                                    valuesStateHolder
                            )
                    );
                }
            }
        }
  /*C*/ if (valuesStateHolder.isCommentsChangedFlagSet()) {
            taskChain.addTaskLink(
                    analysisArticleJoinDataUpdater.new AnalysisArticleUpdater(
                            taskChain,
                            analysisArticleJoin,
                            valuesStateHolder
                    )

            );
        }
        analysisArticleJoinDataUpdater.getTaskChain().startIt();
    }

    public void savingChangesOfReferenceArticle(
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinValuesStateHolder valuesStateHolder,
            TaskChain taskChain ) {
 /*A1*/ taskChain.addTaskLink(
            analysisArticleJoinDataUpdater.new ReferenceArticleUpdater(
                taskChain,
                analysisArticleJoin,
                valuesStateHolder
            )
        );
        if (valuesStateHolder.isReferenceArticleEanChangedFlagSet()) {
     /*A2*/ savingChangesOfRefArtEanIfRefArtIsChanged( analysisArticleJoin, valuesStateHolder, taskChain );
        } else {
     /*A3*/ savingChangesOfCompetitorPriceIfRefArtIsChanged( analysisArticleJoin, valuesStateHolder, taskChain );
        }
    }

    public void /*A2*/ savingChangesOfRefArtEanIfRefArtIsChanged(
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinValuesStateHolder valuesStateHolder,
            TaskChain taskChain ) {
/*A2a*/ if (isReferenceArticleEanCleared(analysisArticleJoin)) {
            if (isReferenceArticleEanInDB(analysisArticleJoin)) {
                taskChain.addTaskLink(
                        analysisArticleJoinDataUpdater.new ReferenceArticleEanDeleter(
                                taskChain,
                                analysisArticleJoin,
                                valuesStateHolder
                        )
                );
                taskChain.addTaskLink(
                        analysisArticleJoinDataUpdater.new CompetitorPriceUpdater(
                                taskChain,
                                analysisArticleJoin,
                                valuesStateHolder
                        )
                );
            }
/*A2b*/} else {
            taskChain.addTaskLink(
                    analysisArticleJoinDataUpdater.new ReferenceArticleEanUpdater(
                            taskChain,
                            analysisArticleJoin,
                            valuesStateHolder
                    )
            );
            taskChain.addTaskLink(
                    analysisArticleJoinDataUpdater.new CompetitorPriceUpdater(
                            taskChain,
                            analysisArticleJoin,
                            valuesStateHolder
                    )
            );
        }
    }

    public void /*A3*/ savingChangesOfCompetitorPriceIfRefArtIsChanged(
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinValuesStateHolder valuesStateHolder,
            TaskChain taskChain ) {
/*A3a*/ if (valuesStateHolder.isCompetitorPriceChangedFlagSet()) {
            taskChain.addTaskLink(
                    analysisArticleJoinDataUpdater.new CompetitorPriceUpdater(
                            taskChain,
                            analysisArticleJoin,
                            valuesStateHolder
                    )
            );
        } else {
    /*A3b*/  if (isReferenceArticleNotInDB(analysisArticleJoin)) {
                taskChain.addTaskLink(
                        analysisArticleJoinDataUpdater.new CompetitorPriceUpdater(
                                taskChain,
                                analysisArticleJoin,
                                valuesStateHolder
                        )
                );
            }
        }
    }

    public void savingChangesOfRefArtEanIfRefArtIsNotChanged(
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinValuesStateHolder valuesStateHolder,
            TaskChain taskChain ) {
/*B1a*/ if (isReferenceArticleEanCleared(analysisArticleJoin)) {
            if (isReferenceArticleEanInDB(analysisArticleJoin)) {
                taskChain.addTaskLink(
                        analysisArticleJoinDataUpdater.new ReferenceArticleEanDeleter(
                                taskChain,
                                analysisArticleJoin,
                                valuesStateHolder
                        )
                );
                taskChain.addTaskLink(
                        analysisArticleJoinDataUpdater.new CompetitorPriceUpdater(
                                taskChain,
                                analysisArticleJoin,
                                valuesStateHolder
                        )
                );
            }
         } else {
     /*B1b*/ if (isReferenceArticleNotInDB(analysisArticleJoin)) {
                 taskChain.addTaskLink(
                         analysisArticleJoinDataUpdater.new ReferenceArticleUpdater(
                                 taskChain,
                                 analysisArticleJoin,
                                 valuesStateHolder
                         )
                 );
             }
     /*B1c*/ taskChain.addTaskLink(
                     analysisArticleJoinDataUpdater.new ReferenceArticleEanUpdater(
                             taskChain,
                             analysisArticleJoin,
                             valuesStateHolder
                     )
             );
     /*B1d*/ taskChain.addTaskLink(
                     analysisArticleJoinDataUpdater.new CompetitorPriceUpdater(
                             taskChain,
                             analysisArticleJoin,
                             valuesStateHolder
                     )
             );
         }
/*B1b*/
    }
}