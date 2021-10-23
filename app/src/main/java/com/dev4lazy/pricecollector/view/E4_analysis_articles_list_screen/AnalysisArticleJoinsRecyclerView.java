package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.MainActivity;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;

public class AnalysisArticleJoinsRecyclerView extends RecyclerView {

    public AnalysisArticleJoinsRecyclerView(@NonNull Context context) {
        super(context);
    }

    public AnalysisArticleJoinsRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
    }

    public void setup() {
        setLayoutManager( new LinearLayoutManager(getContext()));
        addItemDecoration( new DividerItemDecoration(getContext(), VERTICAL));
        setAdapter( new AnalysisArticleJoinAdapter(new AnalysisArticleJoinDiffCallback()) );
    }

    public void submitArticlesList( PagedList<AnalysisArticleJoin> analysisArticlesJoins ) {
        ((AnalysisArticleJoinAdapter)getAdapter()).submitList(analysisArticlesJoins);
    }

    public void scrollToItem( int position ) {
        // TODO setItemHighlited( position )
        //  if (itemIsNotOnScreen( position ) {
        //     scrollToCenterOfPage( int position )
        //  }

    }

    private class AnalysisArticleJoinAdapter extends PagedListAdapter<AnalysisArticleJoin, AnalysisArticleJoinAdapter.AnalysisArticleJoinViewHolder> {

        private AnalysisArticleJoinViewModel analysisArticleJoinViewModel;

        public AnalysisArticleJoinAdapter(AnalysisArticleJoinDiffCallback analysisArticleJoinDiffCallback){
            super(analysisArticleJoinDiffCallback);
            analysisArticleJoinViewModel = new ViewModelProvider( (MainActivity)getContext() ).get( AnalysisArticleJoinViewModel.class );
        }

        @Override
        public void onBindViewHolder(@NonNull AnalysisArticleJoinAdapter.AnalysisArticleJoinViewHolder holder, int position) {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.analysis_article_list_item, parent, false);
            return new AnalysisArticleJoinViewHolder( view );
        }

        class AnalysisArticleJoinViewHolder extends ViewHolder {

            private LinearLayout linearLayout;
            private final TextView textViewArticleName;
            private final TextView textViewArticleOwnCode;
            private final TextView textViewArticleEanCode;

            public AnalysisArticleJoinViewHolder(View view ) {
                super(view);
                linearLayout = view.findViewById(R.id.analysisArticleItem_layout);
                textViewArticleName = view.findViewById(R.id.analysisArticleItem_articleName);
                textViewArticleOwnCode = view.findViewById(R.id.analysisArticleItem_ownCode);
                textViewArticleEanCode = view.findViewById(R.id.analysisArticleItem_eanCode);
                view.setOnClickListener( (View v) -> {
                    openAnalysisArticle( v );
                });
            }

            private void openAnalysisArticle( View view) {
                analysisArticleJoinViewModel.setAnyArticleDisplayed( true );
                analysisArticleJoinViewModel.setAnalysisArticleJoin( getItem( getAdapterPosition() ) );
                analysisArticleJoinViewModel.setPositionOnList( getAdapterPosition() );
                RecyclerView.LayoutManager layoutManager = getLayoutManager();
                analysisArticleJoinViewModel.setFirstVisibleItemPosition( ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition() );
                analysisArticleJoinViewModel.setLastVisibleItemPosition( ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition() );
                Navigation.findNavController( view ).navigate(R.id.action_analysisArticlesListFragment_to_analysisArticlesPagerFragment);
            }

            protected void bind( AnalysisArticleJoin analysisArticleJoin ) {
                if (isCompetitorPriceSet(analysisArticleJoin.getCompetitorStorePrice())) {
                    setItemHighlighted();
                } else {
                    setItemNotHighlighted();
                }
                if (isArticleLastVisited( analysisArticleJoin )) {
                    setINameItemTypefaceBold();
                } else {
                    setINameItemTypefaceNormal();
                }
                showItemData(analysisArticleJoin);
            }

            private boolean isCompetitorPriceSet( Double competitorPrice) {
                return competitorPrice!=null && competitorPrice>0.0;
            }

            private void setItemHighlighted() {
                linearLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
            }

            private void setItemNotHighlighted() {
                linearLayout.setBackgroundColor(Color.WHITE);
            }

            private boolean isArticleLastVisited( AnalysisArticleJoin analysisArticleJoin ) {
                // TODO TEST
                int positionOnList = analysisArticleJoinViewModel.getPositionOnList();
                int adapterPosition = getAdapterPosition();
                // TODO END TEST
                return
                        ( analysisArticleJoinViewModel!=null ) &&
                        analysisArticleJoinViewModel.isAnyArticleDisplayed() && //TODO <- ta nazwa nie oddaje funkcjonalności
                        // TODO XXX( getAdapterPosition()==analysisArticleJoinViewModel.getPositionOnList() );
                        ( analysisArticleJoin.getAnalysisArticleId() == analysisArticleJoinViewModel.getAnalysisArticleJoin().getAnalysisArticleId() );
            }

            private void setINameItemTypefaceBold() {
                textViewArticleName.setTypeface(null, Typeface.BOLD);
            }

            private void setINameItemTypefaceNormal() {
                textViewArticleName.setTypeface(null, Typeface.NORMAL);
            }


            private void showItemData(AnalysisArticleJoin analysisArticleJoin) {
                textViewArticleName.setText(analysisArticleJoin.getArticleName());
                textViewArticleOwnCode.setText(analysisArticleJoin.getOwnCode());
                textViewArticleEanCode.setText(analysisArticleJoin.getEanCode());
            }

            protected void clear() {
                textViewArticleName.setText( null );
                textViewArticleOwnCode.setText( null );
                textViewArticleEanCode.setText( null );
            }

        } //  class AnalysisArticleJoinViewHolder
    } // class AnalysisArticleJoinAdapter22
}
