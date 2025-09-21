//URL在此份代码！！！
package com.example.healthydoggy;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    // 替换为实际的API基础地址（必须正确配置）
    private static final String BASE_URL = "https://api.example.com/";

    // 获取景点服务实例
    public static SpotService getSpotService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(SpotService.class);
    }
}