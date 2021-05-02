package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView forgotPasswordTextView;
    MaterialButton loginMaterialButton;
    FirebaseAuth firebaseAuth;
    EditText loginEmailEditText, loginPasswordEditText;
    String email, password;
    DatabaseReference dbRef;
    UserAccount account;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTitleTextView);
        loginMaterialButton = findViewById(R.id.loginAccountMaterialButton);
        loginEmailEditText = findViewById(R.id.editTextLoginEmail);
        loginPasswordEditText = findViewById(R.id.editTextLoginPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
        forgotPasswordTextView.setOnClickListener(v ->
                Toast.makeText(getApplicationContext(), "Forgot Password", Toast.LENGTH_LONG).show()
        );
    }

    public void onLoginClick(View view){
        showDialog();
        email = loginEmailEditText.getText().toString();
        password = loginPasswordEditText.getText().toString();
        if(email.length() <= 0 && password.length() <= 0){
            Toast.makeText(getApplicationContext(), "Enter email and password", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    account = new UserAccount();
                    account.uID = user.getUid();
                    dbRef.child("Users").child(account.uID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            account = snapshot.getValue(UserAccount.class);
                            CurrentUser currentUser = (CurrentUser) getApplicationContext();
                            currentUser.setUserAccount(account);
                            dialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Failed to Login", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
        }
    }

    private void showDialog() {
        builder = new AlertDialog.Builder(this);
        final ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        progressBar.setPadding(50, 50, 50, 50);
        progressBar.setIndeterminate(true);
        progressBar.setLayoutParams(lp);
        dialog = builder.setView(progressBar).setCancelable(false).create();
        dialog.show();
    }

    public void onCreateAccountClick(View view){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

}