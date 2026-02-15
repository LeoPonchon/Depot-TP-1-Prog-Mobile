package fds.gl.tp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Exo3Activity extends AppCompatActivity {

    private EditText etNom;
    private EditText etPrenom;
    private EditText etAge;
    private EditText etDomaine;
    private EditText etTelephone;
    private Button btnValider;
    private Button btnReinitialiser;
    private LinearLayout llResultat;
    private TextView tvResultat;
    private Button btnOuvrirVersionJava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo3);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // on récupère tous les champs du formulaire
        etNom = findViewById(R.id.et_nom);
        etPrenom = findViewById(R.id.et_prenom);
        etAge = findViewById(R.id.et_age);
        etDomaine = findViewById(R.id.et_domaine);
        etTelephone = findViewById(R.id.et_telephone);
        btnValider = findViewById(R.id.btn_valider);
        btnReinitialiser = findViewById(R.id.btn_reinitialiser);
        llResultat = findViewById(R.id.ll_resultat);
        tvResultat = findViewById(R.id.tv_resultat);
        btnOuvrirVersionJava = findViewById(R.id.btn_ouvrir_version_java);

        // quand on clique sur valider, on vérifie le formulaire
        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validerFormulaire();
            }
        });

        // le bouton reset vide tous les champs
        btnReinitialiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reinitialiserFormulaire();
            }
        });

        // ce bouton ouvre la version où l'interface est codée en Java
        btnOuvrirVersionJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Exo3Activity.this, Exo3JavaActivity.class);
                startActivity(intent);
            }
        });
    }

    // fonction appelée quand on clique sur valider
    private void validerFormulaire() {
        // on récupère ce que l'utilisateur a tapé
        String nom = etNom.getText().toString().trim();
        String prenom = etPrenom.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String domaine = etDomaine.getText().toString().trim();
        String telephone = etTelephone.getText().toString().trim();

        // on vérifie que tous les champs sont remplis
        if (nom.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer votre nom", Toast.LENGTH_SHORT).show();
            etNom.requestFocus();
            return;
        }

        if (prenom.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer votre prénom", Toast.LENGTH_SHORT).show();
            etPrenom.requestFocus();
            return;
        }

        if (age.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer votre âge", Toast.LENGTH_SHORT).show();
            etAge.requestFocus();
            return;
        }

        if (domaine.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer votre domaine de compétences", Toast.LENGTH_SHORT).show();
            etDomaine.requestFocus();
            return;
        }

        if (telephone.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer votre numéro de téléphone", Toast.LENGTH_SHORT).show();
            etTelephone.requestFocus();
            return;
        }

        // on vérifie que l'âge est un nombre valide
        try {
            int ageValue = Integer.parseInt(age);
            if (ageValue < 0 || ageValue > 120) {
                Toast.makeText(this, "Veuillez entrer un âge valide (0-120)", Toast.LENGTH_SHORT).show();
                etAge.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer un âge valide", Toast.LENGTH_SHORT).show();
            etAge.requestFocus();
            return;
        }

        // on affiche le récapitulatif
        afficherResultats(nom, prenom, age, domaine, telephone);

        // on cache le clavier
        hideKeyboard();
    }

    // affiche toutes les infos dans la zone de résultat
    private void afficherResultats(String nom, String prenom, String age, String domaine, String telephone) {
        StringBuilder sb = new StringBuilder();
        sb.append("Récapitulatif des informations :\n\n");
        sb.append("Nom : ").append(nom).append("\n");
        sb.append("Prénom : ").append(prenom).append("\n");
        sb.append("Âge : ").append(age).append(" ans\n");
        sb.append("Domaine de compétences : ").append(domaine).append("\n");
        sb.append("Numéro de téléphone : ").append(telephone).append("\n");

        tvResultat.setText(sb.toString());
        llResultat.setVisibility(View.VISIBLE);
    }

    // vide tous les champs du formulaire
    private void reinitialiserFormulaire() {
        etNom.setText("");
        etPrenom.setText("");
        etAge.setText("");
        etDomaine.setText("");
        etTelephone.setText("");
        llResultat.setVisibility(View.GONE);
        etNom.requestFocus();
        Toast.makeText(this, "Formulaire réinitialisé", Toast.LENGTH_SHORT).show();
    }

    // cache le clavier virtuel
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
        }
    }
}
