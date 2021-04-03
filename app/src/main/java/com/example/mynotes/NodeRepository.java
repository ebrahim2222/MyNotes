package com.example.mynotes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NodeRepository {

    private NoteDAO noteDAO;
    private LiveData<List<Note>> allNotes;

    public NodeRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDAO = noteDatabase.noteDAO();
        allNotes = noteDAO.mGetAllNotes();
    }

    public void insertNote(Note note){
        new insertAsyncTask(noteDAO).execute(note);
    }
    public void updateNote(Note note){
        new updateAsyncTask(noteDAO).execute(note);
    }
    public void deleteNote(Note note){
        new deleteAsyncTask(noteDAO).execute(note);
    }
    public void deleteAllNotes(){
        new deleteAllAsyncTask(noteDAO).execute();
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

    private class insertAsyncTask extends AsyncTask<Note, Void, Void> {
        NoteDAO noteDAO ;

        public insertAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.mInsert(notes[0]);
            return null;
        }
    }
    private class updateAsyncTask extends AsyncTask<Note, Void, Void> {
        NoteDAO noteDAO ;

        public updateAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.mUpdate(notes[0]);
            return null;
        }
    }
    private class deleteAsyncTask extends AsyncTask<Note, Void, Void> {
        NoteDAO noteDAO ;

        public deleteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.mDelete(notes[0]);
            return null;
        }
    }
    private class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        NoteDAO noteDAO ;

        public deleteAllAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDAO.mDeleteAllNotes();
            return null;
        }
    }
}
