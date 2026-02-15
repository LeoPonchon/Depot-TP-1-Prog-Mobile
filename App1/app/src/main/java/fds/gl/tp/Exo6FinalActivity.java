package fds.gl.tp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Exo6FinalActivity extends AppCompatActivity {

    private TextView tvNumeroTelephone;
    private Button btnAppeler;
    private Button btnRetourMenu;
    private Button btnRetourDetail;
    private String numeroTelephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo6_final);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // on récupère les éléments de l'interface
        tvNumeroTelephone = findViewById(R.id.tv_numero_telephone);
        btnAppeler = findViewById(R.id.btn_appeler);
        btnRetourMenu = findViewById(R.id.btn_retour_menu);
        btnRetourDetail = findViewById(R.id.btn_retour_detail);

        // on récupère le numéro transmis par l'écran précédent
        Intent intent = getIntent();
        numeroTelephone = intent.getStringExtra("EXTRA_TELEPHONE");

        // on affiche le numéro si on en a un, sinon on désactive le bouton
        if (numeroTelephone != null && !numeroTelephone.isEmpty()) {
            tvNumeroTelephone.setText(numeroTelephone);
            btnAppeler.setEnabled(true);
        } else {
            tvNumeroTelephone.setText("Aucun numéro");
            btnAppeler.setEnabled(false);
        }

        // ouvre l'appli téléphonique pour appeler le numéro
        btnAppeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerAppelTelephonique();
            }
        });

        // retourne au menu principal de l'app
        btnRetourMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retournerMenuPrincipal();
            }
        });

        // retourne simplement à l'écran précédent
        btnRetourDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // ouvre l'appli téléphonique avec le numéro pré-rempli
    private void lancerAppelTelephonique() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + numeroTelephone));
        startActivity(intent);
    }

    // retourne au menu principal en nettoyant la pile d'écrans
    private void retournerMenuPrincipal() {
        Intent intent = new Intent(Exo6FinalActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
