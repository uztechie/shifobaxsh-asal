package uz.techie.shifobaxshasaluz.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final int CACHE_SIZE = 1024*1024*10;
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";

    private static ApiInterface apiInterface;
    public static final String BASE_URL = "https://Shifobaxshasal.uz/";
//    public static final String BASE_URL = "https://ci87220.tmweb.ru/";

    public static ApiInterface getApiInterface(){

        if (apiInterface == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
//
            OkHttpClient client;

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS);

            HttpLoggingInterceptor interceptor1 = new HttpLoggingInterceptor();
            interceptor1.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(interceptor1);
            client = httpClient.build();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            apiInterface = retrofit.create(ApiInterface.class);
        }

        return apiInterface;
    }
}
