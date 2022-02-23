package com.dev4lazy.pricecollector.test_view.remote_departments_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteDepartment;

public class RemoteDepartmentAdapter extends PagedListAdapter<RemoteDepartment, RemoteDepartmentAdapter.RemoteDepartmentViewHolder> {

    private RemoteDepartmentDiffCallback remoteDepartmentDiffCallback = null;

    public RemoteDepartmentAdapter( RemoteDepartmentDiffCallback remoteDepartmentDiffCalback){
        super( remoteDepartmentDiffCalback );
        this.remoteDepartmentDiffCallback = remoteDepartmentDiffCalback;
    }

    @Override
    public void onBindViewHolder( @NonNull RemoteDepartmentViewHolder holder, int position) {
        RemoteDepartment remoteDepartment = getItem(position);
        if ( remoteDepartment == null) {
            holder.clear();
        } else {
            holder.bind( remoteDepartment );
        }
    }

    @NonNull
    @Override
    public RemoteDepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.remote_department_item, parent, false);
        return new RemoteDepartmentViewHolder( view );
    }

    class RemoteDepartmentViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewRemoteDepartmentName;

        public RemoteDepartmentViewHolder( View view ) {
            super(view);
            textViewRemoteDepartmentName = view.findViewById(R.id.remote_department_name);
        }

        protected void bind(RemoteDepartment remoteDepartment) {
            textViewRemoteDepartmentName.setText(remoteDepartment.getName());
        }

        protected void clear() {
            textViewRemoteDepartmentName.setText(null);
        }

    }
}
