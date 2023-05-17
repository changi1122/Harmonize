package kr.ac.chungbuk.harmonize.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MusicSearchResult {

    @Expose
    @SerializedName("music_id")
    public Long id;
    @Expose
    @SerializedName("music_name")
    public String name;
    @Expose
    public String artist;
    @Expose
    @SerializedName("img_link")
    public String thumbnail;
    @Expose
    public Integer level;
    public Integer matchRate;
    public Boolean isFavorite;
}
