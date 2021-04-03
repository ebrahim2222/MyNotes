package com.example.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditNoteActivity extends AppCompatActivity {

    @BindView(R.id.add_edit_note_title)
    EditText addEditNoteTitle;
    @BindView(R.id.add_edit_note_description)
    EditText addEditNoteDescription;
    @BindView(R.id.add_edit_note_priority)
    NumberPicker addEditNotePriority;

    public static final String NOTE_TITLE_INTEND_KEY = "note_title";
    public static final String NOTE_ID_INTEND_KEY = "note_id";
    public static final String NOTE_DES_INTEND_KEY = "note_des";
    public static final String NOTE_PRIORITY_INTEND_KEY = "note_priority";
    public static final String NOTE_OBJECT_INTEND_KEY = "note_object";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);
        ButterKnife.bind(this);

        addEditNotePriority.setMinValue(1);
        addEditNotePriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if (intent.hasExtra(NOTE_ID_INTEND_KEY)) {
            setTitle("Edit Note");
            Note note = (Note) intent.getSerializableExtra(NOTE_OBJECT_INTEND_KEY);
            addEditNoteTitle.setText(note.getTitle());
            addEditNoteDescription.setText(note.getDescription());
            addEditNotePriority.setValue(note.getPriority());
        } else {
            setTitle("Add Note");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save_note:
                backToMainActivityAndSaveData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void backToMainActivityAndSaveData() {

        if (Validation()) {
            String noteTitle = addEditNoteTitle.getText().toString();
            String noteDescription = addEditNoteDescription.getText().toString();
            int notePriority = addEditNotePriority.getValue();
            Note note = new Note(noteTitle, noteDescription, notePriority);

            Intent mainIntent = new Intent();
            mainIntent.putExtra(NOTE_OBJECT_INTEND_KEY, (Serializable) note);

            int noteId = getIntent().getIntExtra(NOTE_ID_INTEND_KEY, -1);
            if (noteId != -1) {
                mainIntent.putExtra(NOTE_ID_INTEND_KEY, noteId);
            }
            /*mainIntent.putExtra(NOTE_TITLE_INTEND_KEY, noteTitle);
            mainIntent.putExtra(NOTE_DES_INTEND_KEY, noteDescription);
            mainIntent.putExtra(NOTE_PRIORITY_INTEND_KEY, notePriority);*/
            setResult(RESULT_OK, mainIntent);
            finish();
        }

    }

    public boolean Validation() {
        if (TextUtils.isEmpty(addEditNoteTitle.getText().toString())) {
            addEditNoteTitle.setError("Please, Add Note Title.");
            return false;
        } else if (TextUtils.isEmpty(addEditNoteDescription.getText().toString())) {
            addEditNoteDescription.setError("Please, Add Note Description.");
            return false;
        }

        return true;
    }
}
