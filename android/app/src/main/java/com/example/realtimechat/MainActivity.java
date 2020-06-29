package com.example.realtimechat;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.realtimechat.controller.SocketController;
import com.example.realtimechat.controller.TokenController;
import com.example.realtimechat.model.ValidateTokenHTTP;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TokenController tokenController = TokenController.getInstance();


        if(tokenController.isEmpty()){
            goToLogin();
        } else {
            validateToken();
        }

    }

    private void alertMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void validateToken() {
        ValidateTokenHTTP validateToken = new ValidateTokenHTTP();
        try {
            int status = validateToken.execute().get();
            if(status == 401) {
                SocketController.clearInstance();
                goToLogin();
                alertMessage("Token expirado.");
            } else if (status == 502){
                alertMessage("Não foi possível se conectar com o servidor. Verifique sua conexão de internet.");
            } else if (status == 200) {
                goToHome();
            } else {
                alertMessage("Ocorreu um erro inesperado." + status);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void goToHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
