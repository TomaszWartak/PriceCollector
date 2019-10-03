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

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.utils.AppHandle;

import java.util.ArrayList;

public class AnalysisCompetitorsAdapter extends ArrayAdapter<CompetitorSlotFullData> {

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
        textViewMenu.setVisibility( View.INVISIBLE );
        setOnSlotViewOnClickListener( slotView, competitorSlotFullData);

        int warningColor = ContextCompat.getColor( AppHandle.getHandle().getApplicationContext(), R.color.colorWarning );
        if (isCompetitorNotChosen(competitorSlotFullData)) {
            textViewCompanyName.setText( "?" );
            textViewCompanyName.setTextColor( warningColor );
            textViewStoreName.setText( getContext().getString(R.string.choose_competitor_store) );
            textViewStoreName.setTextColor( warningColor );
        } else {
            textViewCompanyName.setText( "  " + competitorSlotFullData.getSlot().getCompanyName() + "  " );
            if (isNoCompetitorStores(competitorSlotFullData)) {
                textViewStoreName.setText( getContext().getString( R.string.add_competitor_store ));
                textViewStoreName.setTextColor( warningColor );
            } else {
                if (isCompetitorStoreNotChosen(competitorSlotFullData)) {
                    textViewStoreName.setText( getContext().getString(R.string.choose_competitor_store ));
                    textViewStoreName.setTextColor( warningColor);
                } else {
                    textViewStoreName.setText( competitorSlotFullData.getSlot().getStoreName() );
                    textViewStoreName.setTextColor( ContextCompat.getColor( AppHandle.getHandle().getApplicationContext(), R.color.colorSecondaryText) );
                    setOffSlotViewOnClickListener( slotView );
                    textViewMenu.setVisibility( View.VISIBLE );
                    setOnTextViewMenuOnClickMenuListener( textViewMenu, competitorSlotFullData);
                }
            }
        }
        return slotView;
    }

    private void setOnSlotViewOnClickListener(View slotView, CompetitorSlotFullData competitorSlotFullData) {
        if ( !slotView.hasOnClickListeners() ) {
            slotView.setOnClickListener((View view) -> {
                createStoresListMenu( view, competitorSlotFullData );
            });
        }
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

    private void setOffSlotViewOnClickListener(View slotView ) {
        slotView.setOnClickListener(null);
    }

    private void setStoresListMenuListener( PopupMenu popupMenu, CompetitorSlotFullData competitorSlotFullData ) {
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setStore(item, competitorSlotFullData);

                // todo kontynuuj ListView na youTube...
                // todo zapisz zmiany do bazy

                notifyDataSetChanged();
                return true;
            }
        });
    }

    private void setStore( MenuItem item, CompetitorSlotFullData competitorSlotFullData ) {
        AnalysisCompetitorSlot slot = competitorSlotFullData.getSlot();
        slot.setOtherStoreId(item.getItemId());
        slot.setStoreName(competitorSlotFullData.getCompetitorStores().get(item.getOrder()).getName());
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

}
