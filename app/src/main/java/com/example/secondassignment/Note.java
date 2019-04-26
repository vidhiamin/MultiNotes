package com.example.secondassignment;

import java.io.Serializable;

public class Note implements Serializable {
    private String title;
    private String desc;
    private String date;
    private int index;

    public Note(String title, String desc, String date, int index) {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.index = index;
    }

    public Note(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
