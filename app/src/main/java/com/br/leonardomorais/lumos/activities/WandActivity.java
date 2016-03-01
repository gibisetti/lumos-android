package com.br.leonardomorais.lumos.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.br.leonardomorais.lumos.R;
import com.br.leonardomorais.lumos.features.Flashlight;
import com.br.leonardomorais.lumos.features.SpellSound;
import com.br.leonardomorais.lumos.features.listeners.ShakeDeviceDetector;
import com.br.leonardomorais.lumos.features.listeners.VoiceRecognizer;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by leonardo on 01/03/16.
 */
@RuntimePermissions
public class WandActivity extends Activity {

    public static boolean wandState = false;

    /* Preferences */
    private SharedPreferences appPreferences;
    boolean whilePressedPreference;
    boolean shakeDeviceDetectorPreference;
    boolean voiceRecognizerPreference;
    boolean firstTimeUse;

    /* Features */
    private SpellSound spellSound;
    private Flashlight flashlight;
    private ShakeDeviceDetector shakeDeviceDetector;
    private VoiceRecognizer voiceRecognizer;

    /* UI Components */
    private ImageButton buttonSettings;
    private ImageButton buttonShare;
    private ImageView wandOn;
    private FrameLayout wandOff;
    private Animation animation;

    private Intent intent;

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

        wandOff = (FrameLayout) findViewById(R.id.wand_off);
        wandOn = (ImageView) findViewById(R.id.wand_on);

        spellSound = new SpellSound(this);
        flashlight = new Flashlight(this);

        if(firstTimeUse){
            intent = new Intent(WandActivity.this, TipActivity.class);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }else{
            loadFeatures();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WandActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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

    View.OnClickListener wandClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.wand_on:
                    WandActivityPermissionsDispatcher.darkWandWithCheck(WandActivity.this);
                    break;
                case R.id.wand_off:
                    WandActivityPermissionsDispatcher.lightWandWithCheck(WandActivity.this);
                    break;
            }
        }
    };

    View.OnTouchListener wandTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    WandActivityPermissionsDispatcher.lightWandWithCheck(WandActivity.this);
                    break;
                case MotionEvent.ACTION_UP:
                    WandActivityPermissionsDispatcher.darkWandWithCheck(WandActivity.this);
                    break;
            }
            return false;
        }
    };

    @NeedsPermission(Manifest.permission.CAMERA)
    public void lightWand(){
        wandImageFadein();
        spellSound.playSoundOn();
        flashlight.turnOn();
        wandState = true;
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void darkWand(){
        wandImageFadeout();
        spellSound.playSoundOff();
        flashlight.turnOff();
        wandState = false;
    }

    private void wandImageFadein(){
        animation = AnimationUtils.loadAnimation(WandActivity.this, R.anim.fadein);
        wandOn.setVisibility(View.VISIBLE);
        wandOn.startAnimation(animation);
    }

    private void wandImageFadeout(){
        animation = AnimationUtils.loadAnimation(WandActivity.this, R.anim.fadeout);
        wandOn.setVisibility(View.GONE);
        wandOn.startAnimation(animation);
    }

    private void loadFeatures() {
        whilePressedPreference = appPreferences.getBoolean("whilePressed", false);
        shakeDeviceDetectorPreference = appPreferences.getBoolean("shakeDeviceDetector", true);
        voiceRecognizerPreference = appPreferences.getBoolean("voiceRecognizer", true);

        if(!whilePressedPreference){
            wandOff.setOnTouchListener(null);
            wandOff.setOnClickListener(wandClickListener);
            wandOn.setOnClickListener(wandClickListener);
        }else{
            wandOff.setOnTouchListener(wandTouchListener);
            wandOff.setOnClickListener(null);
            wandOn.setOnClickListener(null);
        }

        if(!shakeDeviceDetectorPreference && shakeDeviceDetector != null){
            shakeDeviceDetector.endListener();
            shakeDeviceDetector = null;
        }

        if(shakeDeviceDetectorPreference && shakeDeviceDetector == null) {
            shakeDeviceDetector = new ShakeDeviceDetector(this);
            shakeDeviceDetector.setOnShakeListener(new ShakeDeviceDetector.OnShakeListener() {
                @Override
                public void onShake() {
                    if (wandState) {
                        WandActivityPermissionsDispatcher.darkWandWithCheck(WandActivity.this);
                    } else {
                        WandActivityPermissionsDispatcher.lightWandWithCheck(WandActivity.this);
                    }
                }
            });
            shakeDeviceDetector.starListener();
        }

        if(!voiceRecognizerPreference && voiceRecognizer != null) {
            voiceRecognizer.endListener();
            voiceRecognizer = null;
        }

        if(voiceRecognizerPreference && voiceRecognizer == null){
            WandActivityPermissionsDispatcher.initializeReconhecedorWithCheck(WandActivity.this);
        }
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO })
    public void initializeReconhecedor(){
        voiceRecognizer = new VoiceRecognizer(this);
        voiceRecognizer.setOnRecognizerListener(new VoiceRecognizer.OnRecognizerListener() {
            @Override
            public void onRecognize(String wordRecognized) {
                if (wordRecognized.equals("lumos")) {
                    WandActivityPermissionsDispatcher.lightWandWithCheck(WandActivity.this);
                } else if (wordRecognized.equals("nox")) {
                    WandActivityPermissionsDispatcher.darkWandWithCheck(WandActivity.this);
                }
            }
        });
        voiceRecognizer.startListener();
    }

    @Override
    protected void onActivityResult(int x, int y, Intent z) {
        loadFeatures();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(flashlight != null) {
            flashlight.release();
        }
        if(shakeDeviceDetector != null) {
            shakeDeviceDetector.endListener();
        }
        if(voiceRecognizer != null) {
            voiceRecognizer.endListener();
        }
    }
}
