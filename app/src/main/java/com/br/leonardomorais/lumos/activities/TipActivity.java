package com.br.leonardomorais.lumos.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.br.leonardomorais.lumos.R;

/**
 * Created by leonardo on 01/03/16.
 */
public class TipActivity extends Activity {

    private SharedPreferences lumosPreferences;
    SharedPreferences.Editor lumosPreferencesEditor;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tip_activity);

        lumosPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        lumosPreferencesEditor = lumosPreferences.edit();
        lumosPreferencesEditor.putBoolean("firstTimeUse", false);
        lumosPreferencesEditor.commit();

        backButton = (Button) findViewById(R.id.button_got_it);
        backButton.setOnClickListener(new View.OnClickListener() {
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