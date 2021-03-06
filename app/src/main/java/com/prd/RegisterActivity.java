package com.prd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText regEmail;
    private EditText regPass;
    private EditText regConfirmPass;
    private Button regBtn1;
    private Button regBtn2;
    private ProgressBar regProgress;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        regEmail = findViewById(R.id.reg_email);
        regPass = findViewById(R.id.reg_pass);
        regConfirmPass = findViewById(R.id.reg_confirm_pass);
        regBtn1 = findViewById(R.id.reg_btn1);
        regBtn2 = findViewById(R.id.reg_btn2);
        regProgress = findViewById(R.id.reg_progress);



        regBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        regBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = regEmail.getText().toString();
                String pass = regPass.getText().toString();
                String confirm_pass = regConfirmPass.getText().toString();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass) & TextUtils.isEmpty(confirm_pass)) {
                    Toast.makeText(getApplication(), "Enter details to register", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplication(), "Enter details to register", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(confirm_pass)) {
                    Toast.makeText(getApplication(), "Enter details to register", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(pass) && TextUtils.isEmpty(confirm_pass)) {
                    Toast.makeText(getApplication(), "Enter details to register", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter details to register", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplication(), "Enter details to register", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(confirm_pass)) {
                    Toast.makeText(getApplication(), "Enter details to register", Toast.LENGTH_LONG).show();
                    return;
                }



                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) & !TextUtils.isEmpty(confirm_pass)) {
                    if(pass.equals(confirm_pass)){

                        regProgress.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
                                    startActivity(setupIntent);
                                    finish();


                                } else {

                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                                }
                                regProgress.setVisibility(View.INVISIBLE);
                            }
                        });


                    } else {

                        Toast.makeText(RegisterActivity.this, "Confirm Password and Password Field does not match!", Toast.LENGTH_LONG).show();

                    }

                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            sendToMain();

        }
    }

    private void sendToMain() {

        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
