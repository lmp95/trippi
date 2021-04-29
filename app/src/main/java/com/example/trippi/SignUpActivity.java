package com.example.trippi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText signUpEmailEditText, signUpPasswordEditText,
            signUpConfirmPasswordEditText, signUpNameEditText, signUpPhoneEditText;
    AlertDialog.Builder builder;
    FirebaseAuth auth;
    String email, password, confirmPassword, name, phone;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
        builder = new AlertDialog.Builder(this);
        signUpEmailEditText = findViewById(R.id.editTextSignUpEmail);
        signUpPasswordEditText = findViewById(R.id.editTextSignUpPassword);
        signUpConfirmPasswordEditText = findViewById(R.id.editTextSignUpConfirmPassword);
        signUpNameEditText = findViewById(R.id.editTextSignUpName);
        signUpPhoneEditText = findViewById(R.id.editTextSignUpPhone);
        addBackAction();
    }

    public void onSignUpClick(View view){
        email = signUpEmailEditText.getText().toString();
        password = signUpPasswordEditText.getText().toString();
        confirmPassword = signUpConfirmPasswordEditText.getText().toString();
        name = signUpNameEditText.getText().toString();
        phone = "+95" + signUpPhoneEditText.getText().toString();
        if(password.equals(confirmPassword) && email.length() > 0 && name.length() > 0 && phone.length() > 0){
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
                            UserAccount userAccount = new UserAccount(auth.getUid(), name, email, phone, null);
                            dbRef.child("Users").child(auth.getUid()).setValue(userAccount);
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