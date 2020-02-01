package com.anime.cloud;

import java.io.Serializable;

public class PojoAnime implements Serializable {

    private String titleAnime;
    private String urlImg;
    private String urlAnimePage;

    public PojoAnime(String titleAnime, String urlImg, String urlAnimePage) {
        this.titleAnime = titleAnime;
        this.urlImg = urlImg;
        this.urlAnimePage = urlAnimePage;
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
}
