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

    public PojoAnime(String titleAnime) {
        this.titleAnime = titleAnime;
    }

    public String getTitleAnime() {
        return titleAnime;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public String getUrlAnimePage() {
        return urlAnimePage;
    }

}
