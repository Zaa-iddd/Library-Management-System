package com.example.librarymanagementsystem_users;

import android.content.Context;
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

    public BorrowedBookAdapter(Context context, List<BorrowedBook> bookList) {
        this.context = context;
        this.bookList = bookList;
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

        // Here you would get the actual database ID from your book object
        // long databaseId = book.getDatabaseId(); 
        // holder.borrowedBookItemLayout.setTag(databaseId);

        holder.returnButton.setOnClickListener(v -> {
            // long clickedDatabaseId = (long) holder.borrowedBookItemLayout.getTag();
            removeBook(book.getTitle());
            bookList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, bookList.size());
            Toast.makeText(context, "Book returned!", Toast.LENGTH_SHORT).show();
        });
    }

    private void removeBook(String bookTitle) {
        SharedPreferences borrowedBooksPrefs = context.getSharedPreferences("borrowed_books", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = borrowedBooksPrefs.edit();
        Set<String> borrowed = borrowedBooksPrefs.getStringSet("borrowed_books_set", new HashSet<>());
        borrowed.remove(bookTitle);
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
