package com.layercontent.build_note.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.layercontent.build_note.R;
import com.layercontent.build_note.notedb.Note;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.BeanHolder> {

    public NotesAdapter(List<Note> list, Context context) {
        this.list = list;
        this.context = context;
        layoutInflater = layoutInflater.from(context);
        this.onNoteItemClik = (OnNoteItemClik) context;

    }

    public interface OnNoteItemClik {
        void onNoteClick(int pos);
    }

    private List<Note> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnNoteItemClik onNoteItemClik;

    @NonNull
    @Override
    public BeanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false);
        return new BeanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.BeanHolder holder, int position) {
        holder.textTitle.setText(list.get(position).getTitle());
        holder.textContent.setText(list.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
//Bean Holder Class
    public class BeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textTitle, textContent;

        public BeanHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textTitle = itemView.findViewById(R.id.tv_title);
            textContent = itemView.findViewById(R.id.tv_content);


        }

        @Override
        public void onClick(View v) {
          onNoteItemClik.onNoteClick(getAdapterPosition());

        }
    }
}
