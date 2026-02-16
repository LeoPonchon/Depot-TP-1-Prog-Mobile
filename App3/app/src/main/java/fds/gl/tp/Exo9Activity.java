package fds.gl.tp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Exo9Activity extends AppCompatActivity {

    private static final String PREFS_NAME = "CalendrierPrefs";
    private static final String KEY_EVENEMENTS = "evenements";

    private TextView tvDateSelectionnee;
    private TextView tvHeureSelectionnee;
    private EditText etEvenementTitre;
    private EditText etEvenementDescription;
    private Button btnChoisirDate;
    private Button btnChoisirHeure;
    private Button btnAjouterEvenement;
    private LinearLayout llEvenementsContainer;
    private TextView tvAucunEvenement;

    private Calendar dateSelectionnee;
    private Calendar heureSelectionnee;
    private List<Evenement> evenements = new ArrayList<>();
    // pour formater la date en français (jj/mm/aaaa)
    private SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    // pour formater l'heure en français (hh:mm)
    private SimpleDateFormat formatHeure = new SimpleDateFormat("HH:mm", Locale.FRANCE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo9);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // on récupère tous les éléments de l'interface
        tvDateSelectionnee = findViewById(R.id.tv_date_selectionnee);
        tvHeureSelectionnee = findViewById(R.id.tv_heure_selectionnee);
        etEvenementTitre = findViewById(R.id.et_evenement_titre);
        etEvenementDescription = findViewById(R.id.et_evenement_description);
        btnChoisirDate = findViewById(R.id.btn_choisir_date);
        btnChoisirHeure = findViewById(R.id.btn_choisir_heure);
        btnAjouterEvenement = findViewById(R.id.btn_ajouter_evenement);
        llEvenementsContainer = findViewById(R.id.ll_evenements_container);
        tvAucunEvenement = findViewById(R.id.tv_aucun_evenement);

        // on met la date du jour par défaut
        dateSelectionnee = Calendar.getInstance();
        heureSelectionnee = Calendar.getInstance();
        afficherDateSelectionnee();
        afficherHeureSelectionnee();

        // Charger les événements sauvegardés
        chargerEvenements();
        // Afficher les événements de la date actuelle
        afficherEvenementsPourDate();

        // bouton pour choisir une date dans le calendrier
        btnChoisirDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherDatePicker();
            }
        });

        // bouton pour choisir une heure
        btnChoisirHeure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherTimePicker();
            }
        });

        // bouton pour ajouter un nouvel événement
        btnAjouterEvenement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajouterEvenement();
            }
        });
    }

    // ouvre le calendrier pour choisir une date
    private void afficherDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Exo9Activity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // on met à jour la date choisie
                        dateSelectionnee.set(Calendar.YEAR, year);
                        dateSelectionnee.set(Calendar.MONTH, month);
                        dateSelectionnee.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        
                        afficherDateSelectionnee();
                        afficherEvenementsPourDate();
                        
                        Toast.makeText(Exo9Activity.this, 
                                "Date sélectionnée : " + formatDate.format(dateSelectionnee.getTime()), 
                                Toast.LENGTH_SHORT).show();
                    }
                },
                dateSelectionnee.get(Calendar.YEAR),
                dateSelectionnee.get(Calendar.MONTH),
                dateSelectionnee.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    // affiche la date sélectionnée à l'écran
    private void afficherDateSelectionnee() {
        String dateFormatee = formatDate.format(dateSelectionnee.getTime());
        tvDateSelectionnee.setText(dateFormatee);
    }

    // affiche l'heure sélectionnée à l'écran
    private void afficherHeureSelectionnee() {
        String heureFormatee = formatHeure.format(heureSelectionnee.getTime());
        tvHeureSelectionnee.setText(heureFormatee);
    }

    // ouvre le sélecteur d'heure
    private void afficherTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                Exo9Activity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                        // on met à jour l'heure choisie
                        heureSelectionnee.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        heureSelectionnee.set(Calendar.MINUTE, minute);
                        heureSelectionnee.set(Calendar.SECOND, 0);

                        afficherHeureSelectionnee();

                        Toast.makeText(Exo9Activity.this,
                                "Heure sélectionnée : " + formatHeure.format(heureSelectionnee.getTime()),
                                Toast.LENGTH_SHORT).show();
                    }
                },
                heureSelectionnee.get(Calendar.HOUR_OF_DAY),
                heureSelectionnee.get(Calendar.MINUTE),
                true // format 24h
        );

        timePickerDialog.show();
    }

    // crée un nouvel événement et l'ajoute à la liste
    private void ajouterEvenement() {
        String titre = etEvenementTitre.getText().toString().trim();
        String description = etEvenementDescription.getText().toString().trim();

        // on vérifie que le titre n'est pas vide
        if (titre.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un titre pour l'événement", 
                    Toast.LENGTH_SHORT).show();
            etEvenementTitre.requestFocus();
            return;
        }

        // on crée l'objet événement
        Evenement evenement = new Evenement();
        evenement.titre = titre;
        evenement.description = description;
        evenement.date = dateSelectionnee.getTime();
        evenement.heureEvenement = heureSelectionnee.getTime();

        // on l'ajoute à la liste
        evenements.add(evenement);

        // on vide les champs de saisie
        etEvenementTitre.setText("");
        etEvenementDescription.setText("");

        // on rafraîchit l'affichage
        afficherEvenementsPourDate();

        // on sauvegarde les événements
        sauvegarderEvenements();

        Toast.makeText(this, "Événement ajouté avec succès !", Toast.LENGTH_SHORT).show();
    }

    // affiche tous les événements de la date sélectionnée
    private void afficherEvenementsPourDate() {
        // on filtre pour garder uniquement ceux du jour choisi
        List<Evenement> evenementsDuJour = filtrerEvenementsPourDate(dateSelectionnee.getTime());

        // on vide la liste actuelle
        llEvenementsContainer.removeAllViews();

        // soit on affiche "aucun événement", soit la liste
        if (evenementsDuJour.isEmpty()) {
            tvAucunEvenement.setVisibility(View.VISIBLE);
            tvAucunEvenement.setText("Aucun événement planifié pour le " + 
                    formatDate.format(dateSelectionnee.getTime()));
        } else {
            tvAucunEvenement.setVisibility(View.GONE);

            // on crée une vue pour chaque événement
            LayoutInflater inflater = LayoutInflater.from(this);
            for (final Evenement evenement : evenementsDuJour) {
                View evenementView = inflater.inflate(R.layout.item_evenement, null);
                
                TextView tvHeure = evenementView.findViewById(R.id.tv_evenement_heure);
                TextView tvDate = evenementView.findViewById(R.id.tv_evenement_date);
                TextView tvTitre = evenementView.findViewById(R.id.tv_evenement_titre);
                TextView tvDescription = evenementView.findViewById(R.id.tv_evenement_description);
                Button btnSupprimer = evenementView.findViewById(R.id.btn_supprimer_evenement);
                
                tvHeure.setText(formatHeure.format(evenement.heureEvenement));
                tvDate.setText(formatDate.format(evenement.date));
                tvTitre.setText(evenement.titre);
                tvDescription.setText(evenement.description.isEmpty() ? 
                        "Aucune description" : evenement.description);
                
                // Bouton supprimer
                btnSupprimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        supprimerEvenement(evenement);
                    }
                });
                
                llEvenementsContainer.addView(evenementView);
            }
            
            Toast.makeText(this, evenementsDuJour.size() + " événement(s) trouvé(s)", 
                    Toast.LENGTH_SHORT).show();
        }
    }

    // garde uniquement les événements d'une date précise
    private List<Evenement> filtrerEvenementsPourDate(Date date) {
        List<Evenement> evenementsFiltres = new ArrayList<>();
        SimpleDateFormat formatSimple = new SimpleDateFormat("yyyyMMdd", Locale.FRANCE);
        String dateCible = formatSimple.format(date);
        
        for (Evenement evenement : evenements) {
            String dateEvenement = formatSimple.format(evenement.date);
            if (dateEvenement.equals(dateCible)) {
                evenementsFiltres.add(evenement);
            }
        }
        
        return evenementsFiltres;
    }

    // supprime un événement de la liste
    private void supprimerEvenement(Evenement evenement) {
        evenements.remove(evenement);
        afficherEvenementsPourDate();
        sauvegarderEvenements();
        Toast.makeText(this, "Événement supprimé", Toast.LENGTH_SHORT).show();
    }

    // sauvegarde les événements dans SharedPreferences
    private void sauvegarderEvenements() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(evenements);
        editor.putString(KEY_EVENEMENTS, json);
        editor.apply();
    }

    // charge les événements depuis SharedPreferences
    private void chargerEvenements() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(KEY_EVENEMENTS, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<Evenement>>() {}.getType();
            evenements = gson.fromJson(json, type);
        }
    }

    // classe simple pour stocker un événement
    public static class Evenement {
        public String titre;
        public String description;
        public Date date;
        public Date heureEvenement;
    }
}
