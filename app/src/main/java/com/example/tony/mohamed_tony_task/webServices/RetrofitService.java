package com.example.tony.mohamed_tony_task.webServices;

import com.example.tony.mohamed_tony_task.webServices.responses.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitService {
    @POST("/")
    Call<List<User>> getUsersData();
}