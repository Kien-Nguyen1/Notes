package com.example.notesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.notesapp.Adapter.NotesAdapter;
import com.example.notesapp.Database.NotesDatabase;
import com.example.notesapp.Model.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private SearchView searchViewHome;
    private RecyclerView recyclerViewHome;
    private FloatingActionButton fabAdd;

    private List<Notes> notes = new ArrayList<>();
    private Notes selectedNotes;
    private NotesDatabase database;
    private NotesAdapter adapter;

    public static final int REQUEST_CODE_101 = 101;
    public static final int REQUEST_CODE_102 = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();

        database = NotesDatabase.getInstance(MainActivity.this);
        notes = database.notesDAO().getAll();

        recycleUpdate(notes);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
                startActivityForResult(intent, REQUEST_CODE_101);
            }
        });

        searchViewHome.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_101)
        {
            if (resultCode == RESULT_OK)
            {
                Notes newNotes = (Notes) data.getSerializableExtra("notes");
                database.notesDAO().insert(newNotes);
                notes.clear();
                notes.addAll(database.notesDAO().getAll());
                adapter.notifyDataSetChanged();
            }
        }
        else if (requestCode == REQUEST_CODE_102)
        {
            if (resultCode == RESULT_OK)
            {
                Notes newNotes = (Notes) data.getSerializableExtra("notes");
                database.notesDAO().update(newNotes.getId(), newNotes.getTitle(), newNotes.getNotes());
                notes.clear();
                notes.addAll(database.notesDAO().getAll());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void filter(String newText) {
        List<Notes> filteredList = new ArrayList<>();
        for (Notes singleNotes : notes)
        {
            if (singleNotes.getTitle().toLowerCase().contains(newText.toLowerCase())
                            || singleNotes.getNotes().toLowerCase().contains(newText.toLowerCase()))
            {
                filteredList.add(singleNotes);
            }
        }
        adapter.filterList(filteredList);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_notes", notes);
            startActivityForResult(intent, REQUEST_CODE_102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNotes = new Notes();
            selectedNotes = notes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, cardView);
        popupMenu.setOnMenuItemClickListener(MainActivity.this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.pin:
                if (selectedNotes.isPinned())
                {
                    database.notesDAO().pin(selectedNotes.getId(), false);
                    Toast.makeText(this, "Unpinned!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    database.notesDAO().pin(selectedNotes.getId(), true);
                    Toast.makeText(this, "Pinned!", Toast.LENGTH_SHORT).show();
                }
                notes.clear();
                notes.addAll(database.notesDAO().getAll());
                adapter.notifyDataSetChanged();
                return true;

            case R.id.delete:
                database.notesDAO().delete(selectedNotes);
                notes.remove(selectedNotes);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Note Deleted!", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return false;
        }
    }

    private void recycleUpdate(List<Notes> notes) {
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        adapter = new NotesAdapter(MainActivity.this, notes, notesClickListener);
        recyclerViewHome.setAdapter(adapter);
    }

    private void initUi() {
        searchViewHome = (SearchView) findViewById(R.id.searchView_home);
        recyclerViewHome = (RecyclerView) findViewById(R.id.recycleView_home);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
    }
}