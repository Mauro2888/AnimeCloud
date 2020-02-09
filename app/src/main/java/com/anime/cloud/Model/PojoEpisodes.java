package com.anime.cloud.Model;

public class PojoEpisodes {
    private String episode;
    private String url;

    public PojoEpisodes(String episode) {
        this.episode = episode;
    }

    public PojoEpisodes(String episode, String url) {
        this.episode = episode;
        this.url = url;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
