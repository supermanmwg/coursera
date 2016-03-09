package com.coursera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.base.activities.BaseActivity;
import com.coursera.activities.CourseSearchActivity;
import com.coursera.activities.StickyActivity;
import com.coursera.fragments.CategoryFragment;
import com.utils.customviews.StickLayout;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    public static final String CATEGORY_ID = "category id";

    @OnClick(R.id.main_search)
    public void onSearch(View v) {
        startActivity(new Intent(this, StickyActivity.class));
       // startActivity(new Intent(this, CourseSearchActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View v = findViewById(R.id.main_activity);
        setBarHeight(v);
        initCategoryFragment();
    }

    private void initCategoryFragment() {
        CategoryFragment fragment = new CategoryFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        ft.commit();
    }
}
