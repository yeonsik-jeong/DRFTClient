package cse.netsys.drftclient.model;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("SpellCheckingInspection")  // Typo: in word 'linenos'
public class Snippet implements Parcelable {
    private String url;
    private int id;  // Doesn't need to set value
    private String owner;
    private String highlight;
    private String title;
    private String code;
    private boolean linenos;
    private String language;
    private String style;

    public Snippet() {
    }

    public Snippet(String title, String code, boolean linenos, String language, String style) {
        this.title = title;
        this.code = code;
        this.linenos = linenos;
        this.language = language;
        this.style = style;
    }

    public String getUrl() {
        return url;
    }

    public int getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getHighlight() {
        return highlight;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isLinenos() {
        return linenos;
    }

    public void setLinenos(boolean linenos) {
        this.linenos = linenos;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Snippet(Parcel in) {
        this.url = in.readString();
        this.id = in.readInt();
        this.owner = in.readString();
        this.highlight = in.readString();
        this.title = in.readString();;
        this.code = in.readString();;
        this.linenos = (in.readInt() == 1);
        this.language = in.readString();;
        this.style = in.readString();;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeInt(id);
        dest.writeString(owner);
        dest.writeString(highlight);
        dest.writeString(title);
        dest.writeString(code);
        dest.writeInt(linenos? 1: 0);  // Instead of writeBoolean
        dest.writeString(language);
        dest.writeString(style);
    }

    public static final Parcelable.Creator<Snippet> CREATOR = new Parcelable.Creator<Snippet>() {
        @Override
        public Snippet createFromParcel(Parcel source) {
            return new Snippet(source);
        }

        @Override
        public Snippet[] newArray(int size) {
            return new Snippet[size];
        }
    };
}
