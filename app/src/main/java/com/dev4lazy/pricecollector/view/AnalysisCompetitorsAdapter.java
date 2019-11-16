package com.dev4lazy.pricecollector.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;

import java.util.ArrayList;

public class AnalysisCompetitorsAdapter extends ArrayAdapter<CompetitorSlotFullData> {

    // todo? problematyczne (może być null), ale w inny sposób nie mogłem dobrać się do ViewModel
    private Fragment hostFragment;

    public AnalysisCompetitorsAdapter(Context context, Fragment fragment, ArrayList<CompetitorSlotFullData> slots) {
        super(context, 0, slots);
        hostFragment = fragment;
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
        textViewMenu.setVisibility( View.INVISIBLE );

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
                    textViewMenu.setVisibility( View.VISIBLE );
                    setOnTextViewMenuOnClickMenuListener( textViewMenu, competitorSlotFullData);
                    setSlotViewOnClickListener( slotView, new SlotViewWhenStoreChosenOnClickListener( competitorSlotFullData ));
                }
            }
        }
        return slotView;
    }

    private void setSlotViewOnClickListener( View slotView, View.OnClickListener onClickListener ) {
        slotView.setOnClickListener(onClickListener);
    }

    private void addCompetitorStore(View view, CompetitorSlotFullData competitorSlotFullData ) {
        StoreViewModel storeViewModel = ViewModelProviders.of( hostFragment.getActivity() ).get( StoreViewModel.class );
        Store store = new Store();
        store.setCompanyId( competitorSlotFullData.getSlot().getCompanyId() );
        storeViewModel.setData( store );
        storeViewModel.getData().observe( hostFragment.getActivity() /*mainActivity*/, new Observer<Store>() {
            @Override
            public void onChanged(Store modifiedStore) {
                if (modifiedStore.getName()!=null) {
                    MutableLiveData<Long> storeInsertResult = new MutableLiveData<>();
                    Observer<Long> insertingResultObserver = new Observer<Long>() {
                        @Override
                        public void onChanged(Long storeId) {
                            storeInsertResult.removeObserver(this); // this = observer...
                            if ((storeId!=null)&&(storeId>0)) {
                                storeViewModel.getData().removeObservers( hostFragment.getActivity() );
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

    private void setStoresListMenuListener( PopupMenu popupMenu, CompetitorSlotFullData competitorSlotFullData ) {
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

    private class SlotViewWhenNoStoresOnClickListener implements View.OnClickListener {

        private CompetitorSlotFullData competitorSlotFullData;

        SlotViewWhenNoStoresOnClickListener(CompetitorSlotFullData competitorSlotFullData ) {
            this.competitorSlotFullData = competitorSlotFullData;
        }

        @Override
        public void onClick(View view) {
            addCompetitorStore( view, competitorSlotFullData );
        }
    }

    private class SlotViewWhenNoStoreChosenOnClickListener implements View.OnClickListener {

        private CompetitorSlotFullData competitorSlotFullData;

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

    private class SlotViewWhenStoreChosenOnClickListener implements View.OnClickListener {

        private CompetitorSlotFullData competitorSlotFullData;

        SlotViewWhenStoreChosenOnClickListener(CompetitorSlotFullData competitorSlotFullData ) {
            this.competitorSlotFullData = competitorSlotFullData;
        }

        @Override
        public void onClick(View view) {
            Navigation.findNavController( view ).navigate( R.id.action_analysisCompetitorsFragment_to_analysisFragment );
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

    private class SlotViewWhenStoreChosenOnLongClickListener implements View.OnLongClickListener {

        private CompetitorSlotFullData competitorSlotFullData;

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
