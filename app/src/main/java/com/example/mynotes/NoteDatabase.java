package com.example.mynotes;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDAO noteDAO();

    public static synchronized NoteDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    //.fallbackToDestructiveMigration()
                    //.addCallback(callback)
                    .build();
        }

        return instance;
    }

    private static Callback callback = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateUIAsyncTask(instance).execute();
        }
    };

    public static class populateUIAsyncTask extends AsyncTask<Void, Void, Void> {
        NoteDAO noteDAO ;

        public populateUIAsyncTask(NoteDatabase db) {
            this.noteDAO = db.noteDAO();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDAO.mInsert(new Note("title 1", "description1 ", 1));
            noteDAO.mInsert(new Note("title 2", "description2 ", 2));
            noteDAO.mInsert(new Note("title 3", "description3 ", 3));
            return null;
        }
    }
}
