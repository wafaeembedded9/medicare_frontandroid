package com.ouafaa.medicare_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordActivity2 extends AppCompatActivity {

    EditText emailEditText;
    Button resetPasswordButton;
    View backButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password2);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.PasswordButton);
        backButton = findViewById(R.id.backButton);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retourner à l'activité de connexion
                finish();
            }
        });
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Veuillez entrer votre adresse e-mail", Toast.LENGTH_SHORT).show();
            return;
        }

        // Récupérer l'utilisateur actuellement connecté
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // Envoyer un e-mail de réinitialisation du mot de passe
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PasswordActivity2.this, "Un e-mail de réinitialisation de mot de passe a été envoyé à votre adresse e-mail", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PasswordActivity2.this, "Échec de l'envoi de l'e-mail de réinitialisation de mot de passe. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Aucun utilisateur connecté", Toast.LENGTH_SHORT).show();
        }
    }
}
