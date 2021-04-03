package com.example.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView nodesRecycler;
    FloatingActionButton addNoteBtn;
    NoteViewModel noteViewModel;
    NoteAdapter adapter;
    public static final int ADD_REQUEST_CODE = 1;
    public static final int EDIT_REQUEST_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nodesRecycler = findViewById(R.id.notes_recyclerView);
        addNoteBtn = findViewById(R.id.add_note_btn);
        nodesRecycler.setLayoutManager(new LinearLayoutManager(this));
        nodesRecycler.setHasFixedSize(true);
        adapter = new NoteAdapter();
        nodesRecycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent addNoteIntent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                addNoteIntent.putExtra(AddEditNoteActivity.NOTE_OBJECT_INTEND_KEY, (Serializable) note);
                addNoteIntent.putExtra(AddEditNoteActivity.NOTE_ID_INTEND_KEY, note.getId());
                startActivityForResult(addNoteIntent, EDIT_REQUEST_CODE);
            }
        });

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNoteIntent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(addNoteIntent, ADD_REQUEST_CODE);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNote(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Node Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(nodesRecycler);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_all_notes:
                deleteAllNotes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteAllNotes() {
        noteViewModel.deleteAll();
        Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQUEST_CODE && resultCode == RESULT_OK) {
            /*String noteTitle = data.getStringExtra(AddEditNoteActivity.NOTE_TITLE_INTEND_KEY);
            String noteDescription = data.getStringExtra(AddEditNoteActivity.NOTE_DES_INTEND_KEY);
            int notePriority = data.getIntExtra(AddEditNoteActivity.NOTE_PRIORITY_INTEND_KEY, -1);
            Note note = new Note(noteTitle, noteDescription, notePriority);*/

            Note note = (Note) data.getSerializableExtra(AddEditNoteActivity.NOTE_OBJECT_INTEND_KEY);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {

            int noteId = data.getIntExtra(AddEditNoteActivity.NOTE_ID_INTEND_KEY, -1);
            if (noteId == -1) {
                Toast.makeText(this, "Note Not Updated", Toast.LENGTH_SHORT).show();
                return;
            }
            Note note = (Note) data.getSerializableExtra(AddEditNoteActivity.NOTE_OBJECT_INTEND_KEY);
            note.setId(noteId);
            noteViewModel.update(note);
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note Not Saved or Updated", Toast.LENGTH_SHORT).show();
        }
    }
}
