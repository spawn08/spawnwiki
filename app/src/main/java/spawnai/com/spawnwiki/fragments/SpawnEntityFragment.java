package spawnai.com.spawnwiki.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import spawnai.com.spawnwiki.R;
import spawnai.com.spawnwiki.models.SpawnWikiModel;

/**
 * Created by amarthakur on 11/02/19.
 */

public class SpawnEntityFragment extends Fragment {

    private SpawnWikiModel spawnWikiModel;
    private ImageView profileImage;
    private TextView title;
    private TextView description;


    public void setData(SpawnWikiModel spawnWikiModel) {
        this.spawnWikiModel = spawnWikiModel;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.spawn_entity_fragment, container, false);
        title = view.findViewById(R.id.title);
        profileImage = view.findViewById(R.id.profile_image);
        description = view.findViewById(R.id.description);
        updateView();
        return view;

    }

    private void updateView() {
        if (spawnWikiModel != null) {
            title.setText(spawnWikiModel.getDisplaytitle());
            description.setText(spawnWikiModel.getExtract());
            if (spawnWikiModel.getThumbnail() != null && spawnWikiModel.getThumbnail().getSource() != null) {
                Glide.with(getActivity())
                        .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.default_portrait).error(R.drawable.default_portrait))
                        .load(spawnWikiModel.getThumbnail().getSource())
                        .apply(new RequestOptions().circleCrop())
                        .into(profileImage);
            } else {
                Glide.with(getActivity())
                        .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.default_portrait).error(R.drawable.default_portrait))
                        .load(R.drawable.default_portrait)
                        .apply(new RequestOptions().circleCrop())
                        .into(profileImage);
            }
        }
    }


}
