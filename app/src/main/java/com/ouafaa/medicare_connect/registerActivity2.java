package com.ouafaa.medicare_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class registerActivity2 extends AppCompatActivity {
    TextView textView3;
    EditText inputUsername2, inputEmail2, inputMotpasse2, inputConfirm;
    MaterialButton btnEnregistrer;
    String EmailPattern =  "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        textView3 = findViewById(R.id.textView3);
        inputUsername2 = findViewById(R.id.inputUsername2);
        inputEmail2 = findViewById(R.id.inputEmail2);
        inputMotpasse2 = findViewById(R.id.inputMotpasse2);
        inputConfirm = findViewById(R.id.inputConfirm);
        btnEnregistrer = findViewById(R.id.btnEnregistrer);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registerActivity2.this, LoginActivity2.class));
            }
        });

        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                PerforAuth();
            }
        });
    }

    private void PerforAuth() {
        final String Nom_d_utilisateur = inputUsername2.getText().toString().trim();
        final String Email = inputEmail2.getText().toString().trim();
        final String Mot_de_passe = inputMotpasse2.getText().toString().trim();
        String Confirmation_du_mot_de_passe = inputConfirm.getText().toString().trim();

        boolean isValid = true;

        if (Nom_d_utilisateur.isEmpty() || Nom_d_utilisateur.length() < 7) {
            inputUsername2.setError("Nom d'utilisateur invalide!");
            isValid = false;
        }

        if (!Email.matches(EmailPattern)) {
            inputEmail2.setError("Entrer un email correct");
            isValid = false;
        }

        if (Mot_de_passe.isEmpty() || Mot_de_passe.length() < 7) {
            inputMotpasse2.setError("Entrer votre propre password");
            isValid = false;
        }

        if (Confirmation_du_mot_de_passe.isEmpty()) {
            inputConfirm.setError("Veuillez confirmer votre mot de passe");
            isValid = false;
        }

        if (!Mot_de_passe.equals(Confirmation_du_mot_de_passe)) {
            inputConfirm.setError("Le mot de passe ne correspond pas aux deux champs");
            isValid = false;
        }

        if (isValid) {
            progressDialog.setMessage("Svp attendez l'Enregistrement...");
            progressDialog.setTitle("Enregistrement");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            // Vérifier si l'email est déjà utilisé
            mDatabaseReference.orderByChild("email").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        progressDialog.dismiss();
                        Toast.makeText(registerActivity2.this, "Cet email est déjà utilisé. Veuillez en choisir un autre.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Créer un nouveau compte utilisateur
                        mAuth.createUserWithEmailAndPassword(Email, Mot_de_passe).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(registerActivity2.this, "Registration avec succès",Toast.LENGTH_SHORT).show();
                                    // Ajouter les informations de l'utilisateur à la base de données Firebase
                                    addUserToDatabase(Nom_d_utilisateur, Email, Mot_de_passe);
                                    // Ne redirige pas vers une autre activité après l'enregistrement
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(registerActivity2.this,"Erreur: "+task.getException(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(registerActivity2.this, "Erreur: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void addUserToDatabase(String username, String email, String password) {
        String userID = mAuth.getCurrentUser().getUid();
        DatabaseReference currentUserDb = mDatabaseReference.child(userID);
        currentUserDb.child("username").setValue(username);
        currentUserDb.child("email").setValue(email);
        currentUserDb.child("password").setValue(password);
    }
}
