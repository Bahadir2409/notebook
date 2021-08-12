package com.layercontent.build_note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.layercontent.build_note.notedb.Note;
import com.layercontent.build_note.notedb.NoteDatabase;

import java.lang.ref.WeakReference;

public class AddNoteActivity extends AppCompatActivity {
    //Variables:
    private TextInputEditText et_title, et_content;
    private boolean update;
    private Button button;
    //objects:
    private NoteDatabase noteDatabase;
    private Note note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        //Widgets
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        button = findViewById(R.id.but_save);

        //objects:
        noteDatabase = NoteDatabase.getInstance(AddNoteActivity.this);
        //Check for correct object and data;
        if ((note = (Note) getIntent().getSerializableExtra("note")) != null) {
            getSupportActionBar().setTitle("Update");
            update = true;
            button.setText("Update");
            et_title.setText(note.getTitle());
            et_content.setText(note.getContent());
        }
        //Handling button click events

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if we need to update the note:
                if (update) {
                    note.setContent(et_content.getText().toString());
                    note.setTitle(et_title.getText().toString());
                    noteDatabase.getNoteDao().updateNote(note);
                    SetResult(note, 2);
                }//Create a new Note:
                else {
                    note = new Note(et_content.getText().toString()
                            , et_title.getText().toString());
                    new InsertTask(AddNoteActivity.this, note).execute();
                }
            }
        });

    }

    //Set Results method;
    private void SetResult(Note note, int flag) {
        setResult(flag, new Intent().putExtra("note", note));
        finish();
    }

    //Insert Task:
    private static class InsertTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<AddNoteActivity> activityWeakReference;
        private Note note;

        //only retain a weak reference to the activity:
        InsertTask(AddNoteActivity context, Note note) {
            activityWeakReference = new WeakReference<>(context);
            this.note = note;

        }

        //Do in background methods runs on a workder thread


        @Override
        protected Boolean doInBackground(Void... voids) {
            //retrieving auto incremented note id
            long j = activityWeakReference.get().noteDatabase.getNoteDao().insertNote(note);
            note.setNote_id(j);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            if (b) {
                activityWeakReference.get().SetResult(note, 1);
                activityWeakReference.get().finish();

            }

        }
    }
}