package kr.ac.chungbuk.harmonize.model;

import com.google.gson.annotations.Expose;

public class MusicDetail {

    @Expose
    public Long music_id;
    @Expose
    public String music_name;
    @Expose
    public String artist;
    @Expose
    public String img_link;
    @Expose
    public Integer level;
    @Expose
    public Integer range_avg;
    @Expose
    public Boolean is_prefer;
    @Expose
    public Long category_id;
    @Expose
    public Double max;
    @Expose
    public Double min;
    @Expose
    public Integer tj_num;
    @Expose
    public Double high;
    @Expose
    public Double low;


    public MusicDetail(Long music_id, String music_name, String artist, String img_link, Integer level,
                       Integer range_avg, Boolean is_prefer, Long category_id, Double max, Double min, Integer tj_num) {
        this.music_id = music_id;
        this.music_name = music_name;
        this.artist = artist;
        this.img_link = img_link;
        this.level = level;
        this.range_avg = range_avg;
        this.is_prefer = is_prefer;
        this.category_id = category_id;
        this.max = max;
        this.min = min;
        this.tj_num = tj_num;
    }

    public Long getMusic_id() {
        return music_id;
    }

    public String getMusic_name() {
        return music_name;
    }

    public String getArtist() {
        return artist;
    }

    public String getImg_link() {
        return img_link;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getRange_avg() {
        return range_avg;
    }

    public Boolean getIs_prefer() {
        return is_prefer;
    }

    public Long getCategory() {
        return category_id;
    }
}
