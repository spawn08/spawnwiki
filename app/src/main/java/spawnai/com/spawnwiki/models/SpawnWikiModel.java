package spawnai.com.spawnwiki.models;

import org.json.JSONObject;

/**
 * Created by amarthakur on 11/02/19.
 */

public class SpawnWikiModel {
    public String title;
    public String displaytitle;
    public String description;
    public String extract;
    public Thumbnail thumbnail;
    public String type;

    public JSONObject content_urls;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplaytitle() {
        return displaytitle;
    }

    public void setDisplaytitle(String displaytitle) {
        this.displaytitle = displaytitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public JSONObject getContent_urls() {
        return content_urls;
    }

    public void setContent_urls(JSONObject content_urls) {
        this.content_urls = content_urls;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Type: " + type + " Display Title: " + displaytitle +
                " Description " + description + " Extract: " + extract;
    }
}
