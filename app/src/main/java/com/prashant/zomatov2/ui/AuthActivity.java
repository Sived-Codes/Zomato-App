package com.prashant.zomatov2.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.prashant.zomatov2.R;
import com.prashant.zomatov2.firebase.Constant;
import com.prashant.zomatov2.model.UserModel;

public class AuthActivity extends AppCompatActivity {


    EditText mobile, fullName;
    MaterialButton loginBtn;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        mobile = findViewById(R.id.userMobile);
        fullName = findViewById(R.id.userName);

        loginBtn = findViewById(R.id.loginBtn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.pd).setVisibility(View.VISIBLE);

                String mobileTxt = mobile.getText().toString();
                String fullNameTxt = fullName.getText().toString();

                if (mobileTxt.isEmpty()) {
                    mobile.requestFocus();
                    findViewById(R.id.pd).setVisibility(View.GONE);

                    mobile.setError("Empty !");
                } else if (fullNameTxt.isEmpty()) {
                    fullName.requestFocus();
                    fullName.setError("Empty !");
                    findViewById(R.id.pd).setVisibility(View.GONE);

                } else {
                    UserModel model = new UserModel();

                    model.setFullName(fullNameTxt);
                    model.setMobile(mobileTxt);
                    model.setUserType("user");

                    Constant.USER_DB.child(mobileTxt).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                Intent intent = new Intent(AuthActivity.this, DashboardActivity.class);
                                intent.putExtra("user", model.getUserType() );
                                startActivity(intent);
                                Toast.makeText(AuthActivity.this, "Logged In ", Toast.LENGTH_SHORT).show();
                            }
                            findViewById(R.id.pd).setVisibility(View.GONE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            findViewById(R.id.pd).setVisibility(View.GONE);
                            Toast.makeText(AuthActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        });

    }
}