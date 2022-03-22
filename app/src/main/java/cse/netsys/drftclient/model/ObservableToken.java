package cse.netsys.drftclient.model;

public class ObservableToken {
    private String value = null;
    private OnStringChangeListener listener;

    public interface OnStringChangeListener
    {
        public void onStringChanged(String newValue);
    }

    public void setOnStringChangeListener(OnStringChangeListener listener)
    {
        this.listener = listener;
    }

    public String get()
    {
        return value;
    }

    public void set(String value)
    {
        this.value = value;
        if(listener != null) {
            listener.onStringChanged(value);
        }
    }
}
