package com.wpbabu.bookstore;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UiActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>>{

    private static final int BOOK_LOADER_ID = 0;
    public static String url = "https://www.googleapis.com/books/v1/volumes?q=";


    private MyAdapter mAdapter;

    private RecyclerView listView;

    private LoaderManager loaderManager;
    private ProgressBar mprogress;

    private EditText text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_activity);

        loaderManager = getLoaderManager();


        listView = (RecyclerView) findViewById(R.id.list_view);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new GridLayoutManager(this, 2));

        TextView search = (TextView) findViewById(R.id.search_button);


        text = (EditText)findViewById(R.id.searchBar) ;
        hideKeyboard();
        mprogress = (ProgressBar)findViewById(R.id.progress_View);


        listView.setVisibility(View.GONE);


        mAdapter = new MyAdapter(this, new ArrayList<Book>());

        listView.setAdapter(mAdapter);



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://www.googleapis.com/books/v1/volumes?q=" + text.getText().toString().trim() + "&maxResults=30";
                mprogress.setVisibility(View.VISIBLE);

                hideKeyboard();

                if(hasInternet()){
                    if(loaderManager.getLoader(BOOK_LOADER_ID) == null){
                        text.setHint("Enter keyword to search book");
                        loaderManager.initLoader(BOOK_LOADER_ID, null, UiActivity.this);
                    }else {
                        text.setHint("Enter keyword to search book");
                        loaderManager.restartLoader(BOOK_LOADER_ID, null, UiActivity.this);
                        mAdapter.notifyDataSetChanged();
                    }

                }else {
                    text.setHint("No internet connection");
                    mprogress.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                }

            }
        });


        if(hasInternet()){
            if(loaderManager.getLoader(BOOK_LOADER_ID) != null){
                text.setHint("Enter keyword to search book");
                loaderManager.initLoader(BOOK_LOADER_ID, null, this);
            }
        }else{
            text.setHint("No internet connection");
            mprogress.setVisibility(View.GONE);
        }

    }



    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mprogress.setVisibility(View.GONE);
        mAdapter.clear();

        if(books != null && !books.isEmpty()){
            text.setHint("Enter keyword to search book");
            mAdapter.addAll(books);
            listView.setVisibility(View.VISIBLE);
        }else {
            text.setHint("No books found");
            listView.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }

    private  Void hideKeyboard(){
        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.search_LinearLayout);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return null;
    }

    private boolean hasInternet(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork!=null && activeNetwork.isConnected();
    }
}
