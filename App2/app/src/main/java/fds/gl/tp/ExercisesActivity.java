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
        View cardEx8 = findViewById(R.id.card_ex8);

        // clique sur l'exo 8 pour les horaires de train
        cardEx8.setOnClickListener(v -> {
            Intent intent = new Intent(ExercisesActivity.this, Exo8Activity.class);
            startActivity(intent);
        });
    }
}
