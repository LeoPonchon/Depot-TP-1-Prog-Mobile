package fds.gl.tp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class ExercisesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        // on récupère toutes les cartes d'exercices
        View cardEx1 = findViewById(R.id.card_ex1);
        View cardEx2 = findViewById(R.id.card_ex2);
        View cardEx3 = findViewById(R.id.card_ex3);
        View cardEx4 = findViewById(R.id.card_ex4);
        View cardEx5 = findViewById(R.id.card_ex5);
        View cardEx6 = findViewById(R.id.card_ex6);

        // clique sur l'exo 1 pour voir le Hello World
        cardEx1.setOnClickListener(v -> {
            Intent intent = new Intent(ExercisesActivity.this, Exo1Activity.class);
            startActivity(intent);
        });

        // clique sur l'exo 2 pour voir les infos de l'app
        cardEx2.setOnClickListener(v -> {
            Intent intent = new Intent(ExercisesActivity.this, Exo2Activity.class);
            startActivity(intent);
        });

        // clique sur l'exo 3 pour le formulaire
        cardEx3.setOnClickListener(v -> {
            Intent intent = new Intent(ExercisesActivity.this, Exo3Activity.class);
            startActivity(intent);
        });

        // clique sur l'exo 4 pour changer la langue
        cardEx4.setOnClickListener(v -> {
            Intent intent = new Intent(ExercisesActivity.this, Exo4Activity.class);
            startActivity(intent);
        });

        // clique sur l'exo 5 pour la popup de confirmation
        cardEx5.setOnClickListener(v -> {
            Intent intent = new Intent(ExercisesActivity.this, Exo5Activity.class);
            startActivity(intent);
        });

        // clique sur l'exo 6 pour les intents
        cardEx6.setOnClickListener(v -> {
            Intent intent = new Intent(ExercisesActivity.this, Exo6Activity.class);
            startActivity(intent);
        });
    }
}
