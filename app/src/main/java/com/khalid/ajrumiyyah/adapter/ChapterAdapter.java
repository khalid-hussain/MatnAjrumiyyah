package com.khalid.ajrumiyyah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.khalid.ajrumiyyah.R;
import com.khalid.ajrumiyyah.model.Chapter;

import java.util.List;

/**
 * Created by Khalid on 26 Mar 2015.
 */
public class ChapterAdapter extends ArrayAdapter<Chapter> {

    public ChapterAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ChapterAdapter(Context context, int resource, List<Chapter> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.chapter_list_item, null);
        }

        Chapter p = getItem(position);

        if (p != null) {
            TextView tvChapterNumber = (TextView) v.findViewById(R.id.tvChapterNumber);
            TextView tvChapterTitle = (TextView) v.findViewById(R.id.tvChapterTitle);

            if (tvChapterNumber != null) {
                tvChapterNumber.setText(p.getChapterNumber());
            }
            if (tvChapterTitle != null) {
                tvChapterTitle.setText(p.getChapterTitle());
            }
        }
        return v;
    }
}