package com.khalid.ajrumiyyah.adapter;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Khalid on 14 Mar 2015.
 */
public abstract class BookLoader<T> extends AsyncTaskLoader<T> {
    public BookLoader(Context context){
        super(context);
    }
}
