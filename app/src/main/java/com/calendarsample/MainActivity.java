package com.calendarsample;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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

public class MainActivity extends AppCompatActivity {

    EditText email,password;
    Button register,forgotpass,signin;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    String emailPattern = "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+";
    TextInputLayout textinput_email,textinput_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setAsAction();
    }

    private void init()
    {
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        forgotpass=findViewById(R.id.forgotpass);
        signin=findViewById(R.id.signin);
        progressBar=findViewById(R.id.progressBar);
        textinput_email=findViewById(R.id.textinput_email);
        textinput_pass=findViewById(R.id.textinput_pass);
        firebaseAuth= FirebaseAuth.getInstance();
    }

    private void setAsAction()
    {
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(validateFields())
                {
                    registerNow();
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this,ForgotPassword.class));
            }
        });

    }

    private void registerNow()
    {
        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful())
                        {
                            Intent intent=new Intent(MainActivity.this,Home.class);
                            intent.putExtra("Email",email.getText().toString());
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateFields()
    {
        boolean fail=false;
        if(TextUtils.isEmpty(email.getText().toString()) || !email.getText().toString().matches(emailPattern))
        {
            fail=true;
            textinput_email.setError("Enter Valid Email");
            email.setFocusable(true);
        }

        if(TextUtils.isEmpty(password.getText().toString()))
        {
            fail=true;
            textinput_pass.setError("Enter Valid Password");
            password.setFocusable(true);
        }

        if(password.getText().toString().length()<6)
        {
            fail=true;
            textinput_pass.setError("Password too short, enter minimum 6 characters!");
            password.setFocusable(true);
        }

        if(!fail)
        {
            textinput_email.setErrorEnabled(false);
            textinput_pass.setErrorEnabled(false);
            return true;
        }
        else
        {
            return false;
        }
    }
}
