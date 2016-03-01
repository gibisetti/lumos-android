package com.br.leonardomorais.lumos.features.listeners;

import android.content.Context;
import android.widget.Toast;

import com.br.leonardomorais.lumos.R;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * Created by leonardo on 01/03/16.
 */
public class VoiceRecognizer implements RecognitionListener {

    private Context context;
    public SpeechRecognizer recognizer;
    private static String COMMANDS_SEARCH = "commands";
    private OnRecognizerListener recognitionListener;

    public VoiceRecognizer(Context context){
        this.context = context;
    }

    public interface OnRecognizerListener {
        public void onRecognize(String wordRecognized);
    }

    public void setOnRecognizerListener(OnRecognizerListener onRecognizerListener){
        this.recognitionListener = onRecognizerListener;
    }


    public void startListener(){
        try {
            Assets assets = new Assets(context);
            File assetDir = assets.syncAssets();
            setupRecognizer(assetDir);
        } catch (IOException e) {
            Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.error_start_recognizer), Toast.LENGTH_LONG).show();
        }

        recognizer.startListening(COMMANDS_SEARCH);
    }

    public void endListener(){
        recognizer.cancel();
        recognizer.shutdown();
    }

    public void setupRecognizer(File assetsDir) throws IOException {
        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                .setKeywordThreshold(1e-40f)
                .setBoolean("-allphone_ci", false)
                .getRecognizer();

        recognizer.addListener(this);

        File menuGrammar = new File(assetsDir, "command.gram");
        recognizer.addGrammarSearch(COMMANDS_SEARCH, menuGrammar);
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis != null){
            recognizer.stop();
            recognitionListener.onRecognize(hypothesis.getHypstr());
            recognizer.startListening(COMMANDS_SEARCH);
        }else{
            return;
        }
    }

    @Override
    public void onError(Exception e) {
        Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.error_while_recognizing), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBeginningOfSpeech() {}

    @Override
    public void onEndOfSpeech() {}

    @Override
    public void onResult(Hypothesis hypothesis) {}

    @Override
    public void onTimeout() {}
}