package com.cursoandroid.minitwitter.retrofit;

import com.cursoandroid.minitwitter.utilities.Constantes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MiniTwitterClient {
    private static MiniTwitterClient instance = null;
    private MiniTwitterService miniTwitterService;
    private Retrofit retrofit;

    public MiniTwitterClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        miniTwitterService = retrofit.create(MiniTwitterService.class);
    }


    //Patrón Singleton esto provoca que sólo se crea 1 vez la instancia al Objeto MiniTwitterClient
    public static MiniTwitterClient getInstance(){

        if(instance == null){
            instance = new MiniTwitterClient();
        }
        return instance;
    }

    //nos permitirá consumir los servicios que tenemos definidos en nuestra API

    public MiniTwitterService getMiniTwitterService(){
        return miniTwitterService;
    }

}
