package com.anime.cloud.Model;

import java.io.Serializable;

public class Episodi implements Serializable {
    private long idAnime;
    private String link;
    private String episodeNumber;

    public Episodi(long idAnime, String link, String episodeNumber) {
        this.idAnime = idAnime;
        this.link = link;
        this.episodeNumber = episodeNumber;
    }

    public long getIdAnime() {
        return idAnime;
    }

    public void setIdAnime(long idAnime) {
        this.idAnime = idAnime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(String episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    @Override
    public String toString() {
        return "Episodi{" +
                "idAnime=" + idAnime +
                ", link='" + link + '\'' +
                ", episodeNumber='" + episodeNumber + '\'' +
                '}';
    }
}
