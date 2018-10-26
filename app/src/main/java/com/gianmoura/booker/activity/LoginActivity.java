package com.gianmoura.booker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gianmoura.booker.R;
import com.gianmoura.booker.config.FirebaseConfig;
import com.gianmoura.booker.helper.Utils;
import com.gianmoura.booker.model.BookerUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {
    @BindView(R.id.txtEmail)
    EditText email;
    @BindView(R.id.txtSenha)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (!Utils.isOnline(this)){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        ButterKnife.bind(this);
    }

    public void signinRedirect(View view){
        startActivity( new Intent(this, SigninActivity.class) );
    }

    @OnClick(R.id.btnLogin)
    public void login(){
        BookerUser user = new BookerUser();
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        validateLogin(user);
    }

    private void validateLogin(BookerUser user) {
        FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Você foi logado com sucesso.", Toast.LENGTH_SHORT).show();
                    startActivity( new Intent( LoginActivity.this, BookFilterActivity.class) );
                    finish();
                }else {
                    String erro = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "Email ou senha incorretos.";
                    }catch (Exception e){
                        erro = "Processo de login falhou.";
                    }
                    Toast.makeText(LoginActivity.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
