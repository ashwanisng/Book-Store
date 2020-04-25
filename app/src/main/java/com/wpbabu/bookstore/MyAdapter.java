package com.wpbabu.bookstore;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danish on 31/3/18.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<Book> mBooks;
    private Context context;

    public MyAdapter(Context context, ArrayList<Book> mBooks) {
        this.mBooks = mBooks;
        this.context = context;
    }

    public void clear(){
        mBooks.clear();
    }

    public void addAll(List<Book> books){
        mBooks.addAll(books);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Book currentBook = mBooks.get(position);

        holder.author.setText("By: " + currentBook.getmAuthor());

        Picasso.get().load(currentBook.getmImageLink()).fit().into(holder.book_thumbnail);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(mBooks.get(position).getmBuyUrl() != ""){
                    Uri bookUrl = Uri.parse(mBooks.get(position).getmBuyUrl());
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUrl);
                    context.startActivity(websiteIntent);
                }else {
                    Toast.makeText(context, "Book not available on store", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Dialog mDialog = new Dialog(context);
                mDialog.setContentView(R.layout.description_dialog);

                ((TextView)mDialog.findViewById(R.id.title)).setText("Title: " + currentBook.getmTitle());
                ((TextView)mDialog.findViewById(R.id.author)).setText("By: " + currentBook.getmAuthor());
                Picasso.get().load(currentBook.getmImageLink()).fit().into((ImageView)mDialog.findViewById(R.id.thumbnail));

                ((TextView)mDialog.findViewById(R.id.pages)).setText("Pages\n" + currentBook.getmPages());
                ((TextView)mDialog.findViewById(R.id.category)).setText("Category\n" + currentBook.getmCategory());
                ((TextView)mDialog.findViewById(R.id.buy)).setText("Buy");
                ((TextView)mDialog.findViewById(R.id.buy)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mBooks.get(position).getmBuyUrl() != ""){
                            Uri bookUrl = Uri.parse(mBooks.get(position).getmBuyUrl());
                            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUrl);
                            context.startActivity(websiteIntent);
                        }else {
                            Toast.makeText(context, "Book not available on store", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                mDialog.show();
                return true;
            }
        });



    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView author;
        ImageView book_thumbnail;
        LinearLayout root;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            author = (TextView)itemView.findViewById(R.id.author);
            book_thumbnail = (ImageView)itemView.findViewById(R.id.book_thumbnail);
            root = (LinearLayout)itemView.findViewById(R.id.root);


        }
    }
}
