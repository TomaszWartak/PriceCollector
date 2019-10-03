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
import com.dev4lazy.pricecollector.model.entities.Store;

public class CompanyAdapter extends PagedListAdapter<Company, CompanyAdapter.CompanyViewHolder> {

    private CompanyDiffCallback companyDiffCallback = null;

    public CompanyAdapter(CompanyDiffCallback companyDiffCalback){
        super(companyDiffCalback);
        this.companyDiffCallback = companyDiffCalback;
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        Company company = getItem(position);
        if (company == null) {
            holder.clear();
        } else {
            holder.bind(company);
        }
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_item,parent, false);
        return new CompanyViewHolder( view );
    }

    class CompanyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCompanyId;
        private TextView textViewCompanyName;

        public CompanyViewHolder( View view ) {
            super(view);
            textViewCompanyId = view.findViewById(R.id.company_id);
            textViewCompanyName = view.findViewById(R.id.company_name);
        }

        protected void bind(Company company) {
            textViewCompanyId.setText(String.valueOf(company.getId()));
            textViewCompanyName.setText(company.getName());
        }

        protected void clear() {
            textViewCompanyId.setText(null);
            textViewCompanyName.setText(null);
        }

    }
}
