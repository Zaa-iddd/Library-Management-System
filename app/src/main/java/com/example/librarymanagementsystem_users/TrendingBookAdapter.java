package com.example.librarymanagementsystem_users;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.librarymanagementsystem_users.functions.Book;

import java.util.List;

public class TrendingBookAdapter extends RecyclerView.Adapter<TrendingBookAdapter.ViewHolder> {

    private final Context context;
    private final List<Book> bookList;
    private final long userId;

    public TrendingBookAdapter(Context context, List<Book> bookList, long userId) {
        this.context = context;
        this.bookList = bookList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trending_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);

        String finalUrl = book.getCover_image_url();
        android.util.Log.d("IMAGE_URL_DEBUG", "Final URL: " + finalUrl);

        holder.title.setText(book.getTitle());

        Glide.with(context)
                .load(finalUrl)
                .placeholder(R.drawable.sample_book)
                .into(holder.cover);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewBookActivity.class);
            intent.putExtra("BOOK_ID", book.getId());
            intent.putExtra("USER_ID", userId);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView cover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            cover = itemView.findViewById(R.id.imageBook);
        }
    }
}
