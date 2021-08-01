package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;

public class AnalysisArticleJoinsRecyclerView extends RecyclerView {

    public AnalysisArticleJoinsRecyclerView(@NonNull Context context) {
        super(context);
    }

    public void setup() {
        setLayoutManager(new LinearLayoutManager(getContext())); // todo ????
        addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        setAdapter(new AnalysisArticleJoinAdapter(new AnalysisArticleJoinDiffCalback()) );
    }

    public void submitArticlesList( PagedList<AnalysisArticleJoin> analysisArticlesJoins ) {
        ((AnalysisArticleJoinAdapter)getAdapter()).submitList(analysisArticlesJoins);
    }
}
