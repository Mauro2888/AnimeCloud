package com.anime.cloud.Model;

import java.io.Serializable;

public class PojoAnime implements Serializable {

    private String titleAnime;
    private String urlImg;
    private String urlAnimePage;
    private String generi;
    private String trama;

    public PojoAnime(String titleAnime, String urlImg, String urlAnimePage, String generi, String trama) {
        this.titleAnime = titleAnime;
        this.urlImg = urlImg;
        this.urlAnimePage = urlAnimePage;
        this.generi = generi;
        this.trama = trama;
    }

    public String getTitleAnime() {
        return titleAnime;
    }

    public void setTitleAnime(String titleAnime) {
        this.titleAnime = titleAnime;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getUrlAnimePage() {
        return urlAnimePage;
    }

    public void setUrlAnimePage(String urlAnimePage) {
        this.urlAnimePage = urlAnimePage;
    }

    public String getGeneri() {
        return generi;
    }

    public void setGeneri(String generi) {
        this.generi = generi;
    }

    public String getTrama() {
        return trama;
    }

    public void setTrama(String trama) {
        this.trama = trama;
    }
}
