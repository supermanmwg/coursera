package com.coursera.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.fragments.BaseFragment;
import com.coursera.R;
import com.coursera.listeners.IClickHistoryListener;
import com.utils.operations.HistoryOperations;
import com.wefika.flowlayout.FlowLayout;

import java.util.List;

/**
 * Created by supermanmwg on 15-11-3.
 */
public class SearchHistoryFragment extends BaseFragment {

    private ImageView deleteIv;
    private FlowLayout flowLayout;
    private List<String> historyList;
    private HistoryOperations historyOperation;
    private IClickHistoryListener clickHistoryListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyOperation = new HistoryOperations(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_history, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        deleteIv = (ImageView) v.findViewById(R.id.search_delete);
        deleteIv.setOnClickListener(deleteListener);
        flowLayout = (FlowLayout) v.findViewById(R.id.search_history);
        historyList = historyOperation.getHistory();

        if(historyList.size() == 0) {
            addNoHistory(flowLayout);
        } else {
            for(int i = 0; i < historyList.size(); i++) {
                addHistoryItem(flowLayout, historyList.get(i));
            }
        }
    }

    private View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Tip")
                    .setMessage("Do you want to clear history？")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            historyOperation.clearHistory();
                            refreshView();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        }
    };

    //to update the view
    private void refreshView() {
        flowLayout.removeAllViews();
        addNoHistory(flowLayout);
    }

    private void addNoHistory(FlowLayout flowLayout) {
        View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_search_history, null);
        TextView itemTextView = (TextView) itemView.findViewById(R.id.history_item);
        itemTextView.setText(getActivity().getString(R.string.no_history));
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(params);
        flowLayout.addView(itemView);
    }

    private void addHistoryItem(FlowLayout flowLayout, String tag) {
        View itemView = addItem(flowLayout, tag);
        itemView.setTag(tag);
        itemView.setOnClickListener(historyOnClickListener);
    }
    private View.OnClickListener historyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            clickHistoryListener.clickHistory((String) view.getTag());
        }
    };

    private View addItem(FlowLayout flowLayout, String name) {
        View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_search_history, null);
        itemView.setTag(name);
        TextView itemTextView = (TextView) itemView.findViewById(R.id.history_item);
        itemTextView.setText(name);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.rightMargin = getResources().getDimensionPixelOffset(R.dimen.search_margin_right);
        itemView.setLayoutParams(params);
        flowLayout.addView(itemView);
        return itemView;
    }

    public void setClickHistoryListener(IClickHistoryListener clickHistoryListener) {
        this.clickHistoryListener = clickHistoryListener;
    }
}
