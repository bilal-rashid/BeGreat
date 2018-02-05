package com.guards.attendance.api;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Bilal Rashid on 1/28/2018.
 */

public interface ApiInterface {
    @GET("movie/top_rated")
    Call<Object> getTopRatedMovies(@Query("api_key") String apiKey);
    @POST("SaveAttendence")
    Call<Object> post(@Body HashMap<String, String> body);
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("SaveAttendence")
    Call<Object> postdata(@Field("request") String amount);
}
