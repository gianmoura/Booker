package com.gianmoura.booker.service.google.book;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHelper {
    GoogleBookService service;
    public ApiHelper(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(GoogleBookService.class);
    }

    public GoogleBookService getService(){
        return service;
    }
}
