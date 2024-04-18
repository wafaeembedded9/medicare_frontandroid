package com.ouafaa.medicare_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity2 extends AppCompatActivity {
    VideoView videoView;
    CardView cardView;
    TextView surveillanceTextView; // Déclarer le TextView
    TextView methodTextView; // Déclarer le TextView pour les méthodes d'utilisation
    TextView conseilsTextView; // Déclarer le TextView pour les conseils

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        // Initialiser le VideoView
        videoView = findViewById(R.id.videoview);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video3);
        videoView.setVideoURI(uri);
        videoView.start();

        // Définir une boucle pour la vidéo
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        // Gérer le clic sur le CardView
        cardView = findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers SurveillanceActivity2 lors du clic sur le CardView
                Intent intent = new Intent(HomeActivity2.this, surveillanceActivity2.class);
                startActivity(intent);
            }
        });

        // Gérer le clic sur le CardView4
        CardView cardView4 = findViewById(R.id.cardView4);
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers MethodActivity2 lors du clic sur le CardView4
                Intent intent = new Intent(HomeActivity2.this, MethodActivity2.class);
                startActivity(intent);
            }
        });

        // Gérer le clic sur le CardView2
        CardView cardView2 = findViewById(R.id.cardView2);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers ConseilsActivity2 lors du clic sur le CardView2
                Intent intent = new Intent(HomeActivity2.this, ConseilsActivity2.class);
                startActivity(intent);
            }
        });

        // Gérer le clic sur le CardView3
        CardView cardView3 = findViewById(R.id.cardView3);
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers LoginActivity2 lors du clic sur le CardView3
                Intent intent = new Intent(HomeActivity2.this, LoginActivity2.class);
                startActivity(intent);
            }
        });

        // Gérer le clic sur le TextView "Surveillance medicale"
        surveillanceTextView = findViewById(R.id.Surveillancee);
        surveillanceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers SurveillanceActivity2 lors du clic sur le TextView
                Intent intent = new Intent(HomeActivity2.this, surveillanceActivity2.class);
                startActivity(intent);
            }
        });

        // Gérer le clic sur le TextView "Méthodes d'utilisation"
        methodTextView = findViewById(R.id.utilisation);
        methodTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers MethodActivity2 lors du clic sur le TextView
                Intent intent = new Intent(HomeActivity2.this, MethodActivity2.class);
                startActivity(intent);
            }
        });

        // Gérer le clic sur le TextView "Conseils"
        conseilsTextView = findViewById(R.id.textView11);
        conseilsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers ConseilsActivity2 lors du clic sur le TextView
                Intent intent = new Intent(HomeActivity2.this, ConseilsActivity2.class);
                startActivity(intent);
            }
        });

        // Gérer le clic sur le TextView "Déconnexion"
        View textView12 = findViewById(R.id.textView12);
        textView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers LoginActivity2 lors du clic sur le TextView
                Intent intent = new Intent(HomeActivity2.this, LoginActivity2.class);
                startActivity(intent);
            }
        });

        // Appeler la méthode pour afficher le nom d'utilisateur
        afficherNomUtilisateur();
    }

    // Méthode pour afficher le nom d'utilisateur
    private void afficherNomUtilisateur() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
            currentUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("username").getValue(String.class);
                        // Afficher le nom d'utilisateur dans un TextView ou tout autre composant approprié
                        TextView textViewNomUtilisateur = findViewById(R.id.textView13);
                        textViewNomUtilisateur.setSingleLine(true); // Définit le TextView pour n'afficher qu'une seule ligne
                        textViewNomUtilisateur.setText("Bonjour  " +username);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Gérer les erreurs éventuelles
                }
            });
        }
    }

    // Gérer la reprise de la vidéo
    @Override
    protected void onResume() {
        videoView.resume();
        super.onResume();
    }

    // Gérer le redémarrage de la vidéo
    @Override
    protected void onRestart() {
        videoView.start();
        super.onRestart();
    }

    // Gérer la pause de la vidéo
    @Override
    protected void onPause() {
        videoView.suspend();
        super.onPause();
    }

    // Gérer la fin de la vidéo
    @Override
    protected void onDestroy() {
        videoView.stopPlayback();
        super.onDestroy();
    }
}
