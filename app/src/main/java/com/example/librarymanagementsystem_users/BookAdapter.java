package com.example.librarymanagementsystem_users;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.Book;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private final Context context;
    private final List<Book> bookList;
    private final SharedPreferences sharedPreferences;

    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
        this.sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.bookCover.setImageResource(book.getCoverResourceId());
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText("Writer: " + book.getAuthor());

        // Set initial favorite state
        book.setFavorite(isFavorite(book.getTitle()));
        updateFavoriteIcon(holder.favoriteIcon, book.isFavorite());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewBookActivity.class);
            intent.putExtra("book", book);
            context.startActivity(intent);
        });

        holder.favoriteIcon.setOnClickListener(v -> {
            boolean isCurrentlyFavorite = isFavorite(book.getTitle());
            book.setFavorite(!isCurrentlyFavorite);
            toggleFavorite(book.getTitle());
            updateFavoriteIcon(holder.favoriteIcon, book.isFavorite());
        });
    }

    private void updateFavoriteIcon(ImageView imageView, boolean isFavorite) {
        if (isFavorite) {
            imageView.setImageResource(R.drawable.heart_red);
        } else {
            imageView.setImageResource(R.drawable.heart_gray);
        }
    }

    private boolean isFavorite(String bookTitle) {
        Set<String> favorites = sharedPreferences.getStringSet("favorite_books", new HashSet<>());
        return favorites.contains(bookTitle);
    }

    private void toggleFavorite(String bookTitle) {
        Set<String> favorites = sharedPreferences.getStringSet("favorite_books", new HashSet<>());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (favorites.contains(bookTitle)) {
            favorites.remove(bookTitle);
        } else {
            favorites.add(bookTitle);
        }
        editor.putStringSet("favorite_books", favorites);
        editor.apply();
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookTitle;
        TextView bookAuthor;
        ImageView favoriteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.imageBook);
            bookTitle = itemView.findViewById(R.id.textTitle);
            //bookAuthor = itemView.findViewById(R.id.textAuthor);
        }
    }
}
