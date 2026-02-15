package fds.gl.tp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Exo1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView helloText = findViewById(R.id.hello_text);
        helloText.setText("Hello world");
    }
}
