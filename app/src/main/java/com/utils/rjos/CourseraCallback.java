package com.utils.rjos;

import android.content.Context;

import com.coursera.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by supermanmwg on 15-10-29.
 */
public class CourseraCallback<T extends BaseRjo> implements Callback<T> {
    private final Class<T> mType;
    private final Context mContext;

    public CourseraCallback(Context context, Class<T> type) {
        this.mContext = context;
        this.mType = type;
    }

    @Override
    public void success(T t, Response response) {

        if (null == t) {
            try {
                T result = createResult();
                result.setMessage(mContext.getString(R.string.server_error));
                EventBus.getDefault().post(result);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            EventBus.getDefault().post(t);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        try {
            T result = createResult();
            switch (error.getKind()) {
                case NETWORK:
                    result.setMessage(mContext.getString(R.string.no_network));
                    break;
                case HTTP:
                    result.setMessage(mContext.getString(R.string.network_error, error.getMessage()));
                    break;
                case CONVERSION:
                    result.setMessage(mContext.getString(R.string.server_error));
                    break;
                default:
                    result.setMessage(mContext.getString(R.string.unknown_error));
                    break;
            }
            EventBus.getDefault().post(result);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private T createResult() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        T result = null;
        Constructor[] ctors = mType.getDeclaredConstructors();
        for (Constructor ctor : ctors) {
            if (ctor.getGenericParameterTypes().length == 0) {
                ctor.setAccessible(true);
                result = (T) ctor.newInstance();
            }
        }
        return result;
    }
}
