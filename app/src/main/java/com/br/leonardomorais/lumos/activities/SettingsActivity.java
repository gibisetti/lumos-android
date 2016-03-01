package com.br.leonardomorais.lumos.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.br.leonardomorais.lumos.R;

/**
 * Created by leonardo on 01/03/16.
 */
public class SettingsActivity extends Activity {
    private SharedPreferences appPreferences;
    SharedPreferences.Editor appPreferencesEditor;
    private boolean voiceRecognizerPreference;
    private boolean shakeDeviceDetectorPreference;
    private boolean whilePressedPreference;
    private Button aboutBack;
    private Button buttonBack;
    private ToggleButton toggleVoiceRecognizer;
    private ToggleButton toggleShakeDeviceDetector;
    private ToggleButton toggleWhilePressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        appPreferencesEditor = appPreferences.edit();

        voiceRecognizerPreference = appPreferences.getBoolean("voiceRecognizer", true);
        shakeDeviceDetectorPreference = appPreferences.getBoolean("shakeDeviceDetector", false);
        whilePressedPreference = appPreferences.getBoolean("whilePressed", false);

        toggleVoiceRecognizer = (ToggleButton) findViewById(R.id.settings_toggle_voice);
        toggleVoiceRecognizer.setOnCheckedChangeListener(OnCheckedChangeListener);
        toggleVoiceRecognizer.setChecked(voiceRecognizerPreference);

        toggleShakeDeviceDetector = (ToggleButton) findViewById(R.id.settings_toggle_shake);
        toggleShakeDeviceDetector.setOnCheckedChangeListener(OnCheckedChangeListener);
        toggleShakeDeviceDetector.setChecked(shakeDeviceDetectorPreference);

        toggleWhilePressed = (ToggleButton) findViewById(R.id.settings_toggle_pressed);
        toggleWhilePressed.setOnCheckedChangeListener(OnCheckedChangeListener);
        toggleWhilePressed.setChecked(whilePressedPreference);

        aboutBack = (Button) findViewById(R.id.button_about);
        aboutBack.setOnClickListener(onClickListener);

        buttonBack = (Button) findViewById(R.id.button_back);
        buttonBack.setOnClickListener(onClickListener);
    }

    CompoundButton.OnCheckedChangeListener OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.settings_toggle_voice:
                    appPreferencesEditor.putBoolean("voiceRecognizer", isChecked);
                    break;
                case R.id.settings_toggle_shake:
                    appPreferencesEditor.putBoolean("shakeDeviceDetector", isChecked);
                    break;
                case R.id.settings_toggle_pressed:
                    appPreferencesEditor.putBoolean("whilePressed", isChecked);
                    break;
            }
            appPreferencesEditor.commit();
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_about:
                    Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    break;
                case R.id.button_back:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
