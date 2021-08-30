package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.utils.AppUtils;
import com.dev4lazy.pricecollector.viewmodel.SearchArticlesViewModel;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

public class SearchArticlesByDataDialogFragment extends DialogFragment {

    public static SearchArticlesByDataDialogFragment newInstance() {
        return new SearchArticlesByDataDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.search_articles_by_data_fragment, null);

        EditText articleNameEditText = viewInflated.findViewById(R.id.search_articles_article_name_edit_text);
        EditText articleEANEditText = viewInflated.findViewById(R.id.search_articles_ean_edit_text);
        EditText articleSKUEditText = viewInflated.findViewById(R.id.search_articles_sku_edit_text);
        EditText articleAnyTextEditText = viewInflated.findViewById(R.id.search_articles_any_text_edit_text);

        SearchArticlesViewModel searchArticlesViewModel = new ViewModelProvider(getActivity()).get(SearchArticlesViewModel.class);
        articleNameEditText.setText( searchArticlesViewModel.getArticleName() );
        articleEANEditText.setText( searchArticlesViewModel.getArticleEAN() );
        articleSKUEditText.setText( searchArticlesViewModel.getArticleSKU()) ;
        articleAnyTextEditText.setText( searchArticlesViewModel.getArticleAnyText() );

        return getSearchArticlesByDataDialog(
                viewInflated,
                searchArticlesViewModel,
                articleEANEditText,
                articleSKUEditText,
                articleAnyTextEditText );
    }

    @NonNull
    private AlertDialog getSearchArticlesByDataDialog(
            View viewInflated,
            SearchArticlesViewModel searchArticlesViewModel,
            EditText articleEANEditText,
            EditText articleSKUEditText,
            EditText articleAnyTextEditText) {
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(getContext())
                .setTitle("")
                .setView(viewInflated) // jeśli dialog ma mieć niestandarodowy widok
                .setPositiveButton(R.string.caption_search, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.caption_clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog.cancel();
                    }
                })
                .create();
        return alertDialog;
    }

}
