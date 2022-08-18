package com.example.notesapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.Model.Notes;
import com.example.notesapp.NotesClickListener;
import com.example.notesapp.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>{

    private Context context;
    private List<Notes> notesList;
    private NotesClickListener notesClickListener;

    public NotesAdapter(Context context, List<Notes> notesList, NotesClickListener notesClickListener) {
        this.context = context;
        this.notesList = notesList;
        this.notesClickListener = notesClickListener;
    }

    public void filterList(List<Notes> list)
    {
        this.notesList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Notes notes = notesList.get(position);
        if (notes == null)
        {
            return;
        }
        holder.textViewTitle.setText(notes.getTitle());
        holder.textViewTitle.setSelected(true);

        holder.textViewNotes.setText(notes.getNotes());

        holder.textViewDate.setText(notes.getDate());
        holder.textViewDate.setSelected(true);

        if (notes.isPinned())
        {
            holder.imageViewPin.setBackgroundResource(R.drawable.ic_pin);
        }
        else
        {
            holder.imageViewPin.setBackgroundResource(0);
        }

        holder.notesContainer.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.yellow));

        holder.notesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesClickListener.onClick(notes);
            }
        });

        holder.notesContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                notesClickListener.onLongClick(notes, holder.notesContainer);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (notesList != null)
        {
            return notesList.size();
        }
        return 0;
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle, textViewNotes, textViewDate;
        private ImageView imageViewPin;
        private CardView notesContainer;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.textView_title);
            textViewNotes = (TextView) itemView.findViewById(R.id.textView_notes);
            textViewDate = (TextView) itemView.findViewById(R.id.textView_date);
            imageViewPin = (ImageView) itemView.findViewById(R.id.imageView_pin);
            notesContainer = (CardView) itemView.findViewById(R.id.notes_container);
        }
    }
}
