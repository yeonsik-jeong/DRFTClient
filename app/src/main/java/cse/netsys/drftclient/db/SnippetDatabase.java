package cse.netsys.drftclient.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import cse.netsys.drftclient.model.Snippet;

@Database(entities = {Snippet.class, RemoteKey.class}, version = 1)
public abstract class SnippetDatabase extends RoomDatabase {
    private static volatile SnippetDatabase INSTANCE;

    public abstract SnippetDao snippetDao();
    public abstract RemoteKeyDao remoteKeyDao();

    public static SnippetDatabase getInstance(final Context context) {
        if(INSTANCE == null) {
            synchronized(SnippetDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SnippetDatabase.class, "snippet_database")
                                .build();
                }
            }
        }
        return INSTANCE;
    }
}
