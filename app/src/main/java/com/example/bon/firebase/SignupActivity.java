package com.example.bon.firebase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.RegionIterator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText emailedit,passedit;
    private Button btnsignup,btninsert,btnview;
    private FirebaseAuth mAuth;
    private DatabaseReference  databaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth=FirebaseAuth.getInstance();

        emailedit=findViewById(R.id.email);
        passedit=findViewById(R.id.password);
        btnsignup=findViewById(R.id.btnsignup);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sigin();
            }
        });

        btninsert=findViewById(R.id.btninsert);
        btnview=findViewById(R.id.btnview);
        databaseUser = FirebaseDatabase.getInstance().getReference();

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertData();
            }
        });
        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,Userlist.class));
            }
        });

    }

    private void InsertData() {
        String email = emailedit.getText().toString();
        String pass = passedit.getText().toString();
        String id =databaseUser.push().getKey();

        User user =new User(email,pass);
        databaseUser.child("users").child(id).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupActivity.this,"User details Inserted",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sigin() {
        String email,pass;
        email=emailedit.getText().toString();
        pass=passedit.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Vui lòng nhập email!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Vui lòng nhập password",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Tạo tài khoản thành công",Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(SignupActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"Tạo tài khoản không thành công",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
