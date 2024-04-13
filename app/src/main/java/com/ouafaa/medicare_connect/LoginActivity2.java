package com.ouafaa.medicare_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity2 extends AppCompatActivity {
    TextView textView;

    EditText email1, motpasse1;
    MaterialButton btnEnregistrer;
    String email1Pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageView googleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        textView = findViewById(R.id.textView);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        email1 = findViewById(R.id.email1);
        motpasse1 = findViewById(R.id.motpasse1);
        progressDialog = new ProgressDialog(this);
        googleButton=findViewById(R.id.googleButton);
        btnEnregistrer = findViewById(R.id.btnEnregistrer);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity2.this, registerActivity2.class));
            }
        });
        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforLogin();
            }
        });
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity2.this, GoogleSignActivity2.class);
                startActivity(intent);
            }
        });
    }

    private void PerforLogin() {
        String Email = email1.getText().toString();
        String Mot_de_passe = motpasse1.getText().toString();
        boolean isValid = true;

        if (!Email.matches(email1Pattern)) {
            email1.setError("Entrer un email correct");
            isValid = false;

        }

        if (Mot_de_passe.isEmpty() || Mot_de_passe.length() < 7) {
            motpasse1.setError("Entrer votre propre password");
            isValid = false;
        }
        if (isValid) {
            progressDialog.setMessage("Svp attendez la Connexion...");
            progressDialog.setTitle("Connexion");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(Email, Mot_de_passe).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        sendUserToNextActivity();
                        Toast.makeText(LoginActivity2.this, "Connexion avec succès", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LoginActivity2.this, ""+task.getException(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }
    public void sendUserToNextActivity() {
        Intent intent = new Intent(LoginActivity2.this, HomeActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}


