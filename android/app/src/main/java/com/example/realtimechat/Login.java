package com.example.realtimechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimechat.model.LoginHTTP;
import com.example.realtimechat.model.User;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    Button btnLogin;
    EditText edtUsername;
    EditText edtPassword;
    TextView tv_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextWatcher validateTextFields = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkValidationFields();
            }
        };

        edtUsername = findViewById(R.id.username);
        edtUsername.addTextChangedListener(validateTextFields);
        edtPassword = findViewById(R.id.password);
        edtPassword.addTextChangedListener(validateTextFields);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setEnabled(false);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInput(v);
                login();
            }
        });
        tv_register = findViewById(R.id.tv_register);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegister();
            }
        });
    }

    private void hideInput(View view){
        if(view!=null){
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void alertMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void goRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    private void login() { ;
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        User user = new User(username, password);
        LoginHTTP login = new LoginHTTP(user);
        int status = 0;
        try {
            status = login.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(status == 401) {
            alertMessage("O usurio digitado não existe.");
        } else if (status == 403) {
            alertMessage("A senha digitada é inválida.");
        } else if (status == 502){
            alertMessage("Não foi possível se conectar com o servidor. Verifique sua conexão de internet.");
        } else if (status == 200) {
            alertMessage("Login efetuado com sucesso.");
            Intent intent = new Intent(this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            alertMessage("Ocorreu um erro inesperado.");
        }
    }

    private void checkValidationFields() {
        if (edtUsername.getText().length() < 4 || edtPassword.getText().length() < 4) {
            btnLogin.setEnabled(false);
        } else {
            btnLogin.setEnabled(true);
        }
    }
}
