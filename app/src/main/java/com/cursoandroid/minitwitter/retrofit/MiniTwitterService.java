package com.cursoandroid.minitwitter.retrofit;

import com.cursoandroid.minitwitter.retrofit.requests.RequestLogin;
import com.cursoandroid.minitwitter.retrofit.requests.RequestSignUp;
import com.cursoandroid.minitwitter.retrofit.responses.ResponseAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

//con esta interface se van a comunicar el cliente con todos los servicios que hay en la API
public interface MiniTwitterService {

    //petición de Login
    @POST("auth/login")
    Call<ResponseAuth> doLogin(@Body RequestLogin requestLogin);

    //petición de SignUp el parámetro es un objeto de tipo RequestSignUp y RequestLogin
    @POST("auth/signup")
    Call<ResponseAuth> doSignUp(@Body RequestSignUp requestSignUp);
}
