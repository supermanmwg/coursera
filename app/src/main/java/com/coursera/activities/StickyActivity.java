package com.coursera.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.coursera.R;
import com.utils.customviews.IGiveUpTouchListener;
import com.utils.customviews.StickLayout;

/**
 * Created by weiguangmeng on 16/3/9.
 */
public class StickyActivity extends Activity {
    private final String TAG = "StickyActivity";

    private String data[] =
            {   "banana0", "apple0", "oranges0", "pear0", "grape0", "cherry0",
                "banana1", "apple1", "oranges1", "pear1", "grape1", "cherry1",
                "banana2", "apple2", "oranges2", "pear2", "grape2", "cherry2"};

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(StickyActivity.this, android.R.layout.simple_list_item_1, data);
        mListView = (ListView) findViewById(R.id.stick_content);
        mListView.setAdapter(adapter);

        StickLayout stickLayout = (StickLayout) findViewById(R.id.stick_layout);
        stickLayout.setGiveUpTouchListener(mGiveUpTouchListener);
    }

    private IGiveUpTouchListener mGiveUpTouchListener = new IGiveUpTouchListener() {
        @Override
        public boolean giveUpTouchEvent(MotionEvent event) {
            if(mListView != null && 0 == mListView.getFirstVisiblePosition()) {
                View firstChild = mListView.getChildAt(0);
                Log.d(TAG, "first child top is " + firstChild.getTop());
                if(firstChild.getTop() >= 0) {
                    return true;
                }
            }

            return false;
        }
    };
}
