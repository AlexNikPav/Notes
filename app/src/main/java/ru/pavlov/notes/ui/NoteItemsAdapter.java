package ru.pavlov.notes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.pavlov.notes.R;
import ru.pavlov.notes.data.NotesSource;
import ru.pavlov.notes.data.NoteData;

public class NoteItemsAdapter extends RecyclerView.Adapter<NoteItemsAdapter.ViewHolder> {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private final static String TAG = "NoteItemsAdapter";
    private final Fragment fragment;
    private NotesSource notesSource;
    private OnItemClickHandler itemClickHandler;
    private int menuPosition;

    public NoteItemsAdapter(NotesSource dataSource, Fragment fragment) {
        this.notesSource = dataSource;
        this.fragment = fragment;
    }

    @Override
    public NoteItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_note, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteItemsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.setData(notesSource.getNoteData(i));
    }

    @Override
    public int getItemCount() {
        return notesSource.size();
    }

    public void setOnItemClickHandler(OnItemClickHandler itemClickListener) {
        this.itemClickHandler = itemClickListener;
    }

    public int getMenuPosition() {
        return menuPosition;
    }

    public void setNotesSource(NotesSource notesSource) {
        this.notesSource = notesSource;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            this.initView(itemView);
            initListeners();
            registerContextMenu(itemView);
        }

        private void initListeners() {
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickHandler != null) {
                        menuPosition = getLayoutPosition();
                        itemClickHandler.onItemClick(view, getAdapterPosition());
                    }
                }
            });
            title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    menuPosition = getLayoutPosition();
                    itemView.showContextMenu(10, 10);
                    return true;
                }
            });
        }

        private void initView(View itemView) {
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
        }

        public void setData(NoteData note) {
            title.setText(note.getTitle());
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            date.setText(formatter.format(note.getDateTime().getTime()));
        }

        private void registerContextMenu(@NonNull View itemView) {
            if (fragment != null) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        menuPosition = getLayoutPosition();
                        return false;
                    }
                });
                fragment.registerForContextMenu(itemView);
            }
        }
    }
}