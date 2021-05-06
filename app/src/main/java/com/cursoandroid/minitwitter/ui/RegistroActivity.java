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
import com.cursoandroid.minitwitter.retrofit.requests.RequestSignUp;
import com.cursoandroid.minitwitter.retrofit.responses.ResponseAuth;
import com.cursoandroid.minitwitter.utilities.Constantes;
import com.cursoandroid.minitwitter.utilities.SharedPreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegistrarse;
    TextView textViewIniciarSesion;
    EditText etEmail, etPassword, etUsername;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        getSupportActionBar().hide();


        retrofitInit();
        findViews();
        clickEvents();


    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    public void findViews(){
        btnRegistrarse = findViewById(R.id.button_registrarse);
        textViewIniciarSesion = findViewById(R.id.textView_ir_iniciarSesion);
        etEmail = findViewById(R.id.editText_email);
        etPassword = findViewById(R.id.editText_password);
        etUsername = findViewById(R.id.editText_username);
    }
    private void clickEvents() {
        btnRegistrarse.setOnClickListener(this);
        textViewIniciarSesion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.button_registrarse:
                goToSignUp();
                break;
            case R.id.textView_ir_iniciarSesion:
                irAlInicioDeSesion();
                break;
        }
    }

    private void goToSignUp() {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(email.isEmpty() && password.isEmpty() && username.isEmpty()){
            etUsername.setError("Campo obligatorio");
            etEmail.setError("Campo obligatorio");
            etPassword.setError("Campo obligatorio");
        }
        else if(username.isEmpty())
            etUsername.setError("Campo obligatorio");
        else if(email.isEmpty())
            etEmail.setError("Campo obligatorio");
        else if(password.isEmpty() || password.length() < 4)
            etPassword.setError("Campo obligatorio *La contraseña debe de tener más de 4 dígitos*");
        else{
            RequestSignUp requestSignUp = new RequestSignUp(username, email, password, Constantes.CODIGO_INGRESO);
            Call<ResponseAuth> call = miniTwitterService.doSignUp(requestSignUp);
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(RegistroActivity.this, "Registro exitoso!", Toast.LENGTH_SHORT).show();

                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_ACTIVE, response.body().getActive());

                        Intent intent = new Intent(RegistroActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(RegistroActivity.this, "Algo ha ido mal, revisa los datos", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(RegistroActivity.this, "Ocurrió un error con la conexión, inténtalo de nuevo", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void irAlInicioDeSesion() {
        Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

}