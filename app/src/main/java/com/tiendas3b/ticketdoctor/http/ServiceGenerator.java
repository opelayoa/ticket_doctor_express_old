package com.tiendas3b.ticketdoctor.http;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tiendas3b.ticketdoctor.util.Constants;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dfa on 22/02/2016.
 */
public class ServiceGenerator {

//        public static final String API_BASE_URL = "http://100.14.0.84:8080/context/rest/";//ver si lo jalo de un string resource
    public static final String API_BASE_URL = "http://t3bevale.homeip.net:8080/context-0.0.1-SNAPSHOT/rest/";
//    public static final String API_BASE_URL = "http://192.168.0.185:8080/context/rest/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Gson gson = new GsonBuilder()
//            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .setDateFormat(Constants.DATETIME_FORMAT_WS).create();

    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create(gson));

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, String login, String password) {

//        byte[] bf = "ZGZhOjljYWZlZWYwOGRiMmRkNDc3MDk4YTAyOTNlNzFmOWE=".getBytes();
//        String s;
//        s = new String(Base64.decode(bf, Base64.NO_WRAP));
//        Log.e("TAG", "DECR:" + s);


        if (login != null && password != null) {
            String credentials = login + ":" + password;
//            Log.e("TAG", credentials);
            final String basic;
            try {
                byte[] bytes = credentials.getBytes("UTF-8");
                //intentos y creencias de padding
//            Log.e("login", "bytes: " + bytes.length + " mod:" + (bytes.length % 6));
//            byte[] newBytes = bytes;
//            switch (bytes.length % 6){
//                case 5:
//                    newBytes = new byte[bytes.length + 1];
//                    System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
//                    break;
//            }

                basic = "Basic " + Base64.encodeToString(bytes, Base64.NO_WRAP);
//            basic = "Basic " + Base64.encodeToString(newBytes, Base64.NO_WRAP);
//            String b = new String(Base64.encode(newBytes, Base64.NO_WRAP), "UTF-8");
//            basic = "Basic " + b;

//                Log.e("BASIC", basic);

                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request original = chain.request();

                        Request.Builder requestBuilder = original.newBuilder().header("Authorization", basic).header("Accept", "application/json").method(original.method(), original.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}
