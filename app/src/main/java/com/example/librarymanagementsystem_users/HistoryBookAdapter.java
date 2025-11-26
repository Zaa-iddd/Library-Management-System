package com.example.librarymanagementsystem_users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.librarymanagementsystem_users.functions.Book;
import java.util.List;

public class HistoryBookAdapter extends RecyclerView.Adapter<HistoryBookAdapter.ViewHolder> {

    private Context context;
    private List<Book> historyList;

    public HistoryBookAdapter(Context context, List<Book> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = historyList.get(position);
        holder.title.setText(book.getTitle());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bookTitle);
        }
    }
}
