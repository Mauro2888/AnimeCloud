package com.anime.cloud.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class AnimePojo implements Serializable {

    @SerializedName("Anime")
    @Expose
    private List<Anime> anime = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public AnimePojo() {
    }

    /**
     *
     * @param anime
     */
    public AnimePojo(List<Anime> anime) {
        super();
        this.anime = anime;
    }

    public List<Anime> getAnime() {
        return anime;
    }

    public void setAnime(List<Anime> anime) {
        this.anime = anime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("anime", anime).toString();
    }

}