package com.wpbabu.bookstore;

public class Book{

    private String mTitle;
    private String mAuthor;
    private String mImageLink;

    private String mPages;
    private String mBuyUrl;
    private String mCategory;


    public Book(String title, String pages, String author, String buyUrl, String imageLink, String category){
        mTitle = title;
        mPages = pages;
        mAuthor = author;
        mBuyUrl = buyUrl;
        mImageLink = imageLink;
        mCategory = category;

    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmPages() {
        return mPages;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmBuyUrl() {
        return mBuyUrl;
    }

    public String getmImageLink(){
        return mImageLink;
    }

    public String getmCategory() {
        return mCategory;
    }

}
