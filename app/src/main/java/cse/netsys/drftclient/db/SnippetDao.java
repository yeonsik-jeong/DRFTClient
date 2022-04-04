package cse.netsys.drftclient.db;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cse.netsys.drftclient.model.Snippet;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface SnippetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Snippet> snippetList);

    @Query("SELECT * FROM snippets ORDER BY id DESC")
    public PagingSource<Integer, Snippet> pagingSource();

    @Query("DELETE FROM snippets")
    public void clearAll();

/*    @Query("SELECT TOP 1 * FROM snippets ORDER BY [id] DESC")
    Single<long> lastUpdatedTimeSingle();*/

    @Insert
    public Completable insertSnippet(Snippet snippet);  // OK

    @Update
    public Completable updateSnippet(Snippet snippet);  // OK

    @Query("DELETE FROM snippets WHERE id = :id")
    public Completable deleteSnippet(int id);  // OK
}
