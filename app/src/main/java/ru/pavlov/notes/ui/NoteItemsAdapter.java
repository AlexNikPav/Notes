package ru.pavlov.notes.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.pavlov.notes.R;
import ru.pavlov.notes.data.CardsSource;
import ru.pavlov.notes.data.NoteData;

public class NoteItemsAdapter extends RecyclerView.Adapter<NoteItemsAdapter.ViewHolder> {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private final static String TAG = "NoteItemsAdapter";
    private CardsSource dataSource;
    private OnItemClickHandler itemClickHandler;

    public NoteItemsAdapter(CardsSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public NoteItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_note, viewGroup, false);
        Log.d(TAG, "onCreateViewHolder");
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteItemsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.setData(dataSource.getCardData(i));
        Log.d(TAG, "onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public void setOnItemClickHandler(OnItemClickHandler itemClickListener) {
        this.itemClickHandler = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            this.initView(itemView);
            initListeners();
        }

        private void initListeners() {
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickHandler != null) {
                        itemClickHandler.onItemClick(v, getAdapterPosition());
                    }
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
    }
}