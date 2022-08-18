package com.example.notesapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notesapp.Model.Notes;

@Database(entities = {Notes.class}, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "notes_db";
    private static NotesDatabase database;

    public static synchronized NotesDatabase getInstance(Context context)
    {
        if (database == null)
        {
            database = Room.databaseBuilder(context.getApplicationContext(), NotesDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract NotesDAO notesDAO();
}
