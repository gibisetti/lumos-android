package com.br.leonardomorais.lumos.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

import com.br.leonardomorais.lumos.R;

/**
 * Created by leonardo on 01/03/16.
 */
public class WandActivity extends Activity {

    private SharedPreferences appPreferences;
    boolean firstTimeUse;
    private Intent intent;
    private ImageButton buttonSettings;
    private ImageButton buttonShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wand_activity);

        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        firstTimeUse = appPreferences.getBoolean("firstTimeUse", true);

        buttonSettings = (ImageButton) findViewById(R.id.button_settings);
        buttonSettings.setOnClickListener(onClickListener);

        buttonShare = (ImageButton) findViewById(R.id.button_share);
        buttonShare.setOnClickListener(onClickListener);

        if(firstTimeUse){
            intent = new Intent(WandActivity.this, TipActivity.class);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }else{
            loadFeatures();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_settings:
                    intent = new Intent(WandActivity.this, SettingsActivity.class);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    break;
                case R.id.button_share:
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, WandActivity.this.getResources().getString(R.string.app_share_message) + "\n https://play.google.com/store/apps/details?id=com.br.leonardomorais.lumos");
                    startActivity(Intent.createChooser(intent, WandActivity.this.getResources().getString(R.string.app_share_title)));
                    break;
            }
        }
    };

    private void loadFeatures() {

    }
}
