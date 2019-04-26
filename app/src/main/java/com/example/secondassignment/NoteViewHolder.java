package com.example.secondassignment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    public TextView noteTitle;
    public TextView noteDesc;
    public TextView lastModifyDate;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        this.noteTitle = itemView.findViewById(R.id.noteTitle);
        this.noteDesc = itemView.findViewById(R.id.noteDesc);
        this.lastModifyDate = itemView.findViewById(R.id.lastModifyDate);
    }
}
