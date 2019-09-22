package com.dev4lazy.pricecollector.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Store;

public class StoreAdapter extends PagedListAdapter<Store, StoreAdapter.StoreViewHolder> {

        private StoreDiffCallback storeDiffCallback = null;

        public StoreAdapter( StoreDiffCallback storeDiffCalback){
            super(storeDiffCalback);
            this.storeDiffCallback = storeDiffCalback;
        }

        @Override
        public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
            Store store = getItem(position);
            if (store == null) {
                holder.clear();
            } else {
                holder.bind(store);
            }
        }

        @NonNull
        @Override
        public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item,parent, false);
            return new StoreViewHolder( view );
        }

        class StoreViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewStoreId;
            private TextView textViewStoreName;

            public StoreViewHolder( View view ) {
                super(view);
                textViewStoreId = view.findViewById(R.id.store_id);
                textViewStoreName = view.findViewById(R.id.store_name);
            }

            protected void bind(Store store) {
                textViewStoreId.setText(String.valueOf(store.getId()));
                textViewStoreName.setText(store.getName());
            }

            protected void clear() {
                textViewStoreId.setText(null);
                textViewStoreName.setText(null);
            }

        }
    }
