package com.anime.cloud.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class UrlVideo implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("anime_id")
    @Expose
    private Integer animeId;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("visite")
    @Expose
    private Integer visite;
    @SerializedName("hidden")
    @Expose
    private Integer hidden;

    /**
     * No args constructor for use in serialization
     *
     */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAnimeId() {
        return animeId;
    }

    public void setAnimeId(Integer animeId) {
        this.animeId = animeId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getVisite() {
        return visite;
    }

    public void setVisite(Integer visite) {
        this.visite = visite;
    }

    public Integer getHidden() {
        return hidden;
    }

    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("animeId", animeId).append("number", number).append("createdAt", createdAt).append("link", link).append("visite", visite).append("hidden", hidden).toString();
    }

}