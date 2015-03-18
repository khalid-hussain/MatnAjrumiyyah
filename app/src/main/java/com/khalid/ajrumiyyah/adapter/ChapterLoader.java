package com.khalid.ajrumiyyah.adapter;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.khalid.ajrumiyyah.model.Book;
import com.khalid.ajrumiyyah.model.Chapter;

import java.util.List;

public class ChapterLoader extends AsyncTaskLoader<List<Chapter>> {
    protected List<Chapter> mData;

    public ChapterLoader(Context context) {
        super(context);
    }

    protected void onStartLoading(List<Chapter> data) {
        if(data != null) {
            deliverResult(data);
        }
        else {
            forceLoad();
        }
    }

    @Override
    public List<Chapter> loadInBackground() {
        List<Chapter> results;
        Book mBook = new Book();
        mBook.setBook(getContext(), "sample_book");
        results = mBook.getChapterList();
        return results;
    }

    @Override
    public void deliverResult(List<Chapter> data) {
        if (isReset()) {
            return;
        }

        mData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    public void onReset() {
        super.onReset();
        onStopLoading();
        mData = null;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }

        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    public void onStopLoading() {
        cancelLoad();
    }
}