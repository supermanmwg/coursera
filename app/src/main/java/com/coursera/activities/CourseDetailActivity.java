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
import com.coursera.fragments.CourseFragment;
import com.facebook.drawee.backends.pipeline.Fresco;

import butterknife.OnClick;

/**
 * Created by supermanmwg on 15-11-1.
 */
public class CourseDetailActivity extends BaseActivity {
    public static String ID = "course id";
    public static String NAME = "course name";
    public static String UNIVERSITY_NAME = "university name";

    public static Intent createIntent(Context context,
                                      int id,
                                      String name,
                                      String universityName) {
        Intent intent = new Intent(context, CourseDetailActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(NAME, name);
        intent.putExtra(UNIVERSITY_NAME, universityName);

        return intent;
    }

    @OnClick(R.id.course_back)
    public void onBack(View v) {
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_course);
        View v = findViewById(R.id.course_activity);
        setBarHeight(v);

        int id = getIntent().getIntExtra(ID, -1);
        String name = getIntent().getStringExtra(NAME);
        ((TextView) findViewById(R.id.course_name)).setText(name);
        String universityName = getIntent().getStringExtra(UNIVERSITY_NAME);
        initCourseDetailFragment(id, name, universityName);
    }

    private void initCourseDetailFragment(int id, String name, String universityName){
        CourseFragment fragment = new CourseFragment();
        Bundle data = new Bundle();
        data.putInt(ID, id);
        data.putString(NAME, name);
        data.putString(UNIVERSITY_NAME, universityName);
        fragment.setArguments(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        ft.commit();
    }
}
