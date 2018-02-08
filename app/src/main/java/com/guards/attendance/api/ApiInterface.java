package com.guards.attendance.api;

import com.guards.attendance.models.Packet;
import com.guards.attendance.models.ResponseModel;

import java.util.HashMap;
import java.util.List;

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
    @POST("TestAPI")
    Call<Object> post(@Body HashMap<String, String> body);
    @FormUrlEncoded
    @POST("SaveAttendence")
    Call<Object> postdata(@Body List<Packet> data);
    @FormUrlEncoded
    @POST("TestAPI")
    Call<Object> test(@Field("request") String data);
    @Headers({"Content-type:application/json"})
    @POST("/SaveAttendence")
    Call<ResponseModel> TEST(@Body List<Packet> args);
}
