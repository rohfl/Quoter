package com.rohfl.quoter.singleton;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * MySingleton class will be used to create only one object throughout the app lifecycle
 * @author Rohit Jangid
 * @author https://www.github.com/rohfl
 * @version 1.0
 * @since 1.0
 */
public class MySingleton {

    private static MySingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    /**
     * Private Constructor
     * @param context context of the activity which first called the getInstance method of this class
     */
    private MySingleton(Context context){
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     *
     * @param context context of the calling activity
     * @return the object
     */
    public static synchronized MySingleton getInstance(Context context){
        if(mInstance == null){
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    /**
     * Creates the RequestQueue object
     * @return RequestQueue object
     */
    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Adds request to the queue
     * @param request the request which we want to add in the queue
     * @param <T> generic
     */
    public<T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
