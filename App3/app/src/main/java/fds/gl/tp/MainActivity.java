package fds.gl.tp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bouton pour accéder aux exercices du TP1
        View btnTp1 = findViewById(R.id.btn_tp1);
        btnTp1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExercisesActivity.class);
            startActivity(intent);
        });
    }
}
