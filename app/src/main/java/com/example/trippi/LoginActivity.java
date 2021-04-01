package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView forgotPasswordTextView;
    MaterialButton loginMaterialButton;
    FirebaseAuth firebaseAuth;
    EditText loginEmailEditText, loginPasswordEditText;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTitleTextView);
        loginMaterialButton = findViewById(R.id.loginAccountMaterialButton);
        loginEmailEditText = findViewById(R.id.editTextLoginEmail);
        loginPasswordEditText = findViewById(R.id.editTextLoginPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        forgotPasswordTextView.setOnClickListener(v ->
                Toast.makeText(getApplicationContext(), "Forgot Password", Toast.LENGTH_LONG).show()
        );
    }

    public void onLoginClick(View view){
        email = loginEmailEditText.getText().toString();
        password = loginPasswordEditText.getText().toString();
        if(email.length() <= 0 && password.length() <= 0){
            Toast.makeText(getApplicationContext(), "Enter email and password", Toast.LENGTH_LONG).show();
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Failed to Login", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void onCreateAccountClick(View view){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

}