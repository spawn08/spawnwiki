package spawnai.com.spawnwiki.interfaces;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import spawnai.com.spawnwiki.models.SpawnWikiModel;

/**
 * Created by amarthakur on 11/02/19.
 */

public interface ISpawnAPI {

    @GET("https://en.wikipedia.org/api/rest_v1/page/summary/{entity}")
    Call<SpawnWikiModel> getWiki(@Path("entity") String entity);
}
