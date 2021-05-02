package com.dev4lazy.pricecollector.remote_view_viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteSector;

public class RemoteSectorAdapter extends PagedListAdapter<RemoteSector, RemoteSectorAdapter.RemoteSectorViewHolder> {

        private RemoteSectorDiffCallback remoteSectorDiffCallback = null;

        public RemoteSectorAdapter(RemoteSectorDiffCallback remoteSectorDiffCalback){
            super(remoteSectorDiffCalback);
            this.remoteSectorDiffCallback = remoteSectorDiffCalback;
        }

        @Override
        public void onBindViewHolder(@NonNull RemoteSectorViewHolder holder, int position) {
            RemoteSector remoteSector = getItem(position);
            if (remoteSector == null) {
                holder.clear();
            } else {
                holder.bind(remoteSector);
            }
        }

        @NonNull
        @Override
        public RemoteSectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remote_sector_item,parent, false);
            return new RemoteSectorViewHolder( view );
        }

        class RemoteSectorViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewRemoteSectorName;

            public RemoteSectorViewHolder( View view ) {
                super(view);
                textViewRemoteSectorName = view.findViewById(R.id.remote_sector_name);
            }

            protected void bind(RemoteSector remoteSector) {
                textViewRemoteSectorName.setText(remoteSector.getName());
            }

            protected void clear() {
                textViewRemoteSectorName.setText(null);
            }

        }
    }
