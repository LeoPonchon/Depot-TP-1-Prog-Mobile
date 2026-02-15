package fds.gl.tp;

import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Exo2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // on va chercher les infos de l'appli dans le manifest
        try {
            PackageInfo packageInfo = getPackageManager()
                    .getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);

            // on affiche le nom du package
            TextView tvPackageName = findViewById(R.id.tv_package_name);
            tvPackageName.setText("Package : " + packageInfo.packageName);

            TextView tvAppName = findViewById(R.id.tv_app_name);
            tvAppName.setText("Nom de l'app : TP1");

            TextView tvMinSdk = findViewById(R.id.tv_min_sdk);
            tvMinSdk.setText("SDK minimum : " + String.valueOf(Build.VERSION.SDK_INT));

            TextView tvTargetSdk = findViewById(R.id.tv_target_sdk);
            tvTargetSdk.setText("SDK cible : " + String.valueOf(packageInfo.applicationInfo.targetSdkVersion));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // on liste toutes les activités de l'app
        TextView tvActivities = findViewById(R.id.tv_activities);
        tvActivities.setText("Activités : MainActivity (principal) + 9 activités d'exercices");

        // on affiche les fichiers XML pour les interfaces
        TextView tvLayouts = findViewById(R.id.tv_layouts);
        tvLayouts.setText("Layouts XML : activity_main.xml + 9 fichiers activity_exoX.xml");

        // on liste les fichiers Java
        TextView tvComponents = findViewById(R.id.tv_components);
        tvComponents.setText("Composants Java : MainActivity + Exo1Activity à Exo9Activity");

        // les fichiers de layout sont dans res/layout
        TextView tvResLayouts = findViewById(R.id.tv_res_layouts);
        tvResLayouts.setText("Layouts : 10 fichiers XML dans res/layout/");

        // les strings, couleurs et thèmes sont dans res/values
        TextView tvResValues = findViewById(R.id.tv_res_values);
        tvResValues.setText("Values : strings.xml, colors.xml, themes.xml dans res/values/");

        // les images et dessins sont dans res/drawable
        TextView tvResDrawable = findViewById(R.id.tv_res_drawable);
        tvResDrawable.setText("Drawable : ic_launcher_background.xml, ic_launcher_foreground.xml");

        // les icônes de l'app sont dans res/mipmap
        TextView tvResMipmap = findViewById(R.id.tv_res_mipmap);
        tvResMipmap.setText("Mipmap : Icônes de l'application (hdpi, mdpi, xhdpi, xxhdpi, xxxhdpi)");

    }
}
