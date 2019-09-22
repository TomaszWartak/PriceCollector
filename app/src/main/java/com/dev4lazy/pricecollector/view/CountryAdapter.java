package com.dev4lazy.pricecollector.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;

public class CountryAdapter extends PagedListAdapter<Country, CountryAdapter.CountryViewHolder> {

        private CountryDiffCallback countryDiffCallback = null;

        public CountryAdapter(CountryDiffCallback countryDiffCalback){
            super(countryDiffCalback);
            this.countryDiffCallback = countryDiffCalback;
        }

        @Override
        public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
            Country country = getItem(position);
            if (country == null) {
                holder.clear();
            } else {
                holder.bind(country);
            }
        }

        @NonNull
        @Override
        public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item,parent, false);
            return new CountryViewHolder( view );
        }

        class CountryViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewCountryId;
            private TextView textViewCountryName;

            public CountryViewHolder( View view ) {
                super(view);
                textViewCountryId = view.findViewById(R.id.country_id);
                textViewCountryName = view.findViewById(R.id.country_name);
            }

            protected void bind(Country country) {
                textViewCountryId.setText(String.valueOf(country.getId()));
                textViewCountryName.setText(country.getName());
            }

            protected void clear() {
                textViewCountryId.setText(null);
                textViewCountryName.setText(null);
            }

        }
    }
