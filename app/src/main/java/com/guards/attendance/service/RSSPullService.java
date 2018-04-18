package com.guards.attendance.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.guards.attendance.api.ApiClient;
import com.guards.attendance.api.ApiInterface;
import com.guards.attendance.database.AppDataBase;
import com.guards.attendance.database.DatabaseUtils;
import com.guards.attendance.models.Packet;
import com.guards.attendance.models.RequestModel;
import com.guards.attendance.models.ResponseModel;
import com.guards.attendance.models.WebApiResponse;
import com.guards.attendance.utils.AppUtils;
import com.guards.attendance.utils.GsonUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Bilal Rashid on 3/25/2018.
 */

public class RSSPullService extends IntentService {
    AppDataBase database;
    public RSSPullService(){
        super("MyService");
    }
    public RSSPullService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        Log.d("TAAAG","Service called");
        database = AppDataBase.getAppDatabase(getApplicationContext());
        if(AppUtils.isInternetAvailable(getApplicationContext())){
            Syncdata();
        }
    }
    private void Syncdata() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.getLastId(AppUtils.getDbId(getApplicationContext()));
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body().Status.equals("Ok")) {
                    Log.d("TAAAG",""+ GsonUtils.toJson(response.body()));
                    postData(DatabaseUtils.with(database).getPacketsToSync(response.body().Result.packetId));
                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
            }
        });

    }

    private void postData(List<Packet> packetsToSync) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        RequestModel request = new RequestModel(AppUtils.getDbId(getApplicationContext()),
                packetsToSync);
//        Log.d("TAAAG",""+GsonUtils.toJson(request));
        Call<WebApiResponse> call = apiService.postPackets(request);
        if(packetsToSync.size()>0){
            call.enqueue(new Callback<WebApiResponse>() {
                @Override
                public void onResponse(Call<WebApiResponse> call, Response<WebApiResponse> response) {
//                    Log.d("TAAAG",GsonUtils.toJson(response.body()));
                }

                @Override
                public void onFailure(Call<WebApiResponse> call, Throwable t) {
                    Log.d("TAAAG",""+t.getMessage());

                }
            });
        }

    }
}