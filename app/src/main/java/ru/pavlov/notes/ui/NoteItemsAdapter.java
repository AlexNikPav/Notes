package ru.pavlov.notes.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    private OnItemClickListener itemClickListener;

    public NoteItemsAdapter(CardsSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public NoteItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_note, viewGroup, false);
        Log.d(TAG, "onCreateViewHolder");
        // Здесь можно установить всякие параметры
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteItemsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.setDate(dataSource.getCardData(i));
        Log.d(TAG, "onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public void SetOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }

        public void setDate(NoteData note) {
            title.setText(note.getTitle());
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            date.setText(formatter.format(note.getDateTime().getTime()));
        }
    }
}