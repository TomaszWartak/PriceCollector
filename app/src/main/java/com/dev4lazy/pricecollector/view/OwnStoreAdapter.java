package com.dev4lazy.pricecollector.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.OwnStore;

public class OwnStoreAdapter extends PagedListAdapter<OwnStore, OwnStoreAdapter.OwnStoreViewHolder> {

        private OwnStoreDiffCallback ownStoreDiffCallback = null;

        public OwnStoreAdapter(OwnStoreDiffCallback storeDiffCalback){
            super(storeDiffCalback);
            this.ownStoreDiffCallback = storeDiffCalback;
        }

        @Override
        public void onBindViewHolder(@NonNull OwnStoreViewHolder holder, int position) {
            OwnStore store = getItem(position);
            if (store == null) {
                holder.clear();
            } else {
                holder.bind(store);
            }
        }

        @NonNull
        @Override
        public OwnStoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item,parent, false);
            return new OwnStoreViewHolder( view );
        }

        class OwnStoreViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewOwnStoreId;
            private TextView textViewOwnStoreName;

            public OwnStoreViewHolder( View view ) {
                super(view);
                textViewOwnStoreId = view.findViewById(R.id.store_id);
                textViewOwnStoreName = view.findViewById(R.id.store_name);
            }

            protected void bind(OwnStore store) {
                textViewOwnStoreId.setText(String.valueOf(store.getId()));
                textViewOwnStoreName.setText(store.getName());
            }

            protected void clear() {
                textViewOwnStoreId.setText(null);
                textViewOwnStoreName.setText(null);
            }

        }
    }
