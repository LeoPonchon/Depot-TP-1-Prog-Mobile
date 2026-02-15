package fds.gl.tp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Exo3JavaActivity extends AppCompatActivity {

    private EditText etNom;
    private EditText etPrenom;
    private EditText etAge;
    private EditText etDomaine;
    private EditText etTelephone;
    private TextView tvResultat;
    private LinearLayout llResultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // conteneur principal de tout l'écran
        LinearLayout mainContainer = new LinearLayout(this);
        mainContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));

        // l'en-tête avec le titre
        LinearLayout header = new LinearLayout(this);
        header.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        header.setOrientation(LinearLayout.VERTICAL);
        header.setBackground(ContextCompat.getDrawable(this, R.drawable.header_background));
        int headerPadding = dpToPx(32);
        header.setPadding(headerPadding, headerPadding, headerPadding, headerPadding);

        TextView title = new TextView(this);
        title.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setText("Exercice 03 - Java UI");
        title.setTextColor(Color.WHITE);
        title.setTextSize(24);
        title.setTypeface(null, Typeface.BOLD);
        ((LinearLayout.LayoutParams) title.getLayoutParams()).gravity = Gravity.CENTER_HORIZONTAL;
        header.addView(title);
        mainContainer.addView(header);

        // zone de défilement pour le contenu
        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f));

        LinearLayout scrollContent = new LinearLayout(this);
        scrollContent.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        scrollContent.setOrientation(LinearLayout.VERTICAL);
        int padding = dpToPx(16);
        scrollContent.setPadding(padding, padding, padding, padding);

        // la carte qui contient le formulaire
        LinearLayout formCard = new LinearLayout(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, dpToPx(16)); // un peu d'espace en bas de la carte
        formCard.setLayoutParams(cardParams);
        formCard.setOrientation(LinearLayout.VERTICAL);
        formCard.setBackground(ContextCompat.getDrawable(this, R.drawable.card_background));
        formCard.setPadding(padding, padding, padding, padding);

        etNom = createStyledEditText("Nom");
        etPrenom = createStyledEditText("Prénom");
        etAge = createStyledEditText("Âge");
        etDomaine = createStyledEditText("Domaine de compétences");
        etTelephone = createStyledEditText("Numéro de téléphone");

        formCard.addView(etNom);
        formCard.addView(etPrenom);
        formCard.addView(etAge);
        formCard.addView(etDomaine);
        formCard.addView(etTelephone);

        Button btnValider = new Button(this);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(0, dpToPx(16), 0, 0);
        btnValider.setLayoutParams(buttonParams);
        btnValider.setText("Valider");
        btnValider.setTextColor(Color.WHITE);
        btnValider.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_blue));
        btnValider.setOnClickListener(v -> validerFormulaire());
        formCard.addView(btnValider);

        scrollContent.addView(formCard);

        // la carte qui affiche les résultats (cachée au début)
        llResultat = new LinearLayout(this);
        llResultat.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        llResultat.setOrientation(LinearLayout.VERTICAL);
        llResultat.setBackground(ContextCompat.getDrawable(this, R.drawable.card_background));
        llResultat.setPadding(padding, padding, padding, padding);
        llResultat.setVisibility(View.GONE);

        TextView resultTitle = new TextView(this);
        LinearLayout.LayoutParams resultTitleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        resultTitleParams.bottomMargin = dpToPx(16);
        resultTitle.setLayoutParams(resultTitleParams);
        resultTitle.setText("Informations validées");
        resultTitle.setTextColor(ContextCompat.getColor(this, R.color.dark_blue));
        resultTitle.setTextSize(20);
        resultTitle.setTypeface(null, Typeface.BOLD);
        resultTitle.setGravity(Gravity.CENTER);
        llResultat.addView(resultTitle);

        tvResultat = new TextView(this);
        tvResultat.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
        tvResultat.setTextSize(14);
        tvResultat.setLineSpacing(0, 1.2f);
        llResultat.addView(tvResultat);

        scrollContent.addView(llResultat);

        scrollView.addView(scrollContent);
        mainContainer.addView(scrollView);

        setContentView(mainContainer);
    }

    // crée un champ de texte avec le style de l'app
    private EditText createStyledEditText(String hint) {
        EditText editText = new EditText(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = dpToPx(8);
        editText.setLayoutParams(params);
        editText.setHint(hint);
        editText.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text_background));
        int padding = dpToPx(12);
        editText.setPadding(padding, padding, padding, padding);
        return editText;
    }

    // convertit les dp en pixels pour la même taille sur tous les téléphones
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    // fonction appelée quand on clique sur valider
    private void validerFormulaire() {
        String nom = etNom.getText().toString().trim();
        String prenom = etPrenom.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String domaine = etDomaine.getText().toString().trim();
        String telephone = etTelephone.getText().toString().trim();

        if (nom.isEmpty() || prenom.isEmpty() || age.isEmpty() || domaine.isEmpty() || telephone.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int ageValue = Integer.parseInt(age);
            if (ageValue < 0 || ageValue > 120) {
                Toast.makeText(this, "Âge invalide (0-120)", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez saisir un âge numérique", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder resultat = new StringBuilder();
        resultat.append("Nom : ").append(nom).append("\n");
        resultat.append("Prénom : ").append(prenom).append("\n");
        resultat.append("Âge : ").append(age).append(" ans\n");
        resultat.append("Domaine : ").append(domaine).append("\n");
        resultat.append("Téléphone : ").append(telephone).append("\n");

        tvResultat.setText(resultat.toString());
        llResultat.setVisibility(View.VISIBLE);
    }
}
