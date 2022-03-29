package cse.netsys.drftclient.model;

import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SnippetResp {
    private int count;
    private String next;
    private String previous;
    List<Snippet> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Snippet> getResults() {
        return results;
    }

    public void setResults(List<Snippet> results) {
        this.results = results;
    }

    public Integer getNextPageNumber() {
        if(getNext() == null) {
            return null;
        } else {
            return Integer.parseInt(Uri.parse(getNext()).getQueryParameter("page"));
        }
    }

    public Integer getPreviousPageNumber() {
        if(getPrevious() == null) {
            return null;
        } else {
            return Integer.parseInt(Uri.parse(getPrevious()).getQueryParameter("page"));
        }
    }
}