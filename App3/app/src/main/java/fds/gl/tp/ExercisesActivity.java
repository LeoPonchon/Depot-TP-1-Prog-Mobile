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
        View cardEx9 = findViewById(R.id.card_ex9);

        // clique sur l'exo 9 pour l'agenda
        cardEx9.setOnClickListener(v -> {
            Intent intent = new Intent(ExercisesActivity.this, Exo9Activity.class);
            startActivity(intent);
        });
    }
}
