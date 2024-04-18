package com.ouafaa.medicare_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity2 extends AppCompatActivity {

    EditText email1, motpasse1;
    MaterialButton btnEnregistrer, googleButton;
    CheckBox showPasswordCheckBox; // Ajout du CheckBox pour afficher le mot de passe
    String email1Pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        email1 = findViewById(R.id.email1);
        motpasse1 = findViewById(R.id.motpasse1);
        progressDialog = new ProgressDialog(this);
        btnEnregistrer = findViewById(R.id.btnEnregistrer);
        View googleBoutton = findViewById(R.id.googleBoutton);
        TextView textView = findViewById(R.id.textView);
        TextView forgotPasswordTextView = findViewById(R.id.textView5); // Ajout du TextView pour le mot de passe oublié
        showPasswordCheckBox = findViewById(R.id.checkBox); // Initialisation du CheckBox

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // Ajout du listener au CheckBox pour afficher ou masquer le mot de passe
        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Si le CheckBox est coché, affichez le mot de passe
                    motpasse1.setTransformationMethod(null);
                } else {
                    // Sinon, masquez le mot de passe
                    motpasse1.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforLogin();
            }
        });

        // Configuration de la connexion Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleBoutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancez l'interface de sélection de compte Google à chaque clic sur le bouton Google
                signInWithGoogle();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity2.this, registerActivity2.class));
            }
        });

        // Gestion du clic sur le texte "Mot de passe oublié ?"
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrir l'activité PasswordActivity2
                startActivity(new Intent(LoginActivity2.this, PasswordActivity2.class));
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
                    } else {
                        Toast.makeText(LoginActivity2.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void signInWithGoogle() {
        // Vérifiez d'abord si un utilisateur est déjà connecté
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Si un utilisateur est déjà connecté, déconnectez-le
            mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // Lancez ensuite l'interface de sélection de compte Google
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });
        } else {
            // Si aucun utilisateur n'est connecté, lancez directement l'interface de sélection de compte Google
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Toast.makeText(LoginActivity2.this, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendUserToNextActivity();
                            Toast.makeText(LoginActivity2.this, "Google Sign In Success", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity2.this, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void sendUserToNextActivity() {
        Intent intent = new Intent(LoginActivity2.this, HomeActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
