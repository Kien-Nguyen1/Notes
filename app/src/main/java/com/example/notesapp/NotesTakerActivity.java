package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notesapp.Model.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakerActivity extends AppCompatActivity {

    private EditText txtTitle, txtNotes;
    private ImageView imageViewSave;

    private Notes notes;
    private boolean isOldNotes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        initUi();

        notes = new Notes();

        try{
            notes = (Notes) getIntent().getSerializableExtra("old_notes");
            txtTitle.setText(notes.getTitle());
            txtNotes.setText(notes.getNotes());
            isOldNotes = true;
        } catch (Exception e){
            e.printStackTrace();
        }

        imageViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = txtTitle.getText().toString();
                String description = txtNotes.getText().toString();

                if (description.isEmpty())
                {
                    Toast.makeText(NotesTakerActivity.this, "Add some note, please!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isOldNotes)
                {
                    notes = new Notes();
                }

                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                Date date = new Date();

                notes.setTitle(title);
                notes.setNotes(description);
                notes.setDate(formatter.format(date));

                Intent intentResult = new Intent();
                intentResult.putExtra("notes", notes);
                setResult(RESULT_OK, intentResult);
                finish();
            }
        });
    }

    private void initUi() {
        txtNotes = (EditText) findViewById(R.id.txt_notes);
        txtTitle = (EditText) findViewById(R.id.txt_title);
        imageViewSave = (ImageView) findViewById(R.id.imageView_save);
    }
}