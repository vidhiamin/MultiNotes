package com.example.secondassignment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    private List<Note> notesLst;
    private MainActivity mainactivity;

    public NotesAdapter(List<Note> notesLst, MainActivity mainactivity) {
        this.notesLst = notesLst;
        this.mainactivity = mainactivity;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_note_view,parent,false);
        itemView.setOnClickListener(mainactivity);
        itemView.setOnLongClickListener(mainactivity);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int position) {
        Note note = notesLst.get(position);
        noteViewHolder.noteTitle.setText(note.getTitle());
        String data = note.getDesc();
        String str;
        if(data.length()>80){
            str = data.substring(0,80)+"...";
        }
        else{
            str = data;
        }
        noteViewHolder.noteDesc.setText(str);
        noteViewHolder.lastModifyDate.setText(note.getDate());
    }

    @Override
    public int getItemCount() {
        return notesLst.size();
    }
}
