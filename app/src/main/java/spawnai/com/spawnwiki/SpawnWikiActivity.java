package spawnai.com.spawnwiki;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import spawnai.com.spawnwiki.Constants.ISPAWNWIKICONSTANT;
import spawnai.com.spawnwiki.fragments.SpawnEntityFragment;
import spawnai.com.spawnwiki.interfaces.ISpawnAPI;
import spawnai.com.spawnwiki.models.SpawnWikiModel;

public class SpawnWikiActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText searchText;
    private Button button;
    private SpawnWikiActivity spawnWikiActivity;
    private ConstraintLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spawn_wiki);
        spawnWikiActivity = this;
        searchText = findViewById(R.id.search_text);
        button = findViewById(R.id.button_search);
        container = findViewById(R.id.container);
        button.setOnClickListener(this);


    }

    private void callWikiAPI(String entity) {

        // entity = entity.trim().replace(" ", "%20");
        Log.d("ENTITY ", entity);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ISPAWNWIKICONSTANT.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ISpawnAPI spawnAPI = retrofit.create(ISpawnAPI.class);
        Call<SpawnWikiModel> data = spawnAPI.getWiki(entity);
        data.enqueue(new Callback<SpawnWikiModel>() {
            @Override
            public void onResponse(Call<SpawnWikiModel> call, Response<SpawnWikiModel> response) {
                if (response.isSuccessful()) {
                    Log.d("API CONTENT ", response.body().toString());
                    SpawnWikiModel spawnWikiModel = response.body();
                    SpawnEntityFragment spawnEntityFragment = new SpawnEntityFragment();
                    spawnEntityFragment.setData(spawnWikiModel);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
                            .replace(R.id.container, spawnEntityFragment, "spawn");

                    button.setVisibility(View.GONE);

                    ft.commit();

                } else {
                    if (spawnWikiActivity != null)
                        Toast.makeText(spawnWikiActivity, "Page not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SpawnWikiModel> call, Throwable t) {
                if (spawnWikiActivity != null)
                    Toast.makeText(spawnWikiActivity, "Page not found!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_search:
                try {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to the mic..");
                    startActivityForResult(intent, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*String entity = searchText.getText().toString();
                callWikiAPI(entity);*/
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1000:
                if (requestCode == 1000 && data != null) {
                    ArrayList<String> voiceSpeech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchText.setText(voiceSpeech.get(0));
                    callWikiAPI(voiceSpeech.get(0));
                }
                break;
        }
    }
}
