package com.khalid.ajrumiyyah.model;

/**
 * Created by theB75 on 09-Mar-15.
 */
public class Chapter {
    String chapterNumber;
    String chapterTitle;
    String href;

    public Chapter(){
    }

    public Chapter(String chapterNumber, String chapterTitle, String href) {
        this.chapterNumber = chapterNumber;
        this.chapterTitle = chapterTitle;
        this.href = href;
    }

    public String getChapterNumber() {return chapterNumber;}

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return chapterTitle;
    }
}