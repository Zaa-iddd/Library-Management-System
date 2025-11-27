package com.example.librarymanagementsystem_users;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.librarymanagementsystem_users.functions.Book;

import java.util.List;

public class BookViewHelper {

    public static void populateBooks(Activity activity, LinearLayout container, List<Book> books) {
        container.removeAllViews();
        if (books == null) return;
        LayoutInflater inflater = LayoutInflater.from(activity);
        final int booksPerRow = 3;

        LinearLayout rowLayout = null;
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);

            if (i % booksPerRow == 0) {
                rowLayout = new LinearLayout(activity);
                LinearLayout.LayoutParams rowLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                rowLayoutParams.setMargins(0, 0, 0, 16);
                rowLayout.setLayoutParams(rowLayoutParams);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setWeightSum(booksPerRow);
                container.addView(rowLayout);
            }

            View bookView = inflater.inflate(R.layout.item_book, rowLayout, false);
            ImageView cover = bookView.findViewById(R.id.imageBook);
            TextView title = bookView.findViewById(R.id.textTitle);
            TextView author = bookView.findViewById(R.id.textAuthor);
            TextView genreView = bookView.findViewById(R.id.textGenre);
            TextView status = bookView.findViewById(R.id.textStatus);

            //para sa url
            cover.setImageResource(R.drawable.sample_book);

            title.setText(book.getTitle());
            author.setText(book.getAuthor());
            genreView.setText(book.getGenre());

            if ("Available".equalsIgnoreCase(book.getStatus())) {
                status.setText("Available");
                status.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.green)));
            } else {
                status.setText("Unavailable");
                status.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.orange)));
            }

            LinearLayout.LayoutParams bookLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            bookLayoutParams.setMarginEnd(16);
            bookView.setLayoutParams(bookLayoutParams);

            if (rowLayout != null) {
                rowLayout.addView(bookView);
            }

            bookView.setOnClickListener(v -> {
                Intent viewBookIntent = new Intent(activity, ViewBookActivity.class);
                viewBookIntent.putExtra("book", book);
                activity.startActivity(viewBookIntent);
            });
        }

        // Fill empty space in the last row
        if (rowLayout != null) {
            int childCount = rowLayout.getChildCount();
            if (childCount > 0 && childCount < booksPerRow) {
                for (int i = 0; i < booksPerRow - childCount; i++) {
                    View emptyView = new View(activity);
                    LinearLayout.LayoutParams emptyViewParams = new LinearLayout.LayoutParams(0, 0, 1f);
                    emptyViewParams.setMarginEnd(16);
                    emptyView.setLayoutParams(emptyViewParams);
                    rowLayout.addView(emptyView);
                }
            }
        }
    }
}
