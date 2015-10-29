package com.utils.apis;

import android.content.Context;

import com.coursera.R;

import retrofit.RestAdapter;

/**
 * Created by supermanmwg on 15-10-29.
 */
public class ApiService {
    private static ApiService instance;
    private final Context mContext;
    private RestAdapter mRestAdapter;

    private ApiService(Context context) {
        this.mContext = context.getApplicationContext();
        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setEndpoint(mContext.getString(R.string.api_url));
        mRestAdapter = builder.build();
    }

    public static ApiService getInstance(Context context) {
        if (instance == null) {
            synchronized (ApiService.class) {
                if (instance == null) {
                    instance = new ApiService(context);
                }
            }
        }
        return instance;
    }

    public <T> T create(Class<T> service) {
        return mRestAdapter.create(service);
    }
}
