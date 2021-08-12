package com.layercontent.build_note.notedb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Database(entities = {Note.class}, version = 1,exportSchema = false)
@TypeConverters({DatarRoomConverter.class})
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao getNoteDao();

    private static NoteDatabase noteDB;

    public static /*synchronized*/NoteDatabase getInstance(Context context) {
        if (null == noteDB) {
            noteDB = buildDatabaseInstance(context);

        }
        return noteDB;
    }

    private static NoteDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context
                , NoteDatabase.class
                , Constants.TABLE_NAME_NOTE).allowMainThreadQueries().build();
    }

    public void cleanUp() {
        noteDB = null;
    }

}
