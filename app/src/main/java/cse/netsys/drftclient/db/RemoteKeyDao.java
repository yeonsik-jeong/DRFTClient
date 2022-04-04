package cse.netsys.drftclient.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cse.netsys.drftclient.model.Snippet;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface RemoteKeyDao {
/*    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(RemoteKey remoteKey);*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<RemoteKey> remoteKeyList);

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    public Single<RemoteKey> remoteKeyByIdSingle(Integer id);

    @Query("SELECT * FROM remote_keys ORDER BY id LIMIT 1")
    public Single<RemoteKey> remoteKeyLatestSingle();

    @Query("DELETE FROM remote_keys")
    public void clearAll();

    @Insert
    public Completable insertRemoteKey(RemoteKey remoteKey); // OK

    @Query("DELETE FROM remote_keys WHERE id = :id")
    public Completable deleteRemoteKey(int id);  // OK
}
