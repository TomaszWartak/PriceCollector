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

import com.dev4lazy.pricecollector.MainActivity;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.model.logic.CompetitorSlotFullData;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.utils.AppUtils;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;

import java.util.ArrayList;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
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
    };

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
                        textViewStoreName.setText( competitorSlotFullData.getSlot().getStoreName() );
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

        private void addCompetitorStore(View view, CompetitorSlotFullData competitorSlotFullData ) {
            // todo StoreViewModel storeViewModel = new ViewModelProvider( hostFragment.getActivity() ).get( StoreViewModel.class );
            StoreViewModel storeViewModel = new ViewModelProvider( AppUtils.getActivity(getContext()) ).get( StoreViewModel.class );
            Store store = new Store();
            store.setCompanyId( competitorSlotFullData.getSlot().getCompanyId() );
            storeViewModel.setData( store );
            /**/ storeViewModel.getData().observe( AppUtils.getActivity(getContext()) /*hostFragment.getActivity() /*mainActivity*/, new Observer<Store>() {
                @Override
                public void onChanged(Store modifiedStore) {
                    if (modifiedStore.getName()!=null) {
                        MutableLiveData<Long> storeInsertResult = new MutableLiveData<>();
                        Observer<Long> insertingResultObserver = new Observer<Long>() {
                            @Override
                            public void onChanged(Long storeId) {
                                storeInsertResult.removeObserver(this); // this = observer...
                                if ((storeId!=null)&&(storeId>0)) {
                                    storeViewModel.getData().removeObservers( AppUtils.getActivity(getContext()) /* hostFragment.getActivity() */ );
                                    modifiedStore.setId(storeId.intValue());
                                    competitorSlotFullData.getCompetitorStores().add(modifiedStore);
                                    competitorSlotFullData.getSlot().setStoreName(modifiedStore.getName());
                                    competitorSlotFullData.getSlot().setOtherStoreId(modifiedStore.getId());
                                    notifyDataSetChanged();
                                    AppHandle.getHandle().getRepository().getLocalDataRepository().updateAnalysisCompetitorSlot(competitorSlotFullData.getSlot(),null);
                                }
                            }
                        };
                        storeInsertResult.observeForever(insertingResultObserver);
                        AppHandle.getHandle().getRepository().getLocalDataRepository().insertStore(store,storeInsertResult);
                    }
                }
            });
            Navigation.findNavController( view ).navigate( R.id.action_analysisCompetitorsFragment_to_addStoreDialogFragment );
        }

        private void DriveToComppetitorStore() {

        }

        private void setStoresListMenuListener(PopupMenu popupMenu, CompetitorSlotFullData competitorSlotFullData ) {
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // todo kontynuuj ListView na youTube...

                    AnalysisCompetitorSlot slot = competitorSlotFullData.getSlot();
                    slot.setOtherStoreId( item.getItemId() );
                    slot.setStoreName( competitorSlotFullData.getCompetitorStores().get( item.getOrder() ).getName() );
                    AppHandle.getHandle().getRepository().getLocalDataRepository().updateAnalysisCompetitorSlot( slot,null );
                    notifyDataSetChanged();
                    return true;
                }
            });
        }

        private void createStoresListMenu( View view, CompetitorSlotFullData competitorSlotFullData ) {
            PopupMenu storesListMenu = new PopupMenu(getContext(), view);
            for (int storeNr = 0; storeNr < competitorSlotFullData.getCompetitorStores().size(); storeNr++) {
                storesListMenu.getMenu().add(
                    Menu.NONE,
                    competitorSlotFullData.getCompetitorStores().get(storeNr).getId(),
                    storeNr,
                    competitorSlotFullData.getCompetitorStores().get(storeNr).getName());
            }
            storesListMenu.show();
            setStoresListMenuListener(storesListMenu, competitorSlotFullData);
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
                            break;
                        case R.id.addCompetitorStoreMenuItem:
                            addCompetitorStore( view, competitorSlotFullData );
                            break;
                        case R.id.chooseCompetitorStoreMenuItem:
                            createStoresListMenu( view, competitorSlotFullData );
                            break;
                        case R.id.detachCompetitorStoreMenuItem:
                            slot.setOtherStoreId(AnalysisCompetitorSlot.NONE);
                            slot.setStoreName("");
                            notifyDataSetChanged();
                            break;
                    }
                    return false;
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
            return competitorSlotFullData.getCompetitorStores().size() == 0;
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
                DriveToComppetitorStore();
                return true;
            }
        }

    }
}
