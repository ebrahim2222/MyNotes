package com.example.mynotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {

    OnItemClickListener listener;

    protected NoteAdapter() {
        super(diffCallback);
    }

    private static final DiffUtil.ItemCallback<Note> diffCallback = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();

        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority();
        }
    };


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes, parent, false);
        return new NoteViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        final Note note = getItem(position);
        holder.noteTitle.setText(note.getTitle());
        holder.notePriority.setText(String.valueOf(note.getPriority()));
        holder.noteDes.setText(note.getDescription());

    }



    public Note getNote(int position) {
        return getItem(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteDes, notePriority;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.note_title);
            noteDes = itemView.findViewById(R.id.note_description);
            notePriority = itemView.findViewById(R.id.note_priority);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    listener.onItemClick(getItem(position));
                }
            });
        }


    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }
}
