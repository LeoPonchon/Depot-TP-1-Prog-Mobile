package fds.gl.tp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Exo8Activity extends AppCompatActivity {

    private Spinner spinnerVilleDepart;
    private Spinner spinnerVilleArrivee;
    private TextView tvDateHeure;
    private Button btnChoisirDateHeure;
    private Button btnRechercher;
    private LinearLayout llHorairesContainer;
    private TextView tvMessageErreur;

    // liste pour stocker tous les horaires récupérés
    private List<HoraireTrain> tousLesHoraires;

    // liste des villes qu'on peut choisir
    private static final String[] VILLES = {
        "Paris",
        "Lyon",
        "Marseille",
        "Nice",
        "Bordeaux",
        "Toulouse",
        "Nantes",
        "Strasbourg",
        "Lille",
        "Montpellier",
        "Rennes",
        "Reims",
        "Le Havre",
        "Saint-Étienne",
        "Toulon",
        "Grenoble",
        "Dijon",
        "Angers",
        "Nîmes",
        "Villeurbanne",
        "Clermont-Ferrand",
        "Le Mans",
        "Aix-en-Provence",
        "Brest",
        "Tours",
        "Limoges",
        "Amiens",
        "Metz",
        "Besançon",
        "Perpignan",
        "Orléans",
        "Mulhouse",
        "Caen",
        "Boulogne-Billancourt",
        "Rouen",
        "Nancy",
        "Argenteuil",
        "Montreuil",
        "Saint-Denis",
        "Roubaix",
        "Avignon",
        "Tourcoing",
        "Poitiers",
        "Nanterre",
        "Créteil",
        "Versailles",
        "Courbevoie",
        "Pau",
        "Colmar",
    };

    // variables pour les villes choisies et la date
    private String villeDepart = "";
    private String villeArrivee = "";
    private Calendar dateHeureSelectionnee;

    // l'URL de base de l'API SNCF
    private static final String SNCF_API_BASE_URL =
        "https://api.sncf.com/v1/coverage/sncf/journeys";

    // notre clé pour accéder à l'API
    private static final String SNCF_API_KEY =
        "cef52544-7e83-43ae-a503-691008ba89ea";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo8);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // on récupère tous les éléments de l'interface
        spinnerVilleDepart = findViewById(R.id.spinner_ville_depart);
        spinnerVilleArrivee = findViewById(R.id.spinner_ville_arrivee);
        tvDateHeure = findViewById(R.id.tv_date_heure);
        btnChoisirDateHeure = findViewById(R.id.btn_choisir_date_heure);
        btnRechercher = findViewById(R.id.btn_rechercher);
        llHorairesContainer = findViewById(R.id.ll_horaires_container);
        tvMessageErreur = findViewById(R.id.tv_message_erreur);

        // on met la date du jour par défaut
        dateHeureSelectionnee = Calendar.getInstance();
        afficherDateHeure();

        // on configure les listes déroulantes des villes
        configurerSpinners();

        // bouton pour changer la date et l'heure du trajet
        btnChoisirDateHeure.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choisirDateHeure();
                }
            }
        );

        // bouton pour lancer la recherche des trains
        btnRechercher.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rechercherHoraires();
                }
            }
        );
    }

    // configure les listes déroulantes avec nos villes
    private void configurerSpinners() {
        // on crée l'adapteur pour les listes déroulantes
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            VILLES
        ) {
            @Override
            public View getView(
                int position,
                View convertView,
                ViewGroup parent
            ) {
                TextView tv = (TextView) super.getView(
                    position,
                    convertView,
                    parent
                );
                tv.setTextColor(
                    getResources().getColor(R.color.dark_blue)
                );
                tv.setTextSize(16);
                return tv;
            }

            @Override
            public View getDropDownView(
                int position,
                View convertView,
                ViewGroup parent
            ) {
                TextView tv = (TextView) super.getDropDownView(
                    position,
                    convertView,
                    parent
                );
                tv.setTextColor(
                    getResources().getColor(R.color.dark_blue)
                );
                tv.setTextSize(16);
                tv.setPadding(16, 12, 16, 12);
                return tv;
            }
        };
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        );

        spinnerVilleDepart.setAdapter(adapter);
        spinnerVilleArrivee.setAdapter(adapter);

        // on sélectionne Paris et Lyon par défaut
        spinnerVilleDepart.setSelection(0);
        spinnerVilleArrivee.setSelection(1);
    }

    // affiche la date et l'heure sélectionnées
    private void afficherDateHeure() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
        tvDateHeure.setText(sdf.format(dateHeureSelectionnee.getTime()));
    }

    // change le jour sélectionné (-1 pour jour précédent, +1 pour suivant)
    private void changerJour(int jours) {
        dateHeureSelectionnee.add(Calendar.DAY_OF_MONTH, jours);
        afficherDateHeure();

        // Afficher un message de confirmation
        String message = jours > 0 ? "Jour suivant" : "Jour précédent";
        SimpleDateFormat sdf = new SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.FRANCE
        );
        Toast.makeText(
            this,
            message + " : " + sdf.format(dateHeureSelectionnee.getTime()),
            Toast.LENGTH_SHORT
        ).show();
    }

    /**
     * Ouvre les dialogs pour choisir la date et l'heure
     */
    private void choisirDateHeure() {
        // DatePicker
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(
                    DatePicker view,
                    int year,
                    int month,
                    int dayOfMonth
                ) {
                    dateHeureSelectionnee.set(Calendar.YEAR, year);
                    dateHeureSelectionnee.set(Calendar.MONTH, month);
                    dateHeureSelectionnee.set(
                        Calendar.DAY_OF_MONTH,
                        dayOfMonth
                    );

                    // TimePicker après la date
                    TimePickerDialog timePickerDialog =
                        new TimePickerDialog(
                            Exo8Activity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(
                                    TimePicker view,
                                    int hourOfDay,
                                    int minute
                                ) {
                                    dateHeureSelectionnee.set(
                                        Calendar.HOUR_OF_DAY,
                                        hourOfDay
                                    );
                                    dateHeureSelectionnee.set(
                                        Calendar.MINUTE,
                                        minute
                                    );
                                    afficherDateHeure();
                                }
                            },
                            dateHeureSelectionnee.get(Calendar.HOUR_OF_DAY),
                            dateHeureSelectionnee.get(Calendar.MINUTE),
                            true
                        );
                    timePickerDialog.show();
                }
            },
            dateHeureSelectionnee.get(Calendar.YEAR),
            dateHeureSelectionnee.get(Calendar.MONTH),
            dateHeureSelectionnee.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    /**
     * Recherche les horaires de trains
     */
    private void rechercherHoraires() {
        villeDepart = (String) spinnerVilleDepart.getSelectedItem();
        villeArrivee = (String) spinnerVilleArrivee.getSelectedItem();

        if (villeDepart == null || villeDepart.isEmpty()) {
            Toast.makeText(
                this,
                "Veuillez sélectionner une ville de départ",
                Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (villeArrivee == null || villeArrivee.isEmpty()) {
            Toast.makeText(
                this,
                "Veuillez sélectionner une ville d'arrivée",
                Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (villeDepart.equalsIgnoreCase(villeArrivee)) {
            Toast.makeText(
                this,
                "Les villes de départ et d'arrivée doivent être différentes",
                Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String codeDepart = getAdminCodeForCity(villeDepart);
        String codeArrivee = getAdminCodeForCity(villeArrivee);

        if (codeDepart == null || codeArrivee == null) {
            afficherMessageErreur(
                "Cette ville n'est pas supportée par l'API SNCF pour le moment"
            );
            return;
        }

        chargerHorairesDepuisSncf(
            villeDepart,
            villeArrivee,
            codeDepart,
            codeArrivee
        );
    }

    /**
     * Appelle l'API SNCF pour récupérer les horaires
     */
    private void chargerHorairesDepuisSncf(
        final String villeDepart,
        final String villeArrivee,
        final String codeDepart,
        final String codeArrivee
    ) {
        tvMessageErreur.setVisibility(View.GONE);
        llHorairesContainer.setVisibility(View.GONE);
        llHorairesContainer.removeAllViews();

        new Thread(
            new Runnable() {
                @Override
                public void run() {
                    List<HoraireTrain> horaires = new ArrayList<>();
                    String erreur = null;

                    try {
                        // Formater la date/heure pour l'API
                        SimpleDateFormat apiFormat = new SimpleDateFormat(
                            "yyyyMMdd'T'HHmmss",
                            Locale.FRANCE
                        );
                        String dateTime = apiFormat.format(
                            dateHeureSelectionnee.getTime()
                        );

                        // Construire l'URL selon la documentation API SNCF
                        String urlStr =
                            SNCF_API_BASE_URL +
                            "?key=" +
                            SNCF_API_KEY +
                            "&from=" +
                            URLEncoder.encode(codeDepart, "UTF-8") +
                            "&to=" +
                            URLEncoder.encode(codeArrivee, "UTF-8") +
                            "&datetime=" +
                            dateTime +
                            "&datetime_represents=departure" +
                            "&count=100" + // Beaucoup de résultats pour avoir tous les trajets
                            "&min_nb_journeys=0" +
                            "&max_nb_journeys=10";

                        URL url = new URL(urlStr);
                        HttpURLConnection connection =
                            (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(15000);
                        connection.setReadTimeout(15000);

                        int responseCode = connection.getResponseCode();

                        InputStream inputStream;
                        if (responseCode >= 200 && responseCode < 300) {
                            inputStream = connection.getInputStream();
                        } else {
                            inputStream = connection.getErrorStream();
                        }

                        String body = lireFlux(inputStream);
                        connection.disconnect();

                        if (responseCode >= 200 && responseCode < 300) {
                            horaires = parserJourneysDepuisJson(
                                body,
                                villeDepart,
                                villeArrivee
                            );
                        } else {
                            erreur = extraireMessageErreurApi(body, responseCode);
                        }
                    } catch (Exception e) {
                        erreur = "Erreur: " + e.getMessage();
                    }

                    final List<HoraireTrain> resultats = horaires;
                    final String erreurFinale = erreur;

                    runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                if (erreurFinale != null) {
                                    afficherMessageErreur(erreurFinale);
                                } else if (resultats.isEmpty()) {
                                    afficherMessageErreur(
                                        "Aucun train trouvé pour cet itinéraire"
                                    );
                                } else {
                                    afficherHoraires(resultats);
                                }
                            }
                        }
                    );
                }
            }
        )
            .start();
    }

    /**
     * Affiche tous les horaires trouvés du jour sélectionné
     */
    private void afficherHoraires(List<HoraireTrain> horaires) {
        tvMessageErreur.setVisibility(View.GONE);
        llHorairesContainer.removeAllViews();

        // Stocker tous les horaires
        tousLesHoraires = horaires;

        // Filtrer pour ne garder que les trajets du jour sélectionné
        String jourSelectionne = new SimpleDateFormat(
            "yyyyMMdd",
            Locale.FRANCE
        ).format(dateHeureSelectionnee.getTime());

        List<HoraireTrain> horairesDuJour = new ArrayList<>();
        for (HoraireTrain horaire : horaires) {
            String jourTrajet = horaire.dateDepart.substring(0, 8); // yyyyMMdd
            if (jourTrajet.equals(jourSelectionne)) {
                horairesDuJour.add(horaire);
            }
        }

        // Titre
        TextView tvTitre = new TextView(this);
        SimpleDateFormat affichageDate = new SimpleDateFormat(
            "EEEE dd MMMM yyyy",
            Locale.FRANCE
        );
        String jourAffiche = affichageDate.format(
            dateHeureSelectionnee.getTime()
        );
        jourAffiche =
            jourAffiche.substring(0, 1).toUpperCase() +
            jourAffiche.substring(1);

        tvTitre.setText(
            jourAffiche +
                " - " +
                horairesDuJour.size() +
                " trajet(s)"
        );
        tvTitre.setTextSize(18);
        tvTitre.setPadding(16, 16, 16, 8);
        tvTitre.setTextAppearance(android.R.style.TextAppearance_Large);
        tvTitre.setTextColor(
            getResources().getColor(R.color.dark_blue)
        );
        llHorairesContainer.addView(tvTitre);

        if (horairesDuJour.isEmpty()) {
            TextView tvVide = new TextView(this);
            tvVide.setText(
                "Aucun trajet trouvé pour ce jour. Utilisez les fleches pour changer de jour."
            );
            tvVide.setTextSize(14);
            tvVide.setPadding(16, 16, 16, 16);
            tvVide.setTextColor(
                getResources().getColor(R.color.text_gray)
            );
            llHorairesContainer.addView(tvVide);
        } else {
            // Afficher chaque trajet
            for (HoraireTrain horaire : horairesDuJour) {
                View view = getLayoutInflater().inflate(
                    R.layout.item_horaire_train,
                    null
                );

                TextView tvNumero = view.findViewById(R.id.tv_train_numero);
                TextView tvDepart = view.findViewById(R.id.tv_train_depart);
                TextView tvArrivee = view.findViewById(
                    R.id.tv_train_arrivee
                );
                TextView tvHeureDepart = view.findViewById(
                    R.id.tv_heure_depart
                );
                TextView tvHeureArrivee = view.findViewById(
                    R.id.tv_heure_arrivee
                );

                tvNumero.setText(horaire.numeroTrain);
                tvDepart.setText(horaire.villeDepart);
                tvArrivee.setText(horaire.villeArrivee);
                tvHeureDepart.setText(horaire.heureDepart);
                tvHeureArrivee.setText(horaire.heureArrivee);

                llHorairesContainer.addView(view);
            }
        }

        llHorairesContainer.setVisibility(View.VISIBLE);
        Toast.makeText(
            this,
            horairesDuJour.size() + " trajet(s) affiché(s)",
            Toast.LENGTH_SHORT
        ).show();
    }

    /**
     * Affiche un message d'erreur
     */
    private void afficherMessageErreur(String message) {
        tvMessageErreur.setText(message);
        tvMessageErreur.setVisibility(View.VISIBLE);
        llHorairesContainer.setVisibility(View.GONE);
    }

    /**
     * Extrait un message d'erreur compréhensible depuis la réponse JSON de l'API
     */
    private String extraireMessageErreurApi(String jsonBody, int responseCode) {
        try {
            JSONObject root = new JSONObject(jsonBody);
            JSONObject error = root.optJSONObject("error");

            if (error != null) {
                String errorId = error.optString("id", "");
                String errorMessage = error.optString("message", "");

                // Erreur de date hors limite
                if ("date_out_of_bounds".equals(errorId)) {
                    return "Cette date est trop loin dans le futur. " +
                           "L'API SNCF ne fournit les horaires que pour les prochains jours.";
                }

                // Erreur de paramètre
                if ("bad_filter".equals(errorId) || "bad_request".equals(errorId)) {
                    return "Paramètres de recherche invalides. Veuillez vérifier votre sélection.";
                }

                // Erreur d'authentification
                if ("unknown_apikey".equals(errorId)) {
                    return "Erreur de configuration de l'application (clé API invalide).";
                }

                // Erreur quota dépassé
                if ("quota_exceeded".equals(errorId)) {
                    return "Trop de requêtes. Veuillez réessayer dans quelques instants.";
                }

                // Message d'erreur spécifique mais non mappé
                if (!errorMessage.isEmpty()) {
                    return "Erreur: " + errorMessage;
                }
            }
        } catch (JSONException e) {
            // JSON invalide, on utilise le message par défaut
        }

        // Message par défaut si le parsing échoue ou si l'erreur est inconnue
        if (jsonBody != null && jsonBody.length() < 100) {
            return "Erreur API SNCF (" + responseCode + "): " + jsonBody;
        }
        return "Erreur lors de la récupération des horaires (code " + responseCode + "). " +
               "Veuillez vérifier votre connexion et réessayer.";
    }

    /**
     * Lit un flux
     */
    private String lireFlux(InputStream inputStream) throws IOException {
        if (inputStream == null) return "";
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(inputStream)
        );
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        return builder.toString();
    }

    /**
     * Parse la réponse JSON de l'API
     */
    private List<HoraireTrain> parserJourneysDepuisJson(
        String json,
        String villeDepart,
        String villeArrivee
    ) throws JSONException {
        List<HoraireTrain> horaires = new ArrayList<>();

        JSONObject root = new JSONObject(json);
        JSONArray journeys = root.optJSONArray("journeys");
        if (journeys == null) {
            return horaires;
        }

        for (int i = 0; i < journeys.length(); i++) {
            JSONObject journey = journeys.getJSONObject(i);
            String departure = journey.optString(
                "departure_date_time",
                null
            );
            String arrival = journey.optString("arrival_date_time", null);

            // Récupérer les infos du train
            String numeroTrain = "Train #" + (i + 1);
            JSONArray sections = journey.optJSONArray("sections");
            if (sections != null && sections.length() > 0) {
                JSONObject firstSection = sections.optJSONObject(0);
                if (firstSection != null) {
                    JSONObject display = firstSection.optJSONObject(
                        "display_informations"
                    );
                    if (display != null) {
                        String code = display.optString("code", null);
                        String headsign = display.optString(
                            "headsign",
                            null
                        );
                        String network = display.optString("network", null);

                        if (code != null && !code.isEmpty()) {
                            numeroTrain = code;
                            if (network != null && !network.isEmpty()) {
                                numeroTrain += " (" + network + ")";
                            }
                        } else if (
                            headsign != null && !headsign.isEmpty()
                        ) {
                            numeroTrain = headsign;
                        }
                    }
                }
            }

            String heureDepart = formaterHeure(departure);
            String heureArrivee = formaterHeure(arrival);

            horaires.add(
                new HoraireTrain(
                    villeDepart,
                    villeArrivee,
                    heureDepart,
                    heureArrivee,
                    numeroTrain,
                    departure
                )
            );
        }

        return horaires;
    }

    /**
     * Formate une heure
     */
    private String formaterHeure(String apiDateTime) {
        if (apiDateTime == null || apiDateTime.isEmpty()) {
            return "";
        }
        try {
            SimpleDateFormat in = new SimpleDateFormat(
                "yyyyMMdd'T'HHmmss",
                Locale.FRANCE
            );
            SimpleDateFormat out = new SimpleDateFormat("HH:mm", Locale.FRANCE);
            Date date = in.parse(apiDateTime);
            return out.format(date);
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * Retourne le code administratif pour une ville
     * Utilise les codes admin:fr:XXXX comme dans la doc officielle
     * Codes INSEE pour les principales villes françaises
     */
    private String getAdminCodeForCity(String ville) {
        if (ville == null) return null;

        String lower = ville.trim().toLowerCase(Locale.FRANCE);

        // Utilisation des codes INSEE officiels
        switch (lower) {
            case "paris":
                return "admin:fr:75056";
            case "lyon":
                return "admin:fr:69123";
            case "marseille":
                return "admin:fr:13055";
            case "nice":
                return "admin:fr:06088";
            case "bordeaux":
                return "admin:fr:33063";
            case "toulouse":
                return "admin:fr:31555";
            case "nantes":
                return "admin:fr:44109";
            case "strasbourg":
                return "admin:fr:67482";
            case "lille":
                return "admin:fr:59350";
            case "montpellier":
                return "admin:fr:34172";
            case "rennes":
                return "admin:fr:35238";
            case "reims":
                return "admin:fr:51454";
            case "le havre":
                return "admin:fr:76351";
            case "saint-étienne":
                return "admin:fr:42218";
            case "toulon":
                return "admin:fr:83137";
            case "grenoble":
                return "admin:fr:38185";
            case "dijon":
                return "admin:fr:21231";
            case "angers":
                return "admin:fr:49007";
            case "nîmes":
                return "admin:fr:30189";
            case "villeurbanne":
                return "admin:fr:69266";
            case "clermont-ferrand":
                return "admin:fr:63113";
            case "le mans":
                return "admin:fr:72181";
            case "aix-en-provence":
                return "admin:fr:13100";
            case "brest":
                return "admin:fr:29019";
            case "tours":
                return "admin:fr:37261";
            case "limoges":
                return "admin:fr:87085";
            case "amiens":
                return "admin:fr:80021";
            case "metz":
                return "admin:fr:57463";
            case "besançon":
                return "admin:fr:25056";
            case "perpignan":
                return "admin:fr:66136";
            case "orléans":
                return "admin:fr:45234";
            case "mulhouse":
                return "admin:fr:68224";
            case "caen":
                return "admin:fr:14118";
            case "boulogne-billancourt":
                return "admin:fr:92012";
            case "rouen":
                return "admin:fr:76540";
            case "nancy":
                return "admin:fr:54395";
            case "argenteuil":
                return "admin:fr:95002";
            case "montreuil":
                return "admin:fr:93048";
            case "saint-denis":
                return "admin:fr:93066";
            case "roubaix":
                return "admin:fr:59512";
            case "avignon":
                return "admin:fr:84007";
            case "tourcoing":
                return "admin:fr:59599";
            case "poitiers":
                return "admin:fr:86194";
            case "nanterre":
                return "admin:fr:92050";
            case "créteil":
                return "admin:fr:94028";
            case "versailles":
                return "admin:fr:78646";
            case "courbevoie":
                return "admin:fr:92026";
            case "pau":
                return "admin:fr:64445";
            case "colmar":
                return "admin:fr:68066";
            default:
                return null;
        }
    }

    /**
     * Classe pour représenter un horaire de train
     */
    private static class HoraireTrain {

        String villeDepart;
        String villeArrivee;
        String heureDepart;
        String heureArrivee;
        String numeroTrain;
        String dateDepart; // DateTime complet pour le groupement par jour

        HoraireTrain(
            String villeDepart,
            String villeArrivee,
            String heureDepart,
            String heureArrivee,
            String numeroTrain,
            String dateDepart
        ) {
            this.villeDepart = villeDepart;
            this.villeArrivee = villeArrivee;
            this.heureDepart = heureDepart;
            this.heureArrivee = heureArrivee;
            this.numeroTrain = numeroTrain;
            this.dateDepart = dateDepart;
        }
    }
}
