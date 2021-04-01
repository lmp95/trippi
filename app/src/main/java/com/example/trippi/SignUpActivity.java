package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    EditText signUpEmailEditText, signUpPasswordEditText, signUpConfirmPasswordEditText;
    AlertDialog.Builder builder;
    FirebaseAuth auth;
    String email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        builder = new AlertDialog.Builder(this);
        signUpEmailEditText = findViewById(R.id.editTextSignUpEmail);
        signUpPasswordEditText = findViewById(R.id.editTextSignUpPassword);
        signUpConfirmPasswordEditText = findViewById(R.id.editTextSignUpConfirmPassword);
        addBackAction();
    }

    public void onSignUpClick(View view){
        email = signUpEmailEditText.getText().toString();
        password = signUpPasswordEditText.getText().toString();
        confirmPassword = signUpConfirmPasswordEditText.getText().toString();
        if(password.equals(confirmPassword) && email.length() > 0){
            showDialog("Are you sure to proceed?", "Confirm");
        }
        else if(!password.equals(confirmPassword)){
            showDialog("Password not match.", "Invalid");
        }
        else{
            showDialog("Enter email and password", "Invalid");
        }
    }

    public void showDialog(String message, String title){
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                        } else {
                            Log.d("TAG", "showDialog: " + task.getException());
                        }
                    });
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }

    private void addBackAction() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Create Account");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}