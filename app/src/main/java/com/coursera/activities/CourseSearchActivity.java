package com.coursera.activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.base.activities.BaseActivity;
import com.coursera.R;
import com.coursera.fragments.SearchContentFragment;
import com.coursera.fragments.SearchHistoryFragment;
import com.coursera.listeners.IClickHistoryListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.utils.operations.HistoryOperations;

import butterknife.OnClick;

/**
 * Created by supermanmwg on 15-11-3.
 */
public class CourseSearchActivity extends BaseActivity implements IClickHistoryListener {

    private SearchView mSearchView;
    private AutoCompleteTextView mTextView;
    private HistoryOperations mHistoryOperations;

    @OnClick(R.id.search_key)
    public void onSearch(View v) {
        final String query = mSearchView.getQuery().toString().trim();
        if (query.length() == 0) {
            Toast.makeText(this, getString(R.string.toast_search_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        mHistoryOperations.addHistory(query);
        mSearchView.setQuery(query, true);
    }

    @OnClick(R.id.back)
    public void onBack(View v) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_course_search);
        View v = findViewById(R.id.activity_search);
        setBarHeight(v);

        mHistoryOperations = new HistoryOperations(this);
        initView(v);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (Intent.ACTION_SEARCH.equals(action)) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (query == null) {
                query = intent.getDataString();
            }

            initContentFragment(query);
        }
    }

    private void initView(View v) {
        mSearchView = (SearchView) v.findViewById(R.id.search_view);
        setSearchViewStyle(mSearchView);
        initHistoryFragment();
    }

    private void initContentFragment(String query) {
        SearchContentFragment fragment = SearchContentFragment.getInstance(query);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.course_search_content, fragment);
        ft.commit();
    }

    private void initHistoryFragment() {
        SearchHistoryFragment fragment = new SearchHistoryFragment();
        fragment.setClickHistoryListener(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.course_search_content, fragment);
        ft.commit();
    }


    @SuppressLint("NewApi")
    private void setSearchViewStyle(SearchView searchView) {
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.setSubmitButtonEnabled(false);

        //set search icon
        ImageView icon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        icon.setAdjustViewBounds(true);
        icon.setMaxWidth(0);
        icon.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        icon.setImageDrawable(null);

        //set search view bg
        View searchContainer = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchContainer.setBackgroundColor(Color.parseColor("#ffffff"));

        //set hint color
        mTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelOffset(R.dimen.search_edit_text_size));
        mTextView.setHintTextColor(getResources().getColor(R.color.search_edit_text_hint));
        mTextView.setHint(getString(R.string.hint_search));
        mTextView.setGravity(Gravity.BOTTOM);

        //add SearchView
        LinearLayout searchFrame = (LinearLayout) searchView.findViewById(android.support.v7.appcompat.R.id.search_edit_frame);
        if (searchFrame != null) {
            int searchPlateIndex = searchFrame.indexOfChild(searchFrame.findViewById(android.support.v7.appcompat.R.id.search_plate));
            if (searchPlateIndex > 0) {
                LinearLayout searchPlate = (LinearLayout) searchFrame.findViewById(android.support.v7.appcompat.R.id.search_plate);
                ImageView imgSearch = new ImageView(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, 0);
                lp.gravity = Gravity.BOTTOM;
                imgSearch.setLayoutParams(lp);
                imgSearch.setImageResource(R.drawable.search_grey);
                searchPlate.addView(imgSearch, 0);
            }
        }

        //set clear button
        ImageView btnClear = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        btnClear.setImageResource(R.drawable.search_clear_query);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText("");
                initHistoryFragment();
            }
        });

        searchView.requestFocus();

        //set SearchableInfo
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
    }

    @Override
    public void clickHistory(String name) {
        mTextView.setText("");
        mTextView.append(name);
        mHistoryOperations.addHistory(name);
        initContentFragment(name);
    }
}
