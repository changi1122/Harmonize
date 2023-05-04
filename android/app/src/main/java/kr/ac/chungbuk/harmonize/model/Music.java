package kr.ac.chungbuk.harmonize.model;

public class Music {

    public Long id;
    public String name;
    public String artist;
    public String thumbnail;
    public Integer numberTJ;
    public String youtubeLink;
    public String category;
    public Double max;
    public Double min;
    public Integer level;
    public Integer matchRate;
    public Boolean isFavorite;

    public Music(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }

    public Music(String name, String artist, int level, int matchRate, boolean isFavorite) {
        this(name, artist);
        this.level = level;
        this.matchRate = matchRate;
        this.isFavorite = isFavorite;
    }

    public Music(String name, String artist, int level,
                 int matchRate, boolean isFavorite, String thumbnail) {
        this(name, artist, level, matchRate, isFavorite);
        this.thumbnail = thumbnail;
    }
}
