package com.coursera.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.base.fragments.BaseFragment;
import com.coursera.R;
import com.coursera.adapters.CategoryAdapter;
import com.utils.apis.ApiService;
import com.utils.apis.ICourseApi;
import com.utils.customviews.CustomPullrefreshLayout;
import com.utils.rjos.CategoryRjo;
import com.utils.rjos.CourseraCallback;

/**
 * Created by supermanmwg on 15-11-1.
 */
public class CategoryFragment extends BaseFragment implements CustomPullrefreshLayout.OnRefreshListener {

    //views
    private RecyclerView recyclerView;
    private CustomPullrefreshLayout refreshLayout;
    private ProgressBar progressBar;

    //adapter
    private CategoryAdapter adapter;

    //retrofit
    private ICourseApi courseApiService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseApiService = ApiService.getInstance(getActivity()).create(ICourseApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        initView(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getCategory();
    }

    private void initView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.category_recycler);
        refreshLayout = (CustomPullrefreshLayout) v.findViewById(R.id.refresh_layout);
        progressBar = (ProgressBar) v.findViewById(R.id.category_progressbar);

        //set view
        refreshLayout.setOnRefreshListener(this);
        adapter = new CategoryAdapter();
        adapter.setContext(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }

    @Override
    public void onRefresh() {
        getCategory();
    }

    public void getCategory() {
        courseApiService.getAllCategory(new CourseraCallback<>(getActivity(), CategoryRjo.class));
    }

    public void onEventMainThread(CategoryRjo rjo) {
        if (rjo.isSuccess()) {
            adapter.setElements(rjo.getElements());
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), getString(R.string.get_category_error, rjo.getMessage()),
                    Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
    }

}
