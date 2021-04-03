package com.example.mynotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NodeRepository nodeRepository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        this.nodeRepository = new NodeRepository(application);
        allNotes = nodeRepository.getAllNotes();
    }

    public void  insert(Note note){
        nodeRepository.insertNote(note);
    }
    public void  update(Note note){
        nodeRepository.updateNote(note);
    }
    public void  delete(Note note){
        nodeRepository.deleteNote(note);
    }
    public void  deleteAll(){
        nodeRepository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }


}
