package com.gianmoura.booker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SigninActivity extends Activity {

    FirebaseAuth firebaseAuth;
    @BindView(R.id.editName)
    EditText name;
    @BindView(R.id.editEmail)
    EditText email;
    @BindView(R.id.editPass)
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        if (!Utils.isOnline(this)){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnCadastrar)
    public void signin(){
        BookerUser user = new BookerUser();
        user.setName(name.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        signinFirebaseUser(user);
    }

    private void signinFirebaseUser(final BookerUser user){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    user.setId(firebaseUser.getUid());
                    user.save();
                    Toast.makeText(SigninActivity.this, "Conta criada com sucesso.", Toast.LENGTH_LONG).show();
                    Intent intent =  new Intent(SigninActivity.this, MainActivity.class);
                    startActivity( intent );
                    finish();
                }else {
                    String erro = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erro = "Digite uma senha mais forte, contendo no minimo 6 caracteres, com letras e numeros.";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "Email inválido.";
                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "Este email já está em uso.";
                    } catch (Exception e) {
                        erro = "Ao cadastrar usuário.";
                        e.printStackTrace();
                    }
                    Toast.makeText(SigninActivity.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
