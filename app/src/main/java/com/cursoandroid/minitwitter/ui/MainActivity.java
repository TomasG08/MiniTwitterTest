package com.cursoandroid.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.minitwitter.R;
import com.cursoandroid.minitwitter.retrofit.MiniTwitterClient;
import com.cursoandroid.minitwitter.retrofit.MiniTwitterService;
import com.cursoandroid.minitwitter.retrofit.requests.RequestLogin;
import com.cursoandroid.minitwitter.retrofit.responses.ResponseAuth;
import com.cursoandroid.minitwitter.utilities.Constantes;
import com.cursoandroid.minitwitter.utilities.SharedPreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnIniciarSesion;
    TextView textViewRegistrarse;
    EditText etEmail, etPassword;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //oculta el toolbar
        getSupportActionBar().hide();


        retrofitInit();
        findViews();
        clickEvents();

    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    private void findViews() {
        btnIniciarSesion = findViewById(R.id.button_iniciar_sesion);
        textViewRegistrarse = findViewById(R.id.textView_ir_registrarse);
        etEmail = findViewById(R.id.editText_email);
        etPassword = findViewById(R.id.editText_password);
    }

    private void clickEvents() {
        btnIniciarSesion.setOnClickListener(this);
        textViewRegistrarse.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.button_iniciar_sesion:
                goToLogin();
                break;
            case R.id.textView_ir_registrarse:
                irAlRegistroDeUsuario();
                break;
        }
    }

    private void goToLogin() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(email.isEmpty() && password.isEmpty()){
            etEmail.setError("Campo obligatorio");
            etPassword.setError("Campo obligatorio");
        }
        else if(email.isEmpty())
            etEmail.setError("Campo obligatorio");
        else if(password.isEmpty())
            etPassword.setError("Campo obligatorio");
        else{
            RequestLogin requestLogin = new RequestLogin(email, password);
            Call<ResponseAuth> call = miniTwitterService.doLogin(requestLogin);

            //asincrona
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();

                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_ACTIVE, response.body().getActive());


                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        //destruimos este activity para que no se pueda regresar.
                        finish();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Ocurrió un error, revisa tus datos.", Toast.LENGTH_SHORT).show();
                    }
                    
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Problemas de conexión, intentelo de nuevo", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void irAlRegistroDeUsuario() {
        Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
        startActivity(intent);
        finish();
    }
}