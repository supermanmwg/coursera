package com.coursera.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.base.fragments.BaseFragment;
import com.coursera.R;
import com.coursera.adapters.CategoryDetailAdapter;
import com.utils.apis.ApiService;
import com.utils.apis.ICourseApi;
import com.utils.customviews.DividerItemDecoration;
import com.utils.rjos.CourseRjo;
import com.utils.rjos.CourseraCallback;

/**
 * Created by supermanmwg on 15-11-3.
 */
public class SearchContentFragment extends BaseFragment {
    private static final String KEY_QUERY = "queryKey";

    //data
    private String queryKey;

    //adapter
    private CategoryDetailAdapter adapter;

    //views
    private RecyclerView recyclerView;
    private View searchNoResultsView;
    private ProgressBar progressBar;

    //retrofit
    private ICourseApi courseApiService;

    public static SearchContentFragment getInstance(String query) {
        SearchContentFragment fragment = new SearchContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_QUERY, query);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseApiService = ApiService.getInstance(getActivity()).create(ICourseApi.class);
        queryKey = getArguments().getString(KEY_QUERY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_content, container, false);
        initView(v);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getCourseList();
    }

    private void initView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.search_content_recycler);
        searchNoResultsView = v.findViewById(R.id.search_no_results);
        progressBar = (ProgressBar) v.findViewById(R.id.search_content_progressbar);

        //set view
        adapter = new CategoryDetailAdapter();
        adapter.setContext(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));

    }
    private void getCourseList() {
        courseApiService.getCourseByKeyword(queryKey, new CourseraCallback<>(getActivity(), CourseRjo.class));
    }

    public void onEventMainThread(CourseRjo rjo) {
        if(rjo.isSuccess()) {
            if(rjo.getElements().size() == 0) {
                searchNoResultsView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                searchNoResultsView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            adapter.setRjo(rjo);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), getString(R.string.get_courses_error, rjo.getMessage()), Toast.LENGTH_SHORT).show();
        }

        progressBar.setVisibility(View.GONE);
    }
}
