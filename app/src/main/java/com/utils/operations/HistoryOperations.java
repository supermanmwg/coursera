package com.utils.operations;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by supermanmwg on 15-9-23.
 */
public class HistoryOperations {
    private final String HISTORY = "com.example.app.history";
    private final String KEY_ARRAY = "history";
    private final String KEY_COUNT = "count";
    private final int HISTORY_COUNT = 5;

    private SharedPreferences mHistorys;
    private Context mContext;

    public HistoryOperations(Context context){
        this.mContext = context;
        this.mHistorys = mContext.getSharedPreferences(HISTORY, Context.MODE_PRIVATE);
    }
    public void clearHistory() {
        SharedPreferences.Editor editor = mHistorys.edit();
        for (int i = 0; i < HISTORY_COUNT; i++) {
            editor.remove(KEY_ARRAY + i);
        }
        editor.commit();
    }

    public void addHistory(String query) {
        // Save the list.
        int duplicateCount = 0;

        for (int i = HISTORY_COUNT - 1; i >= 0; i--) {
            String item = mHistorys.getString(KEY_ARRAY + i, null);
            if (null != item && item.equals(query)) {
                duplicateCount = i;
                break;
            }
        }

        for (int i = duplicateCount + 1; i < HISTORY_COUNT; i++) {
            String item = mHistorys.getString(KEY_ARRAY + i, null);
            if (null != item) {
                SharedPreferences.Editor editor = mHistorys.edit();
                editor.putString(KEY_ARRAY + (i - 1), item);
                editor.commit();
            }
        }

        SharedPreferences.Editor editor = mHistorys.edit();
        editor.putString(KEY_ARRAY + (HISTORY_COUNT - 1), query);
        editor.commit();
    }

    public List<String> getHistory() {
        List<String> arrays = new ArrayList<>();
        for (int i = HISTORY_COUNT - 1; i >= 0; i--) {
            String item = mHistorys.getString(KEY_ARRAY + i, null);
            if (null != item)
                arrays.add(item);
        }
        return arrays;
    }
}
