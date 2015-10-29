package com.base.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.utils.rjos.BaseRjo;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by supermanmwg on 15-10-29.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    public void onEventMainThread(BaseRjo rjo) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
