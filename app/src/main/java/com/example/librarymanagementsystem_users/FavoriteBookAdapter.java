package com.example.librarymanagementsystem_users;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.Book;

import java.util.List;

public class FavoriteBookAdapter extends RecyclerView.Adapter<FavoriteBookAdapter.ViewHolder> {

    private final Context context;
    private final List<Book> favoriteBooks;

    public FavoriteBookAdapter(Context context, List<Book> favoriteBooks) {
        this.context = context;
        this.favoriteBooks = favoriteBooks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_book, parent, false);
        // Programmatically set the background to override the faulty XML.
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = favoriteBooks.get(position);

        holder.bookCover.setImageResource(book.getCoverResourceId());
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText("Writer: " + book.getAuthor());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewBookActivity.class);
            intent.putExtra("book", book);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteBooks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookTitle;
        TextView bookAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.bookCover);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
        }
    }
}
