package fds.gl.tp;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class Exo4Activity extends AppCompatActivity {

    private EditText etNom;
    private EditText etPrenom;
    private EditText etAge;
    private EditText etDomaine;
    private EditText etTelephone;
    private Button btnValider;
    private Button btnReinitialiser;
    private Button btnLangueFr;
    private Button btnLangueEn;
    private LinearLayout llResultat;
    private TextView tvResultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo4);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // on récupère tous les champs du formulaire
        etNom = findViewById(R.id.et_nom);
        etPrenom = findViewById(R.id.et_prenom);
        etAge = findViewById(R.id.et_age);
        etDomaine = findViewById(R.id.et_domaine);
        etTelephone = findViewById(R.id.et_telephone);
        btnValider = findViewById(R.id.btn_valider);
        btnReinitialiser = findViewById(R.id.btn_reinitialiser);
        btnLangueFr = findViewById(R.id.btn_langue_fr);
        btnLangueEn = findViewById(R.id.btn_langue_en);
        llResultat = findViewById(R.id.ll_resultat);
        tvResultat = findViewById(R.id.tv_resultat);

        // bouton pour passer en français
        btnLangueFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerLangue("fr");
            }
        });

        // bouton pour passer en anglais
        btnLangueEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerLangue("en");
            }
        });

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
    }

    // change la langue de l'appli (fr ou en)
    private void changerLangue(String langueCode) {
        Locale locale = new Locale(langueCode);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // on recharge l'activité pour appliquer la nouvelle langue
        recreate();

        // petit message pour confirmer le changement
        String message;
        if (langueCode.equals("fr")) {
            message = getString(R.string.msg_langue_changee);
        } else {
            message = getString(R.string.msg_langue_changee);
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, getString(R.string.erreur_nom), Toast.LENGTH_SHORT).show();
            etNom.requestFocus();
            return;
        }

        if (prenom.isEmpty()) {
            Toast.makeText(this, getString(R.string.erreur_prenom), Toast.LENGTH_SHORT).show();
            etPrenom.requestFocus();
            return;
        }

        if (age.isEmpty()) {
            Toast.makeText(this, getString(R.string.erreur_age), Toast.LENGTH_SHORT).show();
            etAge.requestFocus();
            return;
        }

        if (domaine.isEmpty()) {
            Toast.makeText(this, getString(R.string.erreur_domaine), Toast.LENGTH_SHORT).show();
            etDomaine.requestFocus();
            return;
        }

        if (telephone.isEmpty()) {
            Toast.makeText(this, getString(R.string.erreur_telephone), Toast.LENGTH_SHORT).show();
            etTelephone.requestFocus();
            return;
        }

        // on vérifie que l'âge est un nombre valide
        try {
            int ageValue = Integer.parseInt(age);
            if (ageValue < 0 || ageValue > 120) {
                Toast.makeText(this, getString(R.string.erreur_age_invalide), Toast.LENGTH_SHORT).show();
                etAge.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.erreur_age), Toast.LENGTH_SHORT).show();
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
        StringBuilder resultat = new StringBuilder();
        resultat.append(getString(R.string.msg_recapitulatif)).append("\n\n");
        resultat.append(getString(R.string.msg_nom)).append(" ").append(nom).append("\n");
        resultat.append(getString(R.string.msg_prenom)).append(" ").append(prenom).append("\n");
        resultat.append(getString(R.string.msg_age)).append(" ").append(age).append(" ")
                .append(getString(R.string.msg_annees)).append("\n");
        resultat.append(getString(R.string.msg_domaine)).append(" ").append(domaine).append("\n");
        resultat.append(getString(R.string.msg_telephone)).append(" ").append(telephone).append("\n");

        tvResultat.setText(resultat.toString());
        llResultat.setVisibility(View.VISIBLE);

        Toast.makeText(this, getString(R.string.msg_valide), Toast.LENGTH_SHORT).show();

        // on descend automatiquement vers les résultats
        llResultat.post(new Runnable() {
            @Override
            public void run() {
                llResultat.requestChildFocus(llResultat, llResultat.getChildAt(0));
            }
        });
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
        Toast.makeText(this, getString(R.string.msg_reinitialise), Toast.LENGTH_SHORT).show();
    }

    // cache le clavier virtuel
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
        }
    }
}
