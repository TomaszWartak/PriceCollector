package com.dev4lazy.pricecollector.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.DepartmentInSector;

public class DepartmentInSectorAdapter extends PagedListAdapter<DepartmentInSector, DepartmentInSectorAdapter.DepartmentInSectorViewHolder> {

    private DepartmentInSectorDiffCallback departmentInSectorDiffCallback = null;

    public DepartmentInSectorAdapter( DepartmentInSectorDiffCallback departmentInSectorDiffCalback){
        super( departmentInSectorDiffCalback );
        this.departmentInSectorDiffCallback = departmentInSectorDiffCalback;
    }

    @Override
    public void onBindViewHolder( @NonNull DepartmentInSectorViewHolder holder, int position) {
        DepartmentInSector departmentInSector = getItem(position);
        if ( departmentInSector == null) {
            holder.clear();
        } else {
            holder.bind( departmentInSector );
        }
    }

    @NonNull
    @Override
    public DepartmentInSectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.depts_in_secs_item, parent, false);
        return new DepartmentInSectorViewHolder( view );
    }

    class DepartmentInSectorViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDepartmentInSector_Id;
        private TextView textViewDepartmentInSector_SectorId;
        private TextView textViewDepartmentInSector_DepartmentId;

        public DepartmentInSectorViewHolder( View view ) {
            super(view);
            textViewDepartmentInSector_Id = view.findViewById(R.id.depts_in_secs_id);
            textViewDepartmentInSector_SectorId = view.findViewById(R.id.depts_in_secs_sectorId);
            textViewDepartmentInSector_DepartmentId = view.findViewById(R.id.depts_in_secs_departmentId);
        }

        protected void bind( DepartmentInSector departmentInSector ) {
            textViewDepartmentInSector_Id.setText( "id: "+ departmentInSector.getId());
            textViewDepartmentInSector_SectorId.setText( "Sector id :"+ departmentInSector.getSectorId());
            textViewDepartmentInSector_DepartmentId.setText( "Department id: "+ departmentInSector.getDepartmentId());
        }

        protected void clear() {
            textViewDepartmentInSector_Id.setText( null );
            textViewDepartmentInSector_SectorId.setText( null );
            textViewDepartmentInSector_DepartmentId.setText( null );
        }

    }
}
