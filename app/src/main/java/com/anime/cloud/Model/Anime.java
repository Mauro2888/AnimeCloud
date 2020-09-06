package com.anime.cloud.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class Anime extends AnimePojo implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("titleAnime")
    @Expose
    private String titleAnime;
    @SerializedName("urlImage")
    @Expose
    private String urlImage;
    @SerializedName("mangaka")
    @Expose
    private String mangaka;
    @SerializedName("plotAnime")
    @Expose
    private String plotAnime;
    @SerializedName("urlVideo")
    @Expose
    private List<Episodi> urlVideo = null;

    public Anime(Integer id, String titleAnime, String urlImage, String mangaka, String plotAnime) {
        super();
        this.id = id;
        this.titleAnime = titleAnime;
        this.urlImage = urlImage;
        this.mangaka = mangaka;
        this.plotAnime = plotAnime;
    }

    /**
     *
     * @param mangaka
     * @param urlVideo
     * @param id
     * @param titleAnime
     * @param urlImage
     * @param plotAnime
     */
    public Anime(Integer id, String titleAnime, String urlImage, String mangaka, String plotAnime, List<Episodi> urlVideo) {
        super();
        this.id = id;
        this.titleAnime = titleAnime;
        this.urlImage = urlImage;
        this.mangaka = mangaka;
        this.plotAnime = plotAnime;
        this.urlVideo = urlVideo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitleAnime() {
        return titleAnime;
    }

    public void setTitleAnime(String titleAnime) {
        this.titleAnime = titleAnime;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getMangaka() {
        return mangaka;
    }

    public void setMangaka(String mangaka) {
        this.mangaka = mangaka;
    }

    public String getPlotAnime() {
        return plotAnime;
    }

    public void setPlotAnime(String plotAnime) {
        this.plotAnime = plotAnime;
    }

    public List<Episodi> getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(List<Episodi> urlVideo) {
        this.urlVideo = urlVideo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("titleAnime", titleAnime).append("urlImage", urlImage).append("mangaka", mangaka).append("plotAnime", plotAnime).append("urlVideo", urlVideo).toString();
    }
}