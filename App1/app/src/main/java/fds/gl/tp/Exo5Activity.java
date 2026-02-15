package fds.gl.tp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Exo5Activity extends AppCompatActivity {

    private EditText etNom;
    private EditText etPrenom;
    private EditText etAge;
    private EditText etDomaine;
    private EditText etTelephone;
    private Button btnValider;
    private Button btnReinitialiser;
    private LinearLayout llResultat;
    private TextView tvResultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo5);

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

        // quand on clique sur valider, on affiche une popup de confirmation
        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherDialogueConfirmation();
            }
        });

        // le bouton reset vide tous les champs
        btnReinitialiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reinitialiserFormulaire();
            }
        });
    }

    // affiche une popup pour confirmer les infos avant de valider
    private void afficherDialogueConfirmation() {
        // on récupère ce que l'utilisateur a tapé
        String nom = etNom.getText().toString().trim();
        String prenom = etPrenom.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String domaine = etDomaine.getText().toString().trim();
        String telephone = etTelephone.getText().toString().trim();

        // on vérifie que tous les champs sont remplis
        if (nom.isEmpty() || prenom.isEmpty() || age.isEmpty() ||
            domaine.isEmpty() || telephone.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // on prépare le texte de la popup
        String message = "Confirmez-vous les informations suivantes ?\n\n" +
                "Nom : " + nom + "\n" +
                "Prénom : " + prenom + "\n" +
                "Âge : " + age + " ans\n" +
                "Domaine : " + domaine + "\n" +
                "Téléphone : " + telephone;

        // on crée la popup de confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation de validation");
        builder.setMessage(message);

        // si on clique sur confirmer, on valide vraiment
        builder.setPositiveButton("Confirmer", (dialog, which) -> {
            validerFormulaire();
            dialog.dismiss();
        });

        // si on clique sur annuler, on fait rien
        builder.setNegativeButton("Annuler", (dialog, which) -> {
            Toast.makeText(this, "Validation annulée", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        // on affiche la popup
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // fonction qui valide vraiment le formulaire après la confirmation
    private void validerFormulaire() {
        String nom = etNom.getText().toString().trim();
        String prenom = etPrenom.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String domaine = etDomaine.getText().toString().trim();
        String telephone = etTelephone.getText().toString().trim();

        // on vérifie que l'âge est un nombre valide
        try {
            int ageValue = Integer.parseInt(age);
            if (ageValue < 0 || ageValue > 120) {
                Toast.makeText(this, "Veuillez entrer un âge valide (0-120)", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer un âge valide", Toast.LENGTH_SHORT).show();
            return;
        }

        // on affiche le récapitulatif
        afficherResultats(nom, prenom, age, domaine, telephone);
        Toast.makeText(this, "Formulaire validé avec succès", Toast.LENGTH_SHORT).show();
    }

    // affiche toutes les infos dans la zone de résultat
    private void afficherResultats(String nom, String prenom, String age, String domaine, String telephone) {
        StringBuilder resultat = new StringBuilder();
        resultat.append("Nom : ").append(nom).append("\n");
        resultat.append("Prénom : ").append(prenom).append("\n");
        resultat.append("Âge : ").append(age).append(" ans\n");
        resultat.append("Domaine : ").append(domaine).append("\n");
        resultat.append("Téléphone : ").append(telephone).append("\n");

        tvResultat.setText(resultat.toString());
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
}
