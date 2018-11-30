package com.gianmoura.booker.service.google.book;

import com.gianmoura.booker.model.BookList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleBookService {
    @GET("volumes")
    Call<BookList> getBooks(@Query("q") String id);
}
