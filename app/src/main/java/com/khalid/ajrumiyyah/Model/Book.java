package com.khalid.ajrumiyyah.Model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by theB75 on 09-Mar-15.
 */
public class Book {
    String title;
    ArrayList<Chapter> chapterList;

    public Book() {
    }

    public void setBook(Context context, String bookDir) {
        String jsonString = null;
        try {
            InputStream inputStream = context.getAssets().open(bookDir + "/book.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];

            inputStream.read(buffer);
            inputStream.close();

            jsonString = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray tableOfContent = jsonObject.getJSONArray("table_of_content");

            chapterList = new ArrayList<Chapter>();

            for (int i = 0; i < tableOfContent.length(); i++) {
                JSONObject jsonChapter = (JSONObject) tableOfContent.get(i);
                String chapterTitle = jsonChapter.getString("chapter_title");
                String href = jsonChapter.getString("href");

                Chapter chapter = new Chapter(chapterTitle, href);
                chapterList.add(chapter);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Chapter> getChapterList() {
        return chapterList;
    }
}