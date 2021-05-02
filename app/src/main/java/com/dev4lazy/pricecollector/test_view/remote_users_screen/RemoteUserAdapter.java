package com.dev4lazy.pricecollector.test_view.remote_users_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteUser;

public class RemoteUserAdapter extends PagedListAdapter<RemoteUser, RemoteUserAdapter.RemoteUserViewHolder> {

        private RemoteUserDiffCallback remoteUserDiffCallback = null;

        public RemoteUserAdapter(RemoteUserDiffCallback remoteUserDiffCalback){
            super(remoteUserDiffCalback);
            this.remoteUserDiffCallback = remoteUserDiffCalback;
        }

        @Override
        public void onBindViewHolder(@NonNull RemoteUserViewHolder holder, int position) {
            RemoteUser remoteUser = getItem(position);
            if (remoteUser == null) {
                holder.clear();
            } else {
                holder.bind(remoteUser);
            }
        }

        @NonNull
        @Override
        public RemoteUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remote_user_item,parent, false);
            return new RemoteUserViewHolder( view );
        }

        class RemoteUserViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewRemoteUserLogin;
            private TextView textViewRemoteUserName;

            public RemoteUserViewHolder( View view ) {
                super(view);
                textViewRemoteUserLogin = view.findViewById(R.id.remote_user_login);
                textViewRemoteUserName = view.findViewById(R.id.remote_user_name);
            }

            protected void bind(RemoteUser remoteUser) {
                textViewRemoteUserLogin.setText(String.valueOf(remoteUser.getLogin()));
                textViewRemoteUserName.setText(remoteUser.getName());
            }

            protected void clear() {
                textViewRemoteUserLogin.setText(null);
                textViewRemoteUserName.setText(null);
            }

        }
    }
