package spawnai.com.spawnwiki.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by amarthakur on 12/02/19.
 */

public class TextSpeech {

    private static TextSpeech textSpeech;
    private TextToSpeech textToSpeech;


    public static synchronized TextSpeech getInstance() {

        if (textSpeech == null) {
            textSpeech = new TextSpeech();
        }

        return textSpeech;

    }

    public void initialiseTTS(Context context) {
        if (textToSpeech == null)
            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        textToSpeech.setLanguage(Locale.ENGLISH);
                    }
                }
            });
    }

    public void speak(String text) {
        try {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        try {
            textToSpeech.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public TextToSpeech getTextToSpeech() {
        return textToSpeech;
    }
}
