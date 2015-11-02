package com.coursera.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.base.fragments.BaseFragment;
import com.coursera.MainActivity;
import com.coursera.R;
import com.coursera.adapters.CategoryDetailAdapter;
import com.utils.apis.ApiService;
import com.utils.apis.ICourseApi;
import com.utils.customviews.CustomPullrefreshLayout;
import com.utils.customviews.DividerItemDecoration;
import com.utils.rjos.CategoryCourseRjo;
import com.utils.rjos.CourseRjo;
import com.utils.rjos.CourseraCallback;

import java.util.List;

/**
 * Created by supermanmwg on 15-11-1.
 */
public class CategoryDetailFragment extends BaseFragment implements CustomPullrefreshLayout.OnRefreshListener {
    //data
    private int categoryId;

    //adapter
    private CategoryDetailAdapter adapter;

    //retrofit
    private ICourseApi courseApiService;

    //views
    private RecyclerView recyclerView;
    private CustomPullrefreshLayout refreshLayout;
    private ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryId = getArguments().getInt(MainActivity.CATEGORY_ID, -1);
        courseApiService = ApiService.getInstance(getActivity()).create(ICourseApi.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_detail, container, false);
        initView(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getCourseId();
    }

    private void initView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.detail_category_recycler);
        refreshLayout = (CustomPullrefreshLayout) v.findViewById(R.id.detail_refresh_layout);
        progressBar = (ProgressBar) v.findViewById(R.id.detail_category_progressbar);

        //set view
        refreshLayout.setOnRefreshListener(this);
        adapter = new CategoryDetailAdapter();
        adapter.setContext(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void onRefresh() {
        getCourseId();
    }

    private void getCourseId() {
        courseApiService.getCategoryCoursesId(categoryId, new CourseraCallback<>(getActivity(), CategoryCourseRjo.class));
    }

    public void onEventMainThread(CategoryCourseRjo rjo) {
        if (rjo.isSuccess()) {
            List<Integer> courseIds = rjo.getElements().get(0).getLinks().getCourses();

            String ids = courseIds.toString().substring(1, courseIds.size() -2);
            Log.d("haha", "courseIds" + ids);
            courseApiService.getCoursesByIds(ids, new CourseraCallback<>(getActivity(), CourseRjo.class));
        } else {
            Toast.makeText(getActivity(), getString(R.string.get_courses_error, rjo.getMessage()), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            refreshLayout.setRefreshing(false);
        }
    }

    public void onEventMainThread(CourseRjo rjo) {
        if(rjo.isSuccess()) {
            adapter.setRjo(rjo);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), getString(R.string.get_courses_error, rjo.getMessage()), Toast.LENGTH_SHORT).show();
        }

        progressBar.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
    }
}
