package com.example.healthydoggy; // 替换为你的包名

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SpotService {
    // GET 请求获取景点列表
    @GET("spots")
    Call<SpotResponse> getSpots();

    // POST 请求更新景点
    @POST("spots/update")
    Call<UpdateSpotResponse> updateSpot(@Body UpdateSpotRequest request);
}