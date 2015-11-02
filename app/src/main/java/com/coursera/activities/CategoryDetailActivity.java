package com.coursera.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.base.activities.BaseActivity;
import com.coursera.R;
import com.coursera.fragments.CategoryDetailFragment;
import com.facebook.drawee.backends.pipeline.Fresco;

import butterknife.OnClick;

/**
 * Created by supermanmwg on 15-11-1.
 */
public class CategoryDetailActivity extends BaseActivity {
    public static String ID = "category id";
    public static String NAME = "category name";

    public static Intent createIntent(Context context,
                                      int id,
                                      String name) {
        Intent intent = new Intent(context, CategoryDetailActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(NAME, name);

        return intent;
    }

    @OnClick(R.id.detail_back)
    public void onBack(View v) {
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_category_detail);
        View v = findViewById(R.id.category_detail_activity);
        setBarHeight(v);

        int id = getIntent().getIntExtra(ID, -1);
        String name = getIntent().getStringExtra(NAME);
        ((TextView) findViewById(R.id.category_name)).setText(name);

        initCategoryDetailFragment(id);
    }

    private void initCategoryDetailFragment(int id) {
        CategoryDetailFragment fragment = new CategoryDetailFragment();
        Bundle data = new Bundle();
        data.putInt(ID, id);
        fragment.setArguments(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        ft.commit();
    }
}
