package com.layercontent.build_note;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.layercontent.build_note.adapters.NotesAdapter;
import com.layercontent.build_note.notedb.Note;
import com.layercontent.build_note.notedb.NoteDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesAdapter.OnNoteItemClik {
    //Variables and the widgets
    private TextView textViewMsg;
    private RecyclerView recyclerView;
    private NoteDatabase noteDatabase;
    private List<Note> noteList;
    private NotesAdapter notesAdapter;
    private int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Methods:
        initializeViews();
        displayList();
    }
    @Override
    public void onNoteClick(int pos) {
        //Alert Dialog that will show to the user when Fab is clicked
        //To ask for update or delete notes!
        new AlertDialog.Builder(MainActivity.this)
                .setItems(new String[]{"Delete", "Update"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                    switch (i){
                        case 0:
                            noteDatabase.getNoteDao().deleteNote(noteList.get(pos));
                            noteList.remove(pos);
                            listVisibilty();
                            break;
                        case 1:
                            MainActivity.this.pos=pos;
                            startActivityForResult(new Intent(MainActivity.this,AddNoteActivity.class).putExtra("note",noteList.get(pos) ),100);
                            break;
                    }
                    }
                }).show();
    }
    private void displayList() {
        noteDatabase = NoteDatabase.getInstance(MainActivity.this);
        new RetrieveTask(this).execute();
    }
    private static class RetrieveTask extends AsyncTask<Void, Void, List<Note>> {
        private WeakReference<MainActivity> activityWeakReference;
        //The only retain a weak reference to the activity
        RetrieveTask(MainActivity context) {
            activityWeakReference = new WeakReference<>(context);
        }
        @Override
        protected List<Note> doInBackground(Void... voids) {
            if (activityWeakReference.get() != null) {
                return activityWeakReference.get().noteDatabase.getNoteDao().getNotes();
            } else
                return null;
        }
        @Override
        protected void onPostExecute(List<Note> notes) {
            if (notes != null && notes.size() > 0) {
                activityWeakReference.get().noteList.clear();
                activityWeakReference.get().noteList.addAll(notes);
                //Hide the empty textView;
                activityWeakReference.get().textViewMsg.setVisibility(View.GONE);
                activityWeakReference.get().notesAdapter.notifyDataSetChanged();
            }
        }
    }
    @SuppressLint("ResourceAsColor")
    private void initializeViews() {

        textViewMsg = findViewById(R.id.tv_empty);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(listener);
        recyclerView = findViewById(R.id.recyler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList, MainActivity.this);
        recyclerView.setAdapter(notesAdapter);
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivityForResult(new Intent(MainActivity.this, AddNoteActivity.class), 100);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode > 0) {
            if (resultCode == 1) {
                noteList.add((Note) data.getSerializableExtra("note"));
            } else if (resultCode == 2) {
                noteList.set(pos, (Note) data.getSerializableExtra("note"));
            }
            listVisibilty();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void listVisibilty() {

        int emptyMsgVisibiliyt = View.GONE;
        if (noteList.size() == 0) {
            //No items to display
            if (textViewMsg.getVisibility() == View.GONE) {
                emptyMsgVisibiliyt = View.VISIBLE;
            }
        }
        textViewMsg.setVisibility(emptyMsgVisibiliyt);
        notesAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        noteDatabase.cleanUp();
        super.onDestroy();
    }
}