package fds.gl.tp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Exo6Activity extends AppCompatActivity {

    private EditText etNom;
    private EditText etPrenom;
    private EditText etAge;
    private EditText etDomaine;
    private EditText etTelephone;
    private Button btnEnvoyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo6);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // on récupère tous les champs du formulaire
        etNom = findViewById(R.id.et_nom);
        etPrenom = findViewById(R.id.et_prenom);
        etAge = findViewById(R.id.et_age);
        etDomaine = findViewById(R.id.et_domaine);
        etTelephone = findViewById(R.id.et_telephone);
        btnEnvoyer = findViewById(R.id.btn_envoyer);

        // envoie les données vers la prochaine activité
        btnEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                envoyerDonnees();
            }
        });
    }

    // envoie les données vers l'écran suivant avec un Intent
    private void envoyerDonnees() {
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

        // on crée l'Intent pour ouvrir l'écran de détail
        Intent intent = new Intent(Exo6Activity.this, Exo6DetailActivity.class);

        // on met les données dans l'Intent pour les transmettre
        intent.putExtra("EXTRA_NOM", nom);
        intent.putExtra("EXTRA_PRENOM", prenom);
        intent.putExtra("EXTRA_AGE", age);
        intent.putExtra("EXTRA_DOMAINE", domaine);
        intent.putExtra("EXTRA_TELEPHONE", telephone);

        // on lance l'écran suivant avec nos données
        startActivity(intent);
    }
}
