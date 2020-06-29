package com.example.realtimechat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.realtimechat.model.RegisterHTTP;
import com.example.realtimechat.model.User;

import java.util.concurrent.ExecutionException;

public class Register extends AppCompatActivity {

    Button btnRegister;
    EditText edtUsername;
    EditText edtPassword;
    EditText edtRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        edtRePassword = findViewById(R.id.re_password);
        edtRePassword.addTextChangedListener(validateTextFields);

        edtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && edtUsername.getText().length() < 4) {
                    alertMessage("O usuário deve conter pelo menos 4 caracteres.");
                    edtUsername.setText("");
                }
            }
        });

        edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && edtUsername.getText().length() < 4) {
                    alertMessage("A senha deve conter pelo menos 4 caracteres.");
                    edtPassword.setText("");
                }
            }
        });

        edtRePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !edtPassword.getText().toString()
                                .equals(edtRePassword.getText().toString())){
                    alertMessage("As senhas não batem.");
                    edtRePassword.setText("");
                }
            }
        });

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setEnabled(false);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        User user = new User(username, password);
        RegisterHTTP login = new RegisterHTTP(user);
        int status = 0;
        try {
            status = login.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(status == 400) {
            alertMessage("O nome de usuário já existe.");
        } else if (status == 502){
            alertMessage("Não foi possível se conectar com o servidor. Verifique sua conexão de internet.");
        } else if (status == 200) {
            alertMessage("Cadastro realizado com sucesso.");
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            alertMessage("Ocorreu um erro inesperado.");
        }
    }

    private void alertMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void hideInput(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void checkValidationFields() {
        if (edtUsername.getText().length() < 4 || edtPassword.getText().length() < 4
                || !edtPassword.getText().toString().equals(edtRePassword.getText().toString())) {
            btnRegister.setEnabled(false);
        } else {
            btnRegister.setEnabled(true);
        }
    }
}
