package com.layercontent.build_note.notedb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM "+Constants.TABLE_NAME_NOTE)
    List<Note> getNotes();

    //Insert The Data:
    @Insert
    long insertNote(Note note);
    @Update
    void updateNote(Note repos);

    @Delete
    void deleteNote(Note note);
    //Delete All Notes from The Database
    @Delete
    void deleteNotes(Note... note);

}
