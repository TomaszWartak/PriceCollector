package com.dev4lazy.pricecollector.view.E3_analysis_competitors_List_screen;

import android.content.Context;
import android.content.res.Resources;
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
import com.dev4lazy.pricecollector.model.logic.LocalDataRepository;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.utils.AppUtils;
import com.dev4lazy.pricecollector.viewmodel.CompetitorsSlotsViewModel;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class CompetitorsSlotsListView extends ListView { //OK

    private StoreViewModel storeViewModel;
    private CompetitorsSlotsViewModel competitorsSlotsViewModel;

    public CompetitorsSlotsListView(Context context ) {
        super(context);
    }

    public CompetitorsSlotsListView(Context context, AttributeSet attrs) {
        super( context, attrs);
    }

    public void setup( StoreViewModel storeViewModel, CompetitorsSlotsViewModel competitorsSlotsViewModel ) {
        this.storeViewModel = storeViewModel;
        this.competitorsSlotsViewModel = competitorsSlotsViewModel;
        ArrayList<CompetitorSlotFullData> emptyList = new ArrayList<>();
        AnalysisCompetitorsAdapter adapter = new AnalysisCompetitorsAdapter( getContext(), emptyList );
        setAdapter( adapter );
    }

    public void submitCompetitorsSlotsList(ArrayList<CompetitorSlotFullData> competitorsSlotsList ) {
        ((AnalysisCompetitorsAdapter)getAdapter()).addAll( competitorsSlotsList );
    }

    public class AnalysisCompetitorsAdapter extends ArrayAdapter<CompetitorSlotFullData> {

        public AnalysisCompetitorsAdapter(Context context, ArrayList<CompetitorSlotFullData> slots) {
            super(context, 0, slots);
        }

        @Override
        public View getView(int position, View slotView, ViewGroup parent) {
            CompetitorSlotFullData competitorSlotFullData = getItem( position );
            if (slotView == null) {
                slotView = LayoutInflater.from(getContext()).inflate( R.layout.analysis_competitor_slot, parent, false );
            }
            TextView textViewCompanyName = slotView.findViewById( R.id.competitor_company_name );
            setTextViewCompanyNameMaxWidth( textViewCompanyName );
            TextView textViewStoreName = slotView.findViewById( R.id.competitor_store_name );
            TextView textViewThreeDotsMenu = slotView.findViewById( R.id.competitor_slot_menu) ;
            textViewThreeDotsMenu.setVisibility( GONE );

            int warningColor = ContextCompat.getColor( AppHandle.getHandle().getApplicationContext(), R.color.colorWarning );

            if (isCompetitorNotChosen(competitorSlotFullData)) {
                textViewCompanyName.setText( "?" );
                textViewCompanyName.setTextColor( warningColor );
                textViewStoreName.setText( getContext().getString(R.string.choose_competitor_store) );
                textViewStoreName.setTextColor( warningColor );
                setSlotViewOnClickListener( slotView, new SlotViewWhenNoStoreChosenOnClickListener( competitorSlotFullData ));
            } else {
                textViewCompanyName.setText( competitorSlotFullData.getSlot().getCompanyName() );
                if (isCompetitorStoreNotChosen(competitorSlotFullData)) {
                    if (isNoCompetitorStores(competitorSlotFullData)) {
                        textViewStoreName.setText(getContext().getString(R.string.add_competitor_store));
                        textViewStoreName.setTextColor(warningColor);
                        setSlotViewOnClickListener(slotView, new SlotViewWhenNoStoresOnClickListener(competitorSlotFullData));
                    } else {
                        textViewStoreName.setText(getContext().getString(R.string.choose_competitor_store));
                        textViewStoreName.setTextColor(warningColor);
                        setSlotViewOnClickListener(slotView, new SlotViewWhenNoStoreChosenOnClickListener(competitorSlotFullData));
                    }
                } else {
                    AnalysisCompetitorSlot slot = competitorSlotFullData.getSlot();
                    textViewStoreName.setText( slot.getStoreName() );
                    textViewStoreName.setTextColor( ContextCompat.getColor( AppHandle.getHandle().getApplicationContext(), R.color.colorSecondaryText) );
                    setOffSlotViewOnClickListener( slotView );
                    textViewThreeDotsMenu.setVisibility( VISIBLE );
                    setOnThreeDotsOnClickMenuListener(textViewThreeDotsMenu, competitorSlotFullData);
                    setSlotViewOnClickListener(
                        slotView,
                        new SlotViewWhenStoreChosenOnClickListener( competitorSlotFullData )
                    );
                }
            }
            return slotView;
        }

        public void setTextViewCompanyNameMaxWidth(TextView textViewCompanyName) {
            int screeWidthInPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
            int textViewCompanyNameMaxWidth = (int)Math.round( screeWidthInPixels *0.35 );
            textViewCompanyName.setMaxWidth( textViewCompanyNameMaxWidth );
        }

        private void setSlotViewOnClickListener( View slotView, OnClickListener onClickListener ) {
            slotView.setOnClickListener(onClickListener);
        }

        private void setThreeDotsPopupMenuListener(
                PopupMenu threeDotsPopupMenu,
                View view,
                CompetitorSlotFullData competitorSlotFullData ) {
            threeDotsPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.makeAnalysisMenuItem:
                            storeViewModel.setStore(competitorSlotFullData.getChosenStore());
                            Navigation.findNavController( view ).navigate( R.id.action_analysisCompetitorsFragment_to_analysisArticlesListFragment);
                            break;
                        case R.id.addCompetitorStoreMenuItem:
                            addCompetitorStore( view, competitorSlotFullData );
                            break;
                        case R.id.editCompetitorStoreMenuItem:
                            editCompetitorStore( view, competitorSlotFullData );
                            break;
                        case R.id.deleteCompetitorStoreMenuItem:
                            deleteCompetitorStore( view, competitorSlotFullData );
                            break;
                        case R.id.chooseCompetitorStoreMenuItem:
                            createStoresListMenu( view, competitorSlotFullData );
                            break;
                        case R.id.detachCompetitorStoreMenuItem:
                            detachCompetitorStore( competitorSlotFullData );
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
            storeViewModel.getData().observe( AppUtils.getActivity(getContext()), new Observer<Store>() {
                @Override
                public void onChanged( Store newStore ) {
                    if (storeViewModel.isOnChangedReactionAllowed()) {
                        if (newStore.getName() != null) {
                            MutableLiveData<Long> storeInsertResult = new MutableLiveData<>();
                            storeInsertResult.observe(AppUtils.getActivity(getContext()), new Observer<Long>() {
                                @Override
                                public void onChanged( Long addedStoreId) {
                                    storeInsertResult.removeObserver(this); // this = observer...
                                    if ((addedStoreId != null) && (addedStoreId > 0)) {
                                        storeViewModel.getData().removeObservers( AppUtils.getActivity(getContext()) );
                                        AnalysisCompetitorSlot competitorSlot = competitorSlotFullData.getSlot();
                                        Store previousStore = competitorSlotFullData.getChosenStore();
                                        if (previousStore!=null) {
                                            /* Jeśli dodanie sklepu dokonało się dla jednego z lokalnych konkurentów,
                                            gdzie jakiś sklep był juz wybrany (previousStore!=null),
                                            to dodanie sklepu spowodowało odpięcie tego wcześniej wybranego.
                                            W takiej sytuacji u drugiego lokalnego konkurenta
                                            trzeba dodać ten odpięty sklep.
                                         */
                                            switch (competitorSlot.getSlotNr()) {
                                                case 4: {
                                                    competitorsSlotsViewModel.getCompetitorsSlotsFullData().get(4).addStore(previousStore);
                                                    break;
                                                }
                                                case 5: {
                                                    competitorsSlotsViewModel.getCompetitorsSlotsFullData().get(3).addStore(previousStore);
                                                    break;
                                                }
                                            }
                                        }
                                        newStore.setId( addedStoreId.intValue() );
                                        competitorSlotFullData.setChosenStore(newStore);
                                        competitorSlotFullData.addStore(newStore);
                                        competitorSlot.setStoreName(newStore.getName());
                                        competitorSlot.setOtherStoreId(newStore.getId());
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
            storeViewModel.getData().observe( AppUtils.getActivity(getContext()), new Observer<Store>() {
                @Override
                public void onChanged(Store modifiedStore) {
                    if (storeViewModel.isOnChangedReactionAllowed()) {
                        if (modifiedStore.getName() != null) {
                            MutableLiveData<Integer> storeUpdateResult = new MutableLiveData<>();
                            storeUpdateResult.observe(AppUtils.getActivity(getContext()), new Observer<Integer>() {
                                @Override
                                public void onChanged(Integer storeId) {
                                    storeUpdateResult.removeObserver(this); // this = observer...
                                    if ((storeId != null) && (storeId > 0)) {
                                        storeViewModel.getData().removeObservers(AppUtils.getActivity(getContext()) );
                                        AnalysisCompetitorSlot competitorSlot = competitorSlotFullData.getSlot();
                                        competitorSlot.setStoreName( modifiedStore.getName() );
                                         /* Jeśli modyfikacja sklepu dokonało się dla jednego z lokalnych konkurentów
                                        (sloty 4 i 5), to u drugiego lokalnego konkurenta nie trzeba,
                                        bo aby zmodyfikować sklep musi być najpierw przypięty.
                                        A jesli jest przypiety, to nie ma go na u drugiego.
                                        Dlatego nie robisz z tym nic... :-) */
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

        private void deleteCompetitorStore(View view, CompetitorSlotFullData competitorSlotFullData ) {
            StoreViewModel storeViewModel = new ViewModelProvider( AppUtils.getActivity(getContext()) ).get( StoreViewModel.class );
            storeViewModel.setOnChangedReactionNotAllowed();
            Store store = competitorSlotFullData.getChosenStore();
            storeViewModel.setStore( store );
            storeViewModel.getData().observe( AppUtils.getActivity(getContext()), new Observer<Store>() {
                @Override
                public void onChanged(Store modifiedStore) {
                    if (storeViewModel.isOnChangedReactionAllowed()) {
                        if (modifiedStore.getId() > -1) {
                            MutableLiveData<Integer> storeDeleteResult = new MutableLiveData<>();
                            storeDeleteResult.observe(AppUtils.getActivity(getContext()), new Observer<Integer>() {
                                @Override
                                public void onChanged(Integer deletedCount) {
                                    storeDeleteResult.removeObserver(this); // this = observer...
                                    if ((deletedCount != null) && (deletedCount > 0)) {
                                        storeViewModel.getData().removeObservers(AppUtils.getActivity(getContext()) /* hostFragment.getActivity() */);
                                        competitorSlotFullData.setChosenStore(null);
                                        competitorSlotFullData.removeStore(modifiedStore);
                                        competitorSlotFullData.getSlot().reset();
                                        /* Jeśli usunięcie sklepu dokonało się dla jednego z lokalnych konkurentów
                                        (sloty 4 i 5), to u drugiego lokalnego konkurenta nie trzeba,
                                        bo aby usunąć sklep musi być najpierw przypięty.
                                        A jesli jest przypiety, to nie ma go na u drugiego.
                                        Dlatego nie robisz z tym nic... :-) */
                                        notifyDataSetChanged();
                                        AppHandle.getHandle().getRepository().getLocalDataRepository().updateAnalysisCompetitorSlot(competitorSlotFullData.getSlot(), null);
                                    }
                                }
                            });
                            AppHandle.getHandle().getRepository().getLocalDataRepository().deleteStore(store, storeDeleteResult);
                        }
                    } else {
                        storeViewModel.setOnChangedReactionAllowed();
                    }
                }
            });
            Navigation.findNavController( view ).navigate( R.id.action_analysisCompetitorsFragment_to_deleteStoreDialogFragment );
        }

        private void createStoresListMenu( View view, CompetitorSlotFullData competitorSlotFullData ) {
            Context contextThemeWrapper = new ContextThemeWrapper( getContext(), R.style.PC_PopupMenuStyle);
            PopupMenu storesListMenu = new PopupMenu( contextThemeWrapper, view);
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
                    Store previousChosenStore = competitorSlotFullData.getChosenStore();
                    Store chosenStore = competitorSlotFullData.getStore( item.getItemId() );
                    competitorSlotFullData.setChosenStore( chosenStore );
                    AnalysisCompetitorSlot slot = competitorSlotFullData.getSlot();
                    slot.setOtherStoreId( chosenStore.getId() );
                    slot.setStoreName( chosenStore.getName() );
                    LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                    localDataRepository.updateAnalysisCompetitorSlot( slot,null );
                    if (chosenStore!=null) {
                        competitorSlotFullData.removeStore( chosenStore );
                    }
                    /* Jeśli wybór sklepu dokonał się dla jednego z lokalnych konkurentów (sloty 4 i 5),
                       to u drugiego lokalnego konkurenta powinien zniknąc z listy.
                       Jeśli w slocie wczesniej był wybrany sklep, to musi się pojawić na liście
                       w drugim slocie */
                    CompetitorSlotFullData competitorSlotFullData_No4 = competitorsSlotsViewModel.getCompetitorsSlotsFullData().get(3);
                    CompetitorSlotFullData competitorSlotFullData_No5 = competitorsSlotsViewModel.getCompetitorsSlotsFullData().get(4);
                    switch (slot.getSlotNr()) {
                        case 4: {
                            competitorSlotFullData_No5.removeStore(chosenStore);
                            if (previousChosenStore!=null) {
                                competitorSlotFullData_No5.addStore(previousChosenStore);
                            }
                            break;
                        }
                        case 5: {
                            competitorSlotFullData_No4.removeStore(chosenStore);
                            if (previousChosenStore!=null) {
                                competitorSlotFullData_No4.addStore(previousChosenStore);
                            }
                            break;
                        }
                    }
                    notifyDataSetChanged();
                    return true;
                }
            });
        }

        public void detachCompetitorStore( CompetitorSlotFullData competitorSlotFullData ) {
            AnalysisCompetitorSlot analysisCompetitorSlot = competitorSlotFullData.getSlot();
            Store storeToDetach = competitorSlotFullData.getChosenStore();
            /*
            Ponieważ wybór sklepu usuwa go z listy dostepnych sklepów, to odpięcie powinno
            powodować dodanie z powrotem do listy.
            */
            competitorSlotFullData.addStore(storeToDetach);
            /*
            Jeśli odpięcie sklepu dokonało się u jednego z lokalnych konkurentów (sloty 4 i 5),
            to u drugiego lokalnego konkurenta ten sklep powinien również pojawić się na liście.
            */
            switch (analysisCompetitorSlot.getSlotNr()) {
                case 4: {
                    competitorsSlotsViewModel.getCompetitorsSlotsFullData().get(4).addStore(storeToDetach);
                    break;
                }
                case 5: {
                    competitorsSlotsViewModel.getCompetitorsSlotsFullData().get(3).addStore(storeToDetach);
                    break;
                }
            }
            analysisCompetitorSlot.setOtherStoreId( AnalysisCompetitorSlot.NONE );
            analysisCompetitorSlot.setStoreName("");
            competitorSlotFullData.setNoChosenStore();
            notifyDataSetChanged();
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
                storeViewModel.setStore(competitorSlotFullData.getChosenStore());
                Navigation.findNavController( view ).navigate( R.id.action_analysisCompetitorsFragment_to_analysisArticlesListFragment);
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

        private void setOnThreeDotsOnClickMenuListener(View textView, CompetitorSlotFullData competitorSlotFullData) {
            textView.setOnClickListener((View view) -> {
                Context contextThemeWrapper = new ContextThemeWrapper( getContext(), R.style.PC_PopupMenuStyle);
                PopupMenu threeDotsPopupMenu = new PopupMenu( contextThemeWrapper, view);
                MenuCompat.setGroupDividerEnabled( threeDotsPopupMenu.getMenu(), true);
                threeDotsPopupMenu.inflate(R.menu.competitor_slot_popup_menu);
                setThreeDotsPopupMenuListener( threeDotsPopupMenu, view, competitorSlotFullData );
                MenuItem chooseCompetitorStoreMenuItem = threeDotsPopupMenu.getMenu().findItem(R.id.chooseCompetitorStoreMenuItem);
                if (isNoCompetitorStores(competitorSlotFullData)) {
                    chooseCompetitorStoreMenuItem.setVisible(false);
                } else {
                    chooseCompetitorStoreMenuItem.setVisible(true);
                }
                threeDotsPopupMenu.show();
            });
        }


        private class SlotViewWhenStoreChosenOnLongClickListener implements OnLongClickListener {

            private CompetitorSlotFullData competitorSlotFullData;

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
