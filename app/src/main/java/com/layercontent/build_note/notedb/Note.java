package com.layercontent.build_note.notedb;

import android.provider.SyncStateContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName =Constants.TABLE_NAME_NOTE)
public class Note implements Serializable {
    //Columns;
    @PrimaryKey(autoGenerate = true)
    private long note_id;

   @ColumnInfo(name = "note_content")
    private String content;

   private String title;

   private Date date;

   //Constructors:
   @Ignore
    public Note() {
    }

    public Note(String content, String title) {
        this.content = content;
        this.title = title;
        this.date = new Date(System.currentTimeMillis());
    }

    public Date getDate(){return  date;}
    public void setDate(Date date){this.date=date;}

    public long getNote_id() {
        return note_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
       if (this==obj)
       {return true;}
       if (!(obj instanceof Note)){return false;}
       Note note=(Note)obj;
       if (note_id!=note.note_id){return false;}

        return title!=null ? title.equals(note.title) :note.title==null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote_id(long note_id) {
        this.note_id = note_id;
    }

    @Override
    public int hashCode() {
       int reseult=(int) note_id;
       reseult=31*reseult+(title!=null ? title.hashCode():0);

        return reseult;
    }

    @NonNull
    @Override
    public String toString() {
        return "Note{"+
                "note_id"+note_id+
                ",content='"+content+' '+
                ",title='"+title+' '+
                ",date="+date+
                "}";
    }
}
