package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.MainActivity;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;

public class AnalysisArticleJoinAdapter extends PagedListAdapter<AnalysisArticleJoin, AnalysisArticleJoinAdapter.AnalysisArticleJoinViewHolder> {

    public AnalysisArticleJoinAdapter( AnalysisArticleJoinDiffCalback analysisArticleJoinDiffCalback ){
        super( analysisArticleJoinDiffCalback );
    }

    @Override
    public void onBindViewHolder(@NonNull AnalysisArticleJoinViewHolder holder, int position) {
        AnalysisArticleJoin analysisArticleJoin = getItem(position);
        if (analysisArticleJoin == null) {
            holder.clear();
        } else {
            holder.bind(analysisArticleJoin);
        }
    }

    @NonNull
    @Override
    public AnalysisArticleJoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.analysis_article_item, parent, false);
        return new AnalysisArticleJoinViewHolder( view );
    }

    class AnalysisArticleJoinViewHolder extends RecyclerView.ViewHolder {

        AnalysisArticleJoin analysisArticleJoin;
        private AnalysisArticleJoinViewModel analysisArticleJoinViewModel;
        private final TextView textViewArticleName;
        private final TextView textViewArticleOwnCode;
        private final TextView textViewArticleEanCode;

        public AnalysisArticleJoinViewHolder(View view ) {
            super(view);
            textViewArticleName = view.findViewById(R.id.analysisArticleItem_articleName);
            textViewArticleOwnCode = view.findViewById(R.id.analysisArticleItem_ownCode);
            textViewArticleEanCode = view.findViewById(R.id.analysisArticleItem_eanCode);
            view.setOnClickListener( (View v) -> {
                openAnalysisArticle( v );
            });
        }

        private void openAnalysisArticle( View view) {
            analysisArticleJoinViewModel = ViewModelProviders.of( (MainActivity)itemView.getContext() ).get( AnalysisArticleJoinViewModel.class );
            analysisArticleJoinViewModel.setAnalysisArticleJoin( getItem( getAdapterPosition() ) );
            // Navigation.findNavController( view ).navigate(R.id.action_analysisFragment_to_analysisArticleFragment);
            Navigation.findNavController( view ).navigate(R.id.action_analysisFragment_to_analysisArticlesPagerFragment);
        }

        protected void bind( AnalysisArticleJoin analysisArticleJoin ) {
            textViewArticleName.setText( analysisArticleJoin.getArticleName() );
            textViewArticleOwnCode.setText( analysisArticleJoin.getOwnCode() );
            textViewArticleEanCode.setText( analysisArticleJoin.getEanCode() );
        }

        protected void clear() {
            textViewArticleName.setText( null );
            textViewArticleOwnCode.setText( null );
            textViewArticleEanCode.setText( null );
        }

    }
}

