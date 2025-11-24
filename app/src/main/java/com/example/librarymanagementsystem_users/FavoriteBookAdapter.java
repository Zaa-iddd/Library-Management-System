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

public class FavoriteBookAdapter extends RecyclerView.Adapter<FavoriteBookAdapter.ViewHolder> {

    private final Context context;
    private final List<Book> favoriteBooks;
    private final SharedPreferences sharedPreferences;

    public FavoriteBookAdapter(Context context, List<Book> favoriteBooks) {
        this.context = context;
        this.favoriteBooks = favoriteBooks;
        this.sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = favoriteBooks.get(position);

        holder.bookCover.setImageResource(book.getCoverResourceId());
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText("Writer: " + book.getAuthor());

        updateFavoriteIcon(holder.favoriteIcon, book.isFavorite());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewBookActivity.class);
            intent.putExtra("book", book);
            context.startActivity(intent);
        });

        holder.favoriteIcon.setOnClickListener(v -> {
            int adapterPosition = holder.getBindingAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Book bookToRemove = favoriteBooks.get(adapterPosition);
                toggleFavorite(bookToRemove.getTitle());
                bookToRemove.setFavorite(false); // No longer a favorite

                // Remove from the list and notify adapter
                favoriteBooks.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
            }
        });
    }

    private void updateFavoriteIcon(ImageView imageView, boolean isFavorite) {
        if (isFavorite) {
            imageView.setImageResource(R.drawable.heart_red);
        } else {
            imageView.setImageResource(R.drawable.heart_gray);
        }
    }

    private void toggleFavorite(String bookTitle) {
        Set<String> favorites = sharedPreferences.getStringSet("favorite_books", new HashSet<>());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (favorites.contains(bookTitle)) {
            favorites.remove(bookTitle);
        }
        editor.putStringSet("favorite_books", favorites);
        editor.apply();
    }

    @Override
    public int getItemCount() {
        return favoriteBooks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookTitle;
        TextView bookAuthor;
        ImageView favoriteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.imageView2);
            bookTitle = itemView.findViewById(R.id.textView2);
            bookAuthor = itemView.findViewById(R.id.textView5);
            favoriteIcon = itemView.findViewById(R.id.imageView3);
        }
    }
}
