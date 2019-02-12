package spawnai.com.spawnwiki;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import spawnai.com.spawnwiki.utils.TextSpeech;

public class SpawnWikiActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText searchText;
    private TextView button;
    private SpawnWikiActivity spawnWikiActivity;
    private ConstraintLayout container;
    private ProgressBar progressBar;
    private ImageView spawnMic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spawn_wiki);
        spawnWikiActivity = this;
        searchText = findViewById(R.id.search_text);
        button = findViewById(R.id.button_search);
        container = findViewById(R.id.container);
        progressBar = findViewById(R.id.progress);
        spawnMic = findViewById(R.id.spawn_mic);

        TextSpeech.getInstance().initialiseTTS(spawnWikiActivity.getApplicationContext());

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_GO) {
                    callWikiAPI(searchText.getText().toString());
                    hideKeyboardFrom(spawnWikiActivity, container);
                    return true;
                }

                return false;
            }
        });

        button.setOnClickListener(this);
        spawnMic.setOnClickListener(this);

    }

    private void callWikiAPI(final String entity) {

        final String cloneEntity = entity.trim().replace(" ", "_").toLowerCase();
        Log.d("ENTITY ", cloneEntity);
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ISPAWNWIKICONSTANT.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ISpawnAPI spawnAPI = retrofit.create(ISpawnAPI.class);
        Call<SpawnWikiModel> data = spawnAPI.getWiki(cloneEntity);
        data.enqueue(new Callback<SpawnWikiModel>() {
            @Override
            public void onResponse(Call<SpawnWikiModel> call, Response<SpawnWikiModel> response) {
                if (response.isSuccessful()) {
                    hideKeyboardFrom(spawnWikiActivity, container);
                    Log.d("API CONTENT ", response.body().toString());
                    SpawnWikiModel spawnWikiModel = response.body();
                    if (spawnWikiModel.getType().equals("disambiguation")) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(spawnWikiActivity, "Page not found!", Toast.LENGTH_SHORT).show();
                    } else {
                        SpawnEntityFragment spawnEntityFragment = new SpawnEntityFragment();
                        spawnEntityFragment.setData(spawnWikiModel);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
                                .addToBackStack(null)
                                .replace(R.id.container, spawnEntityFragment, "spawn");
                        progressBar.setVisibility(View.GONE);
                        button.setVisibility(View.GONE);

                        ft.commit();
                    }

                } else {
                    hideKeyboardFrom(spawnWikiActivity, container);
                    progressBar.setVisibility(View.GONE);
                    if (spawnWikiActivity != null)
                        Toast.makeText(spawnWikiActivity, "Page not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SpawnWikiModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
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
                    hideKeyboardFrom(spawnWikiActivity, container);
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to the mic..");
                    startActivityForResult(intent, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.spawn_mic:
                try {
                    hideKeyboardFrom(spawnWikiActivity, container);
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to the mic..");
                    startActivityForResult(intent, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
                    searchText.setSelection(voiceSpeech.get(0).length());
                    callWikiAPI(voiceSpeech.get(0));
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int count = 0;
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            button.setVisibility(View.VISIBLE);
            count++;
            if (count > 1) {
                count = 0;
                this.finish();
            }
        } else
            getSupportFragmentManager().popBackStack();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
