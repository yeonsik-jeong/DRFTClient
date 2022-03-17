package cse.netsys.drftclient.model;

import java.util.List;

public class User {
    private String url;
    private int id;
    private String username;
    private List<String> snippetsByOwner;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getSnippetsByOwner() {
        return snippetsByOwner;
    }

    public void setSnippetsByOwner(List<String> snippetsByOwner) {
        this.snippetsByOwner = snippetsByOwner;
    }
}
