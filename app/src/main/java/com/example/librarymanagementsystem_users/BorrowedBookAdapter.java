package com.example.librarymanagementsystem_users;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.BorrowedBook;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BorrowedBookAdapter extends RecyclerView.Adapter<BorrowedBookAdapter.ViewHolder> {

    private final Context context;
    private final List<BorrowedBook> bookList;
    private final long userId;

    public BorrowedBookAdapter(Context context, List<BorrowedBook> bookList, long userId) {
        this.context = context;
        this.bookList = bookList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_borrowed_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BorrowedBook book = bookList.get(position);

        holder.bookTitle.setText(book.getTitle());
        holder.dueDate.setText("Due: " + book.getDueDate());
        holder.bookCover.setImageResource(R.drawable.sample_book);

        // Store the databaseId in layout tag
        holder.borrowedBookItemLayout.setTag(book.getDatabaseId());

        // CLICK: Open book details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewBookActivity.class);
            intent.putExtra("BOOK_ID", book.getDatabaseId());  // Use database ID
            intent.putExtra("USER_ID", userId);
            context.startActivity(intent);
        });

        // CLICK: Return book
        holder.returnButton.setOnClickListener(v -> {
            long clickedDatabaseId = (long) holder.borrowedBookItemLayout.getTag();

            removeBookFromPrefs(clickedDatabaseId);

            bookList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, bookList.size());

            Toast.makeText(context, "Book returned!", Toast.LENGTH_SHORT).show();
        });
    }

    private void removeBookFromPrefs(long databaseId) {
        SharedPreferences prefs = context.getSharedPreferences("borrowed_books", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> borrowed = prefs.getStringSet("borrowed_books_set", new HashSet<>());
        borrowed.remove(String.valueOf(databaseId));   // Remove only once

        editor.putStringSet("borrowed_books_set", borrowed);
        editor.apply();
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookTitle;
        TextView dueDate;
        Button returnButton;
        LinearLayout borrowedBookItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.bookCover);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            dueDate = itemView.findViewById(R.id.dueDate);
            returnButton = itemView.findViewById(R.id.returnButton);
            borrowedBookItemLayout = itemView.findViewById(R.id.borrowedBookItemLayout);
        }
    }
}
