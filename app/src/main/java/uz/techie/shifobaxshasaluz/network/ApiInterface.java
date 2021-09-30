package uz.techie.shifobaxshasaluz.network;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import uz.techie.shifobaxshasaluz.models.Bonus;
import uz.techie.shifobaxshasaluz.models.Friend;
import uz.techie.shifobaxshasaluz.models.History;
import uz.techie.shifobaxshasaluz.models.HoneyResponse;
import uz.techie.shifobaxshasaluz.models.News;
import uz.techie.shifobaxshasaluz.models.OrderResponse;
import uz.techie.shifobaxshasaluz.models.Product;
import uz.techie.shifobaxshasaluz.models.RequestOrder;
import uz.techie.shifobaxshasaluz.models.Seller;
import uz.techie.shifobaxshasaluz.models.User;


public interface ApiInterface {


    //send phone number

    @FormUrlEncoded
    @POST("api/login_send_number/")
    Flowable<HoneyResponse> checkPhoneNumber(@Field("phone_number") String phone);

    //confirm otp with exist number
    @FormUrlEncoded
    @POST("api/login_send_otp/")
    Flowable<HoneyResponse> sendOtpCode(
            @Field("phone_number") String phone,
            @Field("otp_key") String otpCode
    );

    //register
    @FormUrlEncoded
    @POST("api/login_create_user/")
    Flowable<HoneyResponse> registerUser(
            @Field("phone_number") String phone,
            @Field("otp_key") String otpCode,
            @Field("first_name") String name,
            @Field("bdata") String date,
            @Field("Jinsi") int gender
    );


    //products
    @GET("api/product-list/")
    Flowable<List<Product>> loadProducts();

    //seller/client

    @GET("api/user_list")
    Flowable<List<Seller>> loadSellers(
            @Header("Authorization") String token
    );

    //profile


    @GET("/api/user_profil/")
    Flowable<User> loadUserProfile(
            @Header("Authorization") String token
    );

    //order sms code
    @POST("api/order_send_sms/")
    Call<OrderResponse> sendSmsCodeForOrder(
            @Header("token") String tokenKey,
            @Header("seller") int sellerId,
            @Header("key") int code

    );


    //order post
    @Headers({"Content-Type: application/json"})
    @POST("api/order/")
    Call<OrderResponse> sendOrderList(
            @Header("Authorization") String userToken,
            @Header("token") String accessToken,
            @Body RequestOrder orders
    );


    //news
    @GET("api/post-list/")
    Flowable<List<News>> loadNews();


    //history
    @GET("api/user_myorders/")
    Flowable<List<History>> loadHistories(
            @Header("Authorization") String userToken
    );

    //about bonus

    @GET("api/bonus_amount")
    Flowable<List<Bonus>> loadBonusInfo();


    //friends
    @GET("api/affiliate_count")
    Flowable<List<Friend>> loadFriends(
            @Header("Authorization") String userToken
    );

    //bonus history

    @GET("api/bonus_history")
    Flowable<List<History>> loadBonuses(
            @Header("Authorization") String userToken
    );



}

