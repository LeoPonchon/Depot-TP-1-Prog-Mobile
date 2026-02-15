package fds.gl.tp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Exo6DetailActivity extends AppCompatActivity {

    private TextView tvAffichageDonnees;
    private Button btnOk;
    private String numeroTelephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo6_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // on récupère les éléments de l'interface
        tvAffichageDonnees = findViewById(R.id.tv_affichage_donnees);
        btnOk = findViewById(R.id.btn_ok);

        // on récupère l'Intent qui a ouvert cet écran
        Intent intent = getIntent();

        // on récupère les données transmises par l'écran précédent
        String nom = intent.getStringExtra("EXTRA_NOM");
        String prenom = intent.getStringExtra("EXTRA_PRENOM");
        String age = intent.getStringExtra("EXTRA_AGE");
        String domaine = intent.getStringExtra("EXTRA_DOMAINE");
        // on garde le numéro de téléphone pour l'écran final
        numeroTelephone = intent.getStringExtra("EXTRA_TELEPHONE");

        // on affiche toutes les infos récupérées
        afficherDonnees(nom, prenom, age, domaine, numeroTelephone);

        // le bouton OK ouvre le troisième écran
        btnOk.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lancerTroisiemeActivite();
                }
            }
        );
    }

    // affiche les données reçues de l'écran précédent
    private void afficherDonnees(
        String nom,
        String prenom,
        String age,
        String domaine,
        String telephone
    ) {
        StringBuilder donnees = new StringBuilder();
        donnees.append("Nom : ").append(nom != null ? nom : "Non renseigné").append("\n");
        donnees.append("Prénom : ").append(prenom != null ? prenom : "Non renseigné").append("\n");
        donnees.append("Âge : ").append(age != null ? age : "Non renseigné").append(" ans\n");
        donnees.append("Domaine : ").append(domaine != null ? domaine : "Non renseigné").append("\n");
        donnees.append("Téléphone : ").append(telephone != null ? telephone : "Non renseigné").append("\n");

        tvAffichageDonnees.setText(donnees.toString());
    }

    // ouvre le dernier écran de l'exercice
    private void lancerTroisiemeActivite() {
        Intent intent = new Intent(
            Exo6DetailActivity.this,
            Exo6FinalActivity.class
        );
        // on transmet aussi le numéro de téléphone pour le dernier écran
        if (numeroTelephone != null) {
            intent.putExtra("EXTRA_TELEPHONE", numeroTelephone);
        }
        startActivity(intent);
    }
}
