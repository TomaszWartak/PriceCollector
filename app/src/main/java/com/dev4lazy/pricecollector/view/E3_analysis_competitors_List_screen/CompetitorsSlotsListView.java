package com.dev4lazy.pricecollector.view.E3_analysis_competitors_List_screen;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.model.logic.CompetitorSlotFullData;
import com.dev4lazy.pricecollector.utils.Action;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.utils.AppUtils;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class CompetitorsSlotsListView extends ListView {

    public CompetitorsSlotsListView(Context context) {
        super(context);
    }

    public CompetitorsSlotsListView(Context context, AttributeSet attrs) {
        super( context, attrs);
    }

    public void setup() {
        ArrayList<CompetitorSlotFullData> emptyList = new ArrayList<>();
        AnalysisCompetitorsAdapter adapter = new AnalysisCompetitorsAdapter( getContext(),/* todo this ,*/ emptyList );
        setAdapter( adapter );
    }

    public void submitCompetitorsSlotsList(ArrayList<CompetitorSlotFullData> competitorsSlotsList ) {
        ((AnalysisCompetitorsAdapter)getAdapter()).addAll( competitorsSlotsList );
    }

    public static class AnalysisCompetitorsAdapter extends ArrayAdapter<CompetitorSlotFullData> {

        public AnalysisCompetitorsAdapter(Context context, ArrayList<CompetitorSlotFullData> slots) {
            super(context, 0, slots);
        }

        @Override
        public View getView(int position, View slotView, ViewGroup parent) {
            // Get the data item for this position
            CompetitorSlotFullData competitorSlotFullData = getItem( position );
            // Check if an existing view is being reused, otherwise inflate the view
            if (slotView == null) {
                slotView = LayoutInflater.from(getContext()).inflate( R.layout.analysis_competitor_slot, parent, false );
            }
            TextView textViewCompanyName = slotView.findViewById( R.id.competitor_company_name );
            TextView textViewStoreName = slotView.findViewById( R.id.competitor_store_name );
            TextView textViewMenu = slotView.findViewById( R.id.competitor_slot_menu) ;
            textViewMenu.setVisibility(INVISIBLE);

            int warningColor = ContextCompat.getColor( AppHandle.getHandle().getApplicationContext(), R.color.colorWarning );

            if (isCompetitorNotChosen(competitorSlotFullData)) {
                textViewCompanyName.setText( "?" );
                textViewCompanyName.setTextColor( warningColor );
                textViewStoreName.setText( getContext().getString(R.string.choose_competitor_store) );
                textViewStoreName.setTextColor( warningColor );
                setSlotViewOnClickListener( slotView, new SlotViewWhenNoStoreChosenOnClickListener( competitorSlotFullData ));
            } else {
                textViewCompanyName.setText( "  " + competitorSlotFullData.getSlot().getCompanyName() + "  " );
                if (isNoCompetitorStores(competitorSlotFullData)) {
                    textViewStoreName.setText( getContext().getString( R.string.add_competitor_store ));
                    textViewStoreName.setTextColor( warningColor );
                    setSlotViewOnClickListener( slotView, new SlotViewWhenNoStoresOnClickListener( competitorSlotFullData ));
                } else {
                    if (isCompetitorStoreNotChosen(competitorSlotFullData)) {
                        textViewStoreName.setText( getContext().getString(R.string.choose_competitor_store ));
                        textViewStoreName.setTextColor( warningColor);
                        setSlotViewOnClickListener( slotView, new SlotViewWhenNoStoreChosenOnClickListener( competitorSlotFullData ));
                    } else {
                        AnalysisCompetitorSlot slot = competitorSlotFullData.getSlot();
                        competitorSlotFullData.setChosenStore( competitorSlotFullData.getStore( slot.getOtherStoreId() ) );
                        textViewStoreName.setText( slot.getStoreName() );
                        textViewStoreName.setTextColor( ContextCompat.getColor( AppHandle.getHandle().getApplicationContext(), R.color.colorSecondaryText) );
                        setOffSlotViewOnClickListener( slotView );
                        textViewMenu.setVisibility(VISIBLE);
                        setOnTextViewMenuOnClickMenuListener( textViewMenu, competitorSlotFullData);
                        setSlotViewOnClickListener( slotView, new SlotViewWhenStoreChosenOnClickListener( competitorSlotFullData ));
                    }
                }
            }
            return slotView;
        }

        private void setSlotViewOnClickListener( View slotView, OnClickListener onClickListener ) {
            slotView.setOnClickListener(onClickListener);
        }

        private void setPopupMenuListener(PopupMenu popupMenu, View view, CompetitorSlotFullData competitorSlotFullData) {
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    AnalysisCompetitorSlot slot = competitorSlotFullData.getSlot();
                    switch (item.getItemId()) {
                        case R.id.priceAnalysisMenuItem:
                            Navigation.findNavController( view ).navigate( R.id.action_analysisCompetitorsFragment_to_analysisArticlesListFragment);
                            break;
                        case R.id.driveToCompetitorStoreMenuItem:
                            DriveToCompetitorStore();
                            break;
                        case R.id.addCompetitorStoreMenuItem:
                            addCompetitorStore( view, competitorSlotFullData );
                            break;
                        case R.id.editCompetitorStoreMenuItem:
                            editCompetitorStore( view, competitorSlotFullData );
                            break;
                        case R.id.chooseCompetitorStoreMenuItem:
                            createStoresListMenu( view, competitorSlotFullData );
                            break;
                        case R.id.detachCompetitorStoreMenuItem:
                            competitorSlotFullData.setNoChosenStore();
                            slot.setOtherStoreId(AnalysisCompetitorSlot.NONE);
                            slot.setStoreName("");
                            notifyDataSetChanged();
                            break;
                    }
                    return false;
                }
            });
        }


        private void DriveToCompetitorStore() {

        }

        private void addCompetitorStore(View view, CompetitorSlotFullData competitorSlotFullData ) {
            StoreViewModel storeViewModel = new ViewModelProvider( AppUtils.getActivity(getContext()) ).get( StoreViewModel.class );
            storeViewModel.setOnChangedReactionNotAllowed();
            Store store = new Store();
            store.setCompanyId( competitorSlotFullData.getSlot().getCompanyId() );
            storeViewModel.setStore( store );
            storeViewModel.setActionToDo(Action.ADD);
            storeViewModel.getData().observe( AppUtils.getActivity(getContext()), new Observer<Store>() {
                @Override
                public void onChanged(Store newStore) {
                    if (storeViewModel.isOnChangedReactionAllowed()) {
                        if (newStore.getName() != null) {
                            MutableLiveData<Long> storeInsertResult = new MutableLiveData<>();
                            storeInsertResult.observe(AppUtils.getActivity(getContext()), new Observer<Long>() {
                                @Override
                                public void onChanged(Long storeId) {
                                    storeInsertResult.removeObserver(this); // this = observer...
                                    if ((storeId != null) && (storeId > 0)) {
                                        storeViewModel.getData().removeObservers(AppUtils.getActivity(getContext()));
                                        newStore.setId(storeId.intValue());
                                        competitorSlotFullData.setChosenStore(newStore);
                                        competitorSlotFullData.addStore(newStore);
                                        competitorSlotFullData.getSlot().setStoreName(newStore.getName());
                                        competitorSlotFullData.getSlot().setOtherStoreId(newStore.getId());
                                        notifyDataSetChanged();
                                        AppHandle.getHandle().getRepository().getLocalDataRepository().updateAnalysisCompetitorSlot(competitorSlotFullData.getSlot(), null);
                                    }
                                }
                            });
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertStore(store, storeInsertResult);
                        }
                    } else {
                        storeViewModel.setOnChangedReactionAllowed();
                    }
                }
            });
            Navigation.findNavController( view ).navigate( R.id.action_analysisCompetitorsFragment_to_addStoreDialogFragment );
        }

        private void editCompetitorStore(View view, CompetitorSlotFullData competitorSlotFullData ) {
            StoreViewModel storeViewModel = new ViewModelProvider( AppUtils.getActivity(getContext()) ).get( StoreViewModel.class );
            storeViewModel.setOnChangedReactionNotAllowed();
            Store store = competitorSlotFullData.getChosenStore();
            storeViewModel.setStore( store );
            storeViewModel.setActionToDo(Action.MODIFY);
            storeViewModel.getData().observe( AppUtils.getActivity(getContext()), new Observer<Store>() {
                @Override
                public void onChanged(Store modifiedStore) {
                    if (storeViewModel.isOnChangedReactionAllowed()) {
                        if (modifiedStore.getName() != null) {
                            MutableLiveData<Integer> storeUpdateResult = new MutableLiveData<>();
                            // TOD XXX Observer<Integer> updatingResultObserver =
                            storeUpdateResult.observe(AppUtils.getActivity(getContext()), new Observer<Integer>() {
                                @Override
                                public void onChanged(Integer storeId) {
                                    storeUpdateResult.removeObserver(this); // this = observer...
                                    if ((storeId != null) && (storeId > 0)) {
                                        storeViewModel.getData().removeObservers(AppUtils.getActivity(getContext()) /* hostFragment.getActivity() */);
                                        competitorSlotFullData.getSlot().setStoreName(modifiedStore.getName());
                                        notifyDataSetChanged();
                                        AppHandle.getHandle().getRepository().getLocalDataRepository().updateAnalysisCompetitorSlot(competitorSlotFullData.getSlot(), null);
                                    }
                                }
                            });
                            AppHandle.getHandle().getRepository().getLocalDataRepository().updateStore(store, storeUpdateResult);
                        }
                    } else {
                        storeViewModel.setOnChangedReactionAllowed();
                    }
                }
            });
            Navigation.findNavController( view ).navigate( R.id.action_analysisCompetitorsFragment_to_editStoreDialogFragment );
        }

        private void createStoresListMenu( View view, CompetitorSlotFullData competitorSlotFullData ) {
            PopupMenu storesListMenu = new PopupMenu(getContext(), view);
            /* TODO XXX
            for (int storeNr = 0; storeNr < competitorSlotFullData.getCompetitorStores().size(); storeNr++) {
                storesListMenu.getMenu().add(
                        Menu.NONE,
                        competitorSlotFullData.getCompetitorStores().get(storeNr).getId(),
                        storeNr,
                        competitorSlotFullData.getCompetitorStores().get(storeNr).getName());
            }

             */
            HashMap<Integer, Store> stores = competitorSlotFullData.getCompetitorStoresMap();
            int storePositionOnList = 0;
            Store store;
            for (Map.Entry<Integer, Store> entry : stores.entrySet()) {
                store = entry.getValue();
                storesListMenu.getMenu().add(
                        Menu.NONE,
                        store.getId(),
                        storePositionOnList,
                        store.getName());
                storePositionOnList++;
            }

            storesListMenu.show();
            setStoresListMenuListener(storesListMenu, competitorSlotFullData);
        }

        private void setStoresListMenuListener(PopupMenu popupMenu, CompetitorSlotFullData competitorSlotFullData ) {
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // todo XXX Store chosenStore = competitorSlotFullData.getCompetitorStores().get( item.getOrder() );
                    Store chosenStore = competitorSlotFullData.getStore( item.getItemId() );
                    competitorSlotFullData.setChosenStore( chosenStore );
                    AnalysisCompetitorSlot slot = competitorSlotFullData.getSlot();
                    // todo usun slot.setOtherStoreId( item.getItemId() );
                    slot.setOtherStoreId( chosenStore.getId() );
                    slot.setStoreName( chosenStore.getName() );
                    AppHandle.getHandle().getRepository().getLocalDataRepository().updateAnalysisCompetitorSlot( slot,null );
                    notifyDataSetChanged();
                    return true;
                }
            });
        }

        private class SlotViewWhenNoStoresOnClickListener implements OnClickListener {

            private final CompetitorSlotFullData competitorSlotFullData;

            SlotViewWhenNoStoresOnClickListener(CompetitorSlotFullData competitorSlotFullData ) {
                this.competitorSlotFullData = competitorSlotFullData;
            }

            @Override
            public void onClick(View view) {
                addCompetitorStore( view, competitorSlotFullData );
            }
        }

        private class SlotViewWhenNoStoreChosenOnClickListener implements OnClickListener {

            private final CompetitorSlotFullData competitorSlotFullData;

            SlotViewWhenNoStoreChosenOnClickListener(CompetitorSlotFullData competitorSlotFullData ) {
                this.competitorSlotFullData = competitorSlotFullData;
            }

            @Override
            public void onClick(View view) {
                createStoresListMenu( view, competitorSlotFullData );
            }

        }

        private void setOffSlotViewOnClickListener(View slotView ) {
            slotView.setOnClickListener(null);
        }

        private class SlotViewWhenStoreChosenOnClickListener implements OnClickListener {

            private final CompetitorSlotFullData competitorSlotFullData;

            SlotViewWhenStoreChosenOnClickListener(CompetitorSlotFullData competitorSlotFullData ) {
                this.competitorSlotFullData = competitorSlotFullData;
            }

            @Override
            public void onClick(View view) {
                Navigation.findNavController( view ).navigate( R.id.action_analysisCompetitorsFragment_to_analysisArticlesListFragment);
                //todo ? Navigation.findNavController( view ).navigate( R.id.action_analysisCompetitorsFragment_to_analysisArticlesPagerFragment );
            }

        }

        private boolean isCompetitorNotChosen( CompetitorSlotFullData competitorSlotFullData) {
            return competitorSlotFullData.getSlot().getCompanyId() == -1;
        }

        private boolean isCompetitorStoreNotChosen( CompetitorSlotFullData competitorSlotFullData)  {
            return competitorSlotFullData.getSlot().getOtherStoreId() == -1;
        }

        private boolean isNoCompetitorStores( CompetitorSlotFullData competitorSlotFullData) {
            return competitorSlotFullData.getCompetitorStoresMap().size() == 0;
        }

        private void setOnTextViewMenuOnClickMenuListener(View textView, CompetitorSlotFullData competitorSlotFullData) {
            textView.setOnClickListener((View view) -> {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.inflate(R.menu.competitor_slot_popup_menu);
                setPopupMenuListener(popupMenu, view, competitorSlotFullData);
                popupMenu.show();
            });
        }

        private class SlotViewWhenStoreChosenOnLongClickListener implements OnLongClickListener {

            private final CompetitorSlotFullData competitorSlotFullData;

            SlotViewWhenStoreChosenOnLongClickListener(CompetitorSlotFullData competitorSlotFullData ) {
                this.competitorSlotFullData = competitorSlotFullData;
            }

            @Override
            public boolean onLongClick(View v) {
                DriveToCompetitorStore();
                return true;
            }
        }

    }
}
