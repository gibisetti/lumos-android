package com.br.leonardomorais.lumos.activities;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.br.leonardomorais.lumos.R;

/**
 * Created by leonardo on 01/03/16.
 */
public class AboutActivity extends Activity {
    Button backButton;
    TextView txtVersionName;
    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        txtVersionName = (TextView) findViewById(R.id.about_version_value);
        txtVersionName.setText(version);

        backButton = (Button) findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
