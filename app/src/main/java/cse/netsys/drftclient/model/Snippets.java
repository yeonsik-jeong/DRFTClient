package cse.netsys.drftclient.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Snippets {
    private int count;
    private Object next;
    private Object previous;
    List<Snippet> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Object getNext() {
        return next;
    }

    public void setNext(Object next) {
        this.next = next;
    }

    public Object getPrevious() {
        return previous;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    public List<Snippet> getResults() {
        return results;
    }

    public void setResults(List<Snippet> results) {
        this.results = results;
    }
}