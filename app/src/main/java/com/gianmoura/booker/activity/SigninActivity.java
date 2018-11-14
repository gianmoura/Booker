package com.gianmoura.booker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gianmoura.booker.R;
import com.gianmoura.booker.config.FirebaseConfig;
import com.gianmoura.booker.helper.BackgroundTask;
import com.gianmoura.booker.helper.Utils;
import com.gianmoura.booker.model.BookerUser;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final String TAG = "ADDRESS";
    @BindView(R.id.txtSelectedAddress)
    TextView address;
    FirebaseAuth firebaseAuth;
    @BindView(R.id.editName)
    EditText name;
    @BindView(R.id.editEmail)
    EditText email;
    @BindView(R.id.editPass)
    EditText password;
    BookerUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        if (!Utils.isOnline(this)){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        ButterKnife.bind(this);
        user  = new BookerUser();
    }

    @OnClick(R.id.btnSearchAddress)
    public void searchAddress(){
        openAutocompleteActivity();
    }

    @OnClick(R.id.btnCadastrar)
    public void signin(){
        if(address.getText().length() == 0 || name.getText().length() == 0 || email.getText().length() == 0 || password.getText().length() == 0){
            Toast.makeText(this, "Existem campos obrigatórios não preenchidos.", Toast.LENGTH_LONG).show();
            return;
        }
        user.setName(name.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        new CreateUserTask(this).execute();
    }

    private class CreateUserTask extends BackgroundTask{

        public CreateUserTask(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            saveFirebaseUser();
            super.doInBackground(params);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

    public void saveFirebaseUser(){
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

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                address.setText(place.getAddress());
                user.setAddress(place.getAddress().toString());
                user.setLat(place.getLatLng().latitude);
                user.setLng(place.getLatLng().longitude);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
                Log.e(TAG, "Error: Status = Canceled");
            }
        }
    }

    public void loginRedirect(View view){
        startActivity( new Intent(this, LoginActivity.class) );
    }
}
