package com.example.librarymanagementsystem_users;

import android.app.Dialog;
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

import com.bumptech.glide.Glide;
import com.example.librarymanagementsystem_users.functions.RequestedBook;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestedBookAdapter extends RecyclerView.Adapter<RequestedBookAdapter.ViewHolder> {

    private final Context context;
    private final List<RequestedBook> bookList;

    public RequestedBookAdapter(Context context, List<RequestedBook> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_requested_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestedBook book = bookList.get(position);
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthor());
        holder.requestStatus.setText(book.getStatus());

        Glide.with(context)
                .load(book.getCover_image_url())
                .placeholder(R.drawable.sample_book)
                .into(holder.bookCover);

        holder.requestedBookItemLayout.setTag(book.getId());

        holder.itemView.setOnClickListener(v -> {
            showCancelDialog(book, position);
        });
    }

    private void showCancelDialog(RequestedBook book, int position) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_cancel_confirmation);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        Button noButton = dialog.findViewById(R.id.noButton);
        Button yesButton = dialog.findViewById(R.id.yesButton);

        noButton.setOnClickListener(v1 -> dialog.dismiss());

        yesButton.setOnClickListener(v1 -> {
            removeRequest(book.getTitle());
            bookList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, bookList.size());
            Toast.makeText(context, "Request canceled!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void removeRequest(String bookTitle) {
        SharedPreferences requestedBooksPrefs = context.getSharedPreferences("requested_books", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = requestedBooksPrefs.edit();
        Set<String> requests = requestedBooksPrefs.getStringSet("requested_books_set", new HashSet<>());
        requests.remove(bookTitle);
        editor.putStringSet("requested_books_set", requests);
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
        TextView requestStatus;
        LinearLayout requestedBookItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.bookCover);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            requestStatus = itemView.findViewById(R.id.requestStatus);
            requestedBookItemLayout = itemView.findViewById(R.id.requestedBookItemLayout);
        }
    }
}
