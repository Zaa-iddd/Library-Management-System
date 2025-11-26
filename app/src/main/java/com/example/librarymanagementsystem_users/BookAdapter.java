package com.example.librarymanagementsystem_users;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private final Context context;
    private final List<Book> books;
    private final long userId;

    public BookAdapter(Context context, List<Book> books, long userId) {
        this.context = context;
        this.books = books;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);

        // TODO: Load image from URL using a library like Glide or Picasso
        holder.bookCover.setImageResource(R.drawable.sample_book);
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthor());
        holder.textGenre.setText(book.getGenre());

        if ("Available".equalsIgnoreCase(book.getStatus())) {
            holder.textStatus.setText("Available");
            holder.textStatus.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
        } else {
            holder.textStatus.setText("Unavailable");
            holder.textStatus.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange)));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewBookActivity.class);
            intent.putExtra("BOOK_ID", book.getId());
            intent.putExtra("USER_ID", userId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookTitle;
        TextView bookAuthor;
        TextView textGenre;
        TextView textStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.imageBook);
            bookTitle = itemView.findViewById(R.id.textTitle);
            bookAuthor = itemView.findViewById(R.id.textAuthor);
            textGenre = itemView.findViewById(R.id.textGenre);
            textStatus = itemView.findViewById(R.id.textStatus);
        }
    }
}
