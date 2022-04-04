package cse.netsys.drftclient.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "remote_keys")
public class RemoteKey {
    @PrimaryKey
    public int id;
    public Integer prevKey;
    public Integer nextKey;

    public RemoteKey(int id, Integer prevKey, Integer nextKey) {
        this.id = id;
        this.prevKey = prevKey;
        this.nextKey = nextKey;
    }

    public Integer getPrevKey() {
        return prevKey;
    }

    public Integer getNextKey() {
        return nextKey;
    }
}
