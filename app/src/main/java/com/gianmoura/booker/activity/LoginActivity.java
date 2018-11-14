package com.gianmoura.booker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gianmoura.booker.R;
import com.gianmoura.booker.config.FirebaseConfig;
import com.gianmoura.booker.helper.BackgroundTask;
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
    BookerUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (!Utils.isOnline(this)){
            startActivity(new Intent(this, AlertActivity.class));
            finish();
        }
        ButterKnife.bind(this);
        user = new BookerUser();
    }

    public void registerRedirect(View view){
        startActivity( new Intent(this, SigninActivity.class) );
    }

    @OnClick(R.id.btnLogin)
    public void login(){
        if(email.getText().length() == 0 || password.getText().length() == 0){
            Toast.makeText(this, "Existem campos obrigatórios não preenchidos.", Toast.LENGTH_LONG).show();
            return;
        }
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        new AuthenticationTask(this).execute();
    }

    private class AuthenticationTask extends BackgroundTask {

        public AuthenticationTask(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            validateLogin();
            super.doInBackground(params);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

    private void validateLogin() {
        FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Você foi logado com sucesso.", Toast.LENGTH_SHORT).show();
                    startActivity( new Intent( LoginActivity.this, MainActivity.class) );
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
